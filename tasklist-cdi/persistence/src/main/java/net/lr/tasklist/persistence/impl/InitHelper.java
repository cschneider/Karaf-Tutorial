package net.lr.tasklist.persistence.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            taskService.deleteTask(1);
        }
        catch (Exception e1) {
        }
        Runnable command = new Runnable() {
            
            @Override
            public void run() {
                taskWatcher();
            }
        };
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        newSingleThreadExecutor.execute(command);
        try {
            LOG.info("Adding sample task");
            taskService.addTask(createTask());
            LOG.info("Number of tasks is now: " + taskService.getTasks().size());
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }
    
    private void taskWatcher() {
        Thread.currentThread().setName("taskWatcher");
        for (int c=0; c<10;c++) {
            LOG.info("Number in check " + taskService.getTasks().size());
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
            }
        }
    }

    private Task createTask() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Just a sample task");
        task1.setDescription("Some more info");
        return task1;
    }
}
