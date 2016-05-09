package net.lr.tasklist.persistence.impl;

import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@Named
public class InitHelper {
    Logger LOG = LoggerFactory.getLogger(InitHelper.class);
    @Inject
    TaskService taskService;

    @PostConstruct
    public void addDemoTasks() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (taskService.getTask(1) == null) {
                        addSampleTask();
                    }
                } catch (Exception e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
        });
 
    }

    private void addSampleTask() {
        Task task = new Task(1, "Just a sample task", "Some more info");
        taskService.addTask(task);
    }

}
