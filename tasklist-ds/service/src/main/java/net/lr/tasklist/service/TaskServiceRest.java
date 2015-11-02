package net.lr.tasklist.service;

import java.net.URI;
import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@Component(service=TaskServiceRest.class, property={"service.exported.interfaces=*",
                                                    "service.exported.configs=org.apache.cxf.rs",
                                                    "org.apache.cxf.rs.address=/tasklistRest"})
@Path("")
public class TaskServiceRest {
    @Reference
    TaskService taskService;
    
    @Context
    UriInfo uri;

    @GET
    @Path("{id}")
    public Response getTask(@PathParam("id") Integer id) {
        Task task = taskService.getTask(id);
        return task == null ? Response.status(Status.NOT_FOUND).build() : Response.ok(task).build();
    }

    @POST

    public Response addTask(Task task) {
        taskService.addTask(task);
        URI taskURI = uri.getRequestUriBuilder().path(TaskServiceRest.class, "getTask").build(task.getId());
        return Response.created(taskURI).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Task> getTasks() {
        return taskService.getTasks();
    }

    @PUT
    @Path("{id}")
    public void updateTask(@PathParam("id") Integer id, Task task) {
        task.setId(id);
        taskService.updateTask(task);
    }
    
    @DELETE
    @Path("{id}")
    public void deleteTask(@PathParam("id") Integer id) {
        taskService.deleteTask(id);
    }

}
