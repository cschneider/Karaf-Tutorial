package net.lr.tasklist.service.impl;

import java.net.URI;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ops4j.pax.cdi.api.OsgiService;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@Named
@Consumes({"application/json", "test/xml"})
@Produces({"application/json", "test/xml"})
public class TaskServiceRest {
    
    @OsgiService @Inject
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
