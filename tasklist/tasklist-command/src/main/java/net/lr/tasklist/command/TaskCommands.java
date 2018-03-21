package net.lr.tasklist.command;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.aries.blueprint.annotation.service.Reference;
import org.apache.aries.blueprint.annotation.service.Service;
import org.apache.aries.blueprint.annotation.service.ServiceProperty;
import org.apache.karaf.shell.table.ShellTable;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@Singleton
@Service(classes=TaskCommands.class,
properties= {
		@ServiceProperty(name = "osgi.command.scope", values = "task"),
		@ServiceProperty(name = "osgi.command.function", values = {"add", "list", "get"})
})
public class TaskCommands {
    
    @Inject @Reference
    TaskService taskService;
    
    public void add(String id, String title, String description) throws Exception {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        taskService.addTask(task);
    }
    
    public Task get(String id) throws Exception {
        return taskService.getTask(id);
    }
    
    public void list() throws Exception {
        ShellTable table = new ShellTable();
        table.column("id");
        table.column("title");
        for (Task task : taskService.getTasks()) {
            table.addRow().addContent(task.getId(), task.getTitle());
        }
        table.print(System.out);
    }
}
