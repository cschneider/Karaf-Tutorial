package net.lr.tasklist.persistence.impl;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InitHelper {
    Logger LOG = LoggerFactory.getLogger(InitHelper.class);
    
    @Reference
    TaskService taskService;
    
    @Activate
    public void addDemoTasks() {
        try {
            Task task = new Task(1, "Just a sample task", "Some more info");
            taskService.addTask(task);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }
}
