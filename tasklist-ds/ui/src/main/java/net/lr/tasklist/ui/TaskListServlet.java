package net.lr.tasklist.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

@Component(
service = { Servlet.class }, 
property = { "alias:String=/tasklist" }
)
@Designate(ocd = TaskUIConfig.class)
public class TaskListServlet extends HttpServlet {
    @Reference
    private TaskService taskService;

    private String docTitle;

    private int numTasks;

    private static final long serialVersionUID = 34992072289535683L;
    
    @Activate
    public void activate(TaskUIConfig config) {
        this.docTitle = config.title();
        this.numTasks = config.numTasks();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException {
        String add = req.getParameter("add");
        String taskId = req.getParameter("taskId");
        String title = req.getParameter("title");
        PrintWriter writer = resp.getWriter();
        if (add != null) {
            addTask(taskId, title);
        } else if (taskId != null && taskId.length() > 0) {
            showTask(writer, taskId);
        } else {
            showTaskList(writer);
        }
    }

    private void addTask(String taskId, String title) {
        Task task = new Task();
        task.setId(new Integer(taskId));
        task.setTitle(title);
        taskService.addTask(task );
    }

    private void showTaskList(PrintWriter writer) {
        writer.println("<h1>" + docTitle + "</h1>");
        Collection<Task> tasks = taskService.getTasks();
        for (Task task : tasks) {
            writer.println("<a href=\"?taskId=" + task.getId() + "\">" + task.getTitle() + "</a><BR/>");
        }
        writer.println("<BR>\nShowing up to " + numTasks + " tasks");
    }

    private void showTask(PrintWriter writer, String taskId) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Task task = taskService.getTask(new Integer(taskId));
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
