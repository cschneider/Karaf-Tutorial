package net.lr.tasklist.persistence.impl;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.lr.tasklist.model.Task;

import org.junit.Assert;
import org.junit.Test;

public class TaskServiceImplTest {

    @Test
    public void testWriteRead() throws Exception {
        TaskServiceImpl taskService = new TaskServiceImpl();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tasklist", System.getProperties());
        EntityManager em = emf.createEntityManager();
        taskService.em = em;

        em.getTransaction().begin();
        Task task = new Task();
        task.setId(1);
        task.setTitle("Test task");
        taskService.addTask(task);
        em.getTransaction().commit();
        Collection<Task> persons = taskService.getTasks();

        Assert.assertEquals(1, persons.size());
        Task task1 = persons.iterator().next();
        Assert.assertEquals(new Integer(1), task1.getId());
        Assert.assertEquals("Test task", task1.getTitle());
    }

}
