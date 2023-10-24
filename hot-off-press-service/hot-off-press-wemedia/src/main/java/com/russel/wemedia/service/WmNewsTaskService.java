package com.russel.wemedia.service;

import java.util.Date;

/**
 * author by Russel
 * created by 2023/10/24.
 */
public interface WmNewsTaskService {

    /**
     * 添加任务到延迟队列中
     * @param id  文章的id
     * @param publishTime  发布的时间  可以做为任务的执行时间
     */
    public void addNewsToTask(Integer id, Date publishTime);

    /**
     * 消费延迟队列数据
     */
    public void scanNewsByTask();

}
