package com.russel.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.russel.apis.article.IScheduleClient;
import com.russel.common.enums.TaskTypeEnum;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.schedule.dtos.Task;
import com.russel.model.wemedia.dtos.WmNews;
import com.russel.utils.common.ProtostuffUtil;
import com.russel.wemedia.service.WmNewsAutoScanService;
import com.russel.wemedia.service.WmNewsTaskService;
import io.protostuff.ProtostuffIOUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Russel
 * @DATE 2023/10/24.
 */
@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient scheduleClient;

    /**
     * 添加任务到延迟队列中
     * @param id          文章的id
     * @param publishTime 发布的时间  可以做为任务的执行时间
     */
    @Override
    @Async
    public void addNewsToTask(Integer id, Date publishTime) {

        log.info("添加任务到延迟服务中----begin");

        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));

        scheduleClient.addTask(task);

        log.info("添加任务到延迟服务中----end");

    }


    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;
    /**
     * 消费延迟队列数据
     */
    @Scheduled(fixedRate = 1000)
    @Override
    @SneakyThrows
    public void scanNewsByTask() {
        //TODO 后面优化下让它不消费同一个任务,这里我暂时先跳了

        log.info("文章审核---消费任务执行---begin---");

        ResponseResult responseResult = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if(responseResult.getCode().equals(200) && responseResult.getData() != null){
            String json_str = JSON.toJSONString(responseResult.getData());
            Task task = JSON.parseObject(json_str, Task.class);
            byte[] parameters = task.getParameters();
            WmNews wmNews = ProtostuffUtil.deserialize(parameters, WmNews.class);
            log.info(wmNews.getId()+"-----------");
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }
        log.info("文章审核---消费任务执行---end---");

    }

}
