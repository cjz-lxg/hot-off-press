package com.russel.schedule.service;

import com.russel.model.schedule.dtos.Task;

/**
 * author by Russel
 * created by 2023/10/24.
 */
public interface TaskService {
    public long addTask(Task task);

    public void cancelTask(long taskId);

    /**
     * 按照类型和优先级来拉取任务
     * @param type
     * @param priority
     * @return
     */
    public Task poll(int type,int priority);
}
