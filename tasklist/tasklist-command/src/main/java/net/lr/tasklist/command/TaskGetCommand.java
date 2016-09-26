package net.lr.tasklist.command;

import javax.inject.Inject;
import javax.inject.Singleton;

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
 @Property(name = "osgi.command.function", value = "get")
})
public class TaskGetCommand {

    @Inject @OsgiService
    TaskService taskService;

    public Task get(String id) throws Exception {
        return taskService.getTask(id);
    }

}
