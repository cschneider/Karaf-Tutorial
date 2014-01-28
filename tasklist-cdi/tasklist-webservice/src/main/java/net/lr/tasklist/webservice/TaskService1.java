package net.lr.tasklist.webservice;

import java.util.Collection;

import javax.inject.Inject;
import javax.jws.WebService;

import net.lr.cxf.cdi.CXFEndpoint;
import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

import org.ops4j.pax.cdi.api.OsgiService;

@WebService
@CXFEndpoint(url="test")
public class TaskService1 implements TaskService {
    @Inject @OsgiService
    TaskService taskService;

    @Override
    public Task getTask(String id) {
        return taskService.getTask(id);
    }

    @Override
    public void addTask(Task task) {
        taskService.addTask(task);
    }

    @Override
    public void updateTask(Task task) {
        taskService.updateTask(task);
    }

    @Override
    public void deleteTask(String id) {
        taskService.deleteTask(id);
    }

    @Override
    public Collection<Task> getTasks() {
        return taskService.getTasks();
    }

}
