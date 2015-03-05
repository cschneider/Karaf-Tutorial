package net.lr.tasklist.persistence.impl;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class InitHelper {
    Logger LOG = LoggerFactory.getLogger(InitHelper.class);
    @Inject TaskService taskService;
    
    @PostConstruct
    public void addDemoTasks() {
        try {
            Task task = new Task(1, "Just a sample task", "Some more info");
            taskService.addTask(task);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

}
