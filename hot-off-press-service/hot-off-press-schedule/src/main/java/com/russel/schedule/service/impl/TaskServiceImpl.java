package com.russel.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.russel.schedule.mapper.TaskinfoLogsMapper;
import com.russel.schedule.mapper.TaskinfoMapper;
import com.russel.schedule.service.TaskService;
import com.russel.common.constants.ScheduleConstants;
import com.russel.common.redis.CacheService;
import com.russel.model.schedule.dtos.Task;
import com.russel.model.schedule.pojos.Taskinfo;
import com.russel.model.schedule.pojos.TaskinfoLogs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Russel
 * @DATE 2023/10/24.
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    /**
     * 添加延迟任务
     *
     * @param task
     * @return
     */
    @Override
    @Transactional
    public long addTask(Task task) {

        //1.添加任务到数据库中

        boolean success = addTaskToDb(task);

        if (success) {
            //2.添加任务到redis
            addTaskToCache(task);
        }


        return task.getTaskId();
    }

    @Override
    public boolean cancelTask(long taskId) {
        boolean flag = false;
        Task task = updateDb(taskId, ScheduleConstants.CANCELLED);
        if (task != null) {
            flag = true;
            removeTaskFromCache(task);
        }
        return flag;
    }

    @Override
    public Task poll(int type, int priority) {
        Task task = null;
        try {
            String key = type + "_" + priority;
            String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
            if (StringUtils.isNoneBlank(task_json)) {
                task = JSON.parseObject(task_json, Task.class);
                updateDb(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            log.error("拉取任务失败，type:{},priority:{}", type, priority, e);
        }
        return task;
    }

    @Scheduled(cron = "0*/1 * * * * ?")
    public void refresh() {
        String token = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);
        if (StringUtils.isBlank(token)) {
            return;
        }
        log.info(new Date(System.currentTimeMillis()) + "执行了定时任务");
        //1.获取当前时间的后5分钟的时间
        Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
        for (String futureKey : futureKeys) {
            String topicKey = ScheduleConstants.TOPIC + futureKey.split(ScheduleConstants.FUTURE)[1];
            Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());
            if (!tasks.isEmpty()) {
                cacheService.refreshWithPipeline(futureKey, topicKey, tasks);
                log.info("成功的将" + futureKey + "中的任务刷新到" + topicKey + "中");
            }
        }
    }

    @Scheduled(cron = "0 */5 * * * ?")
    @PostConstruct
    public void reloadData() {
        clearCache();
        log.info("数据库数据同步到缓存");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);

        //查看小于未来5分钟的所有任务
        List<Taskinfo> allTasks = taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime,calendar.getTime()));
        if(allTasks != null && !allTasks.isEmpty()){
            for (Taskinfo taskinfo : allTasks) {
                Task task = new Task();
                BeanUtils.copyProperties(taskinfo,task);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                addTaskToCache(task);
            }
        }
    }

    private void clearCache(){
        // 删除缓存中未来数据集合和当前消费者队列的所有key
        Set<String> futurekeys = cacheService.scan(ScheduleConstants.FUTURE + "*");// future_
        Set<String> topickeys = cacheService.scan(ScheduleConstants.TOPIC + "*");// topic_
        cacheService.delete(futurekeys);
        cacheService.delete(topickeys);
    }

    private void removeTaskFromCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, JSON.toJSONString(task));
        } else {
            cacheService.zRemove(ScheduleConstants.FUTURE + key, JSON.toJSONString(task));
        }
    }

    private Task updateDb(long taskId, int status) {
        Task task = null;
        try {
            taskinfoMapper.deleteById(taskId);
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);
            task = new Task();
            BeanUtils.copyProperties(taskinfoLogs, task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        } catch (Exception e) {
            log.error("取消任务失败，taskId:{}", taskId, e);
        }
        return task;
    }

    @Autowired
    private CacheService cacheService;

    /**
     * 把任务添加到redis中
     *
     * @param task
     */
    private void addTaskToCache(Task task) {

        String key = task.getTaskType() + "_" + task.getPriority();

        //获取5分钟之后的时间  毫秒值
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();

        //2.1 如果任务的执行时间小于等于当前时间，存入list
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            //2.2 如果任务的执行时间大于当前时间 && 小于等于预设时间（未来5分钟） 存入zset中
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), task.getExecuteTime());
        }
    }

    @Autowired
    private TaskinfoMapper taskinfoMapper;

    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    /**
     * 添加任务到数据库中
     *
     * @param task
     * @return
     */
    private boolean addTaskToDb(Task task) {

        boolean flag = false;

        try {
            //保存任务表
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task, taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);
            //设置taskID
            task.setTaskId(taskinfo.getTaskId());

            //保存任务日志数据
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo, taskinfoLogs);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);

            flag = true;
        } catch (Exception e) {
            log.error("添加任务失败，taskId:{}", task.getTaskId(), e);
        }
        return flag;
    }
}
