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
        taskService.addTask(createTask());
    }
    
    private Task createTask() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Just a sample task");
        task1.setDescription("Some more info");
        return task1;
    }
}
