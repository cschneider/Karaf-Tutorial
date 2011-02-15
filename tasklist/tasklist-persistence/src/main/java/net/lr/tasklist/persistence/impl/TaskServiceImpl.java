package net.lr.tasklist.persistence.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;


public class TaskServiceImpl implements TaskService {
	Map<String, Task> taskMap;
	
	public TaskServiceImpl() {
		taskMap = new HashMap<String, Task>();
		Task task = new Task();
		task.setId("1");
		task.setTitle("Buy coffee");
		task.setDescription("100% Arabican");
		addTask(task);
		task = new Task();
		task.setId("2");
		task.setTitle("Finish karaf tutorial");
		task.setDescription("Some more examples");
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
		return taskMap.values();
	}

}
