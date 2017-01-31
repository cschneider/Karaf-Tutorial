package net.lr.tasklist.persistence.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@OsgiServiceProvider(classes=TaskService.class)
@Properties(@Property(name = "service.exported.interfaces", value = "*"))
@Singleton
public class TaskServiceImpl implements TaskService {
    Map<String, Task> taskMap;

    public TaskServiceImpl() {
        taskMap = new HashMap<String, Task>();
        Task task = new Task();
        task.setId("1");
        task.setTitle("Buy some coffee");
        task.setDescription("Take the extra strong");
        addTask(task);
        task = new Task();
        task.setId("2");
        task.setTitle("Finish karaf tutorial");
        task.setDescription("Last check and wiki upload");
        addTask(task);
    }

    @Override
    public Task getTask(String id) {
        return taskMap.get(id);
    }

    @Override
    public void addTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    @Override
    public Collection<Task> getTasks() {
        // taskMap.values is not serializable
        return new ArrayList<Task>(taskMap.values());
    }

    @Override
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    @Override
    public void deleteTask(String id) {
        taskMap.remove(id);
    }

}
