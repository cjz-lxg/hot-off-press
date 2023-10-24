package com.ruseel.schedule.test;

import com.russel.schedule.ScheduleApplication;
import com.russel.schedule.service.TaskService;
import com.russel.common.constants.ScheduleConstants;
import com.russel.model.schedule.dtos.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

/**
 * @author Russel
 * @DATE 2023/10/24.
 */
@SpringBootTest(classes= ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;


    @Test
    public void addTask() {
        Task task = new Task();
        task.setTaskType(ScheduleConstants.SCHEDULED);
        task.setPriority(1);
        task.setParameters("test".getBytes());
        task.setExecuteTime(Calendar.getInstance().getTimeInMillis()+1000*60*6);
        taskService.addTask(task);
    }

    @Test
    public void cancelTask() {
        taskService.cancelTask(123L);
    }

}














