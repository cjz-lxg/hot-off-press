package com.russel.model.schedule.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Russel
 * @DATE 2023/10/24.
 */
@Data
public class Task implements Serializable {

    private static final long serialVersionUID = -5241340992298039183L;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 类型
     */
    private Integer taskType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 执行id
     */
    private long executeTime;

    /**
     * task参数
     */
    private byte[] parameters;

}
