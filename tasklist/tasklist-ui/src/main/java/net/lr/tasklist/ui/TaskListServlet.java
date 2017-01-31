package net.lr.tasklist.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@OsgiServiceProvider(classes = Servlet.class)
@Properties({@Property(name = "alias", value = "/tasklist"), // For pax web
		@Property(name = "osgi.http.whiteboard.servlet.pattern", value = "/tasklist") // For felix http
}) 
@Singleton
public class TaskListServlet extends HttpServlet {
    @Inject @OsgiService
    TaskService taskService;

    private static final long serialVersionUID = 34992072289535683L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        resp.setContentType("text/html");
        String taskId = req.getParameter("taskId");
        PrintWriter writer = resp.getWriter();
        if (taskId != null && taskId.length() > 0) {
            showTask(writer, taskId);
        } else {
            showTaskList(writer);
        }
    }

    private void showTaskList(PrintWriter writer) {
        writer.println("<h1>Tasks</h1>");
        Collection<Task> tasks = taskService.getTasks();
        for (Task task : tasks) {
            writer.println("<a href=\"?taskId=" + task.getId() + "\">" + task.getTitle() + "</a><BR/>");
        }
    }

    private void showTask(PrintWriter writer, String taskId) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Task task = taskService.getTask(taskId);
        if (task != null) {
            writer.println("<h1>Task " + task.getTitle() + " </h1>");
            if (task.getDueDate() != null) {
                writer.println("Due date: " + sdf.format(task.getDueDate()) + "<br/>");
            }
            writer.println(task.getDescription());
        } else {
            writer.println("Task with id " + taskId + " not found");
        }

    }

}
