package net.lr.tasklist.model;

import java.util.Collection;

public interface TaskService {
	Task getTask(String id);
	void addTask(Task task);
	Collection<Task> getTasks();
}
