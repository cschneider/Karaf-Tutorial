package org.example.utils.vaadinbridge.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;


public class LogTracker extends ServiceTracker implements LogService {
    
    public LogTracker(BundleContext context) { 
        super(context, LogService.class.getName(), null);
    }

    public void log(int level, String message) {
        log(null, level, message, null);
    }

    public void log(int level, String message, Throwable exception) {
        log(null, level, message, exception);
    }

    public void log(ServiceReference sr, int level, String message) {
        log(sr, level, message, null);
    }

    public void log(ServiceReference sr, int level, String message, Throwable exception) {
        LogService log = (LogService) getService();
        if(log != null)
            log.log(sr, level, message, exception);
    }
}