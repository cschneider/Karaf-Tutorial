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
            LOG.info("Adding sample task");
            Task task1 = new Task();
            task1.setId(1);
            task1.setTitle("Just a sample task");
            task1.setDescription("Some more info");
            taskService.addTask(task1);
            LOG.info("Number of tasks is now: " + taskService.getTasks().size());
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }
}
