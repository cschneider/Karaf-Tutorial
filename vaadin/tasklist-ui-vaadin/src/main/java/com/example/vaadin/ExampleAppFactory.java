package com.example.vaadin;

import net.lr.tasklist.model.TaskService;

import org.example.utils.vaadinbridge.ApplicationFactory;

import com.vaadin.Application;
import com.vaadin.Application.SystemMessages;

public class ExampleAppFactory implements ApplicationFactory {
    
    private final TaskService taskService;

    public ExampleAppFactory(TaskService taskService) {
        this.taskService = taskService;
    }
    
    public String getApplicationCSSClassName() {
        return "ExampleApplication";
    }

    public SystemMessages getSystemMessages() {
        return null;
    }

    public Application newInstance() {
        return new ExampleApplication(taskService);
    }
}