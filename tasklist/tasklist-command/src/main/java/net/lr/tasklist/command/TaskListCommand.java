package net.lr.tasklist.command;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.karaf.shell.table.ShellTable;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@Singleton
@OsgiServiceProvider
@Properties(//
{
 @Property(name = "osgi.command.scope", value = "task"),
 @Property(name = "osgi.command.function", value = "list")
})
public class TaskListCommand {
    
    @Inject @OsgiService
    TaskService taskService;
    
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
