package com.example.vaadin;

import org.example.utils.vaadinbridge.ApplicationFactory;

import com.vaadin.Application;
import com.vaadin.Application.SystemMessages;

public class ExampleAppFactory implements ApplicationFactory {
    
    public String getApplicationCSSClassName() {
        return "ExampleApplication";
    }

    public SystemMessages getSystemMessages() {
        return null;
    }

    public Application newInstance() {
        return new ExampleApplication();
    }
}