package net.lr.tasklist.persistence.impl;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.aries.jpa.template.JpaTemplate;
import org.junit.Assert;
import org.junit.Test;

import net.lr.tasklist.model.Task;

public class TaskServiceImplTest {

    @Test
    public void testWriteRead() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tasklist", System.getProperties());
        EntityManager em = emf.createEntityManager();
        JpaTemplate jpaTemplate = new SimpleJpaTemplate(em);
        TaskServiceImpl taskService = new TaskServiceImpl();
        taskService.jpa = jpaTemplate;

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test task");
        
        em.getTransaction().begin();
        taskService.addTask(task);
        em.getTransaction().commit();
        Collection<Task> persons = taskService.getTasks();

        Assert.assertEquals(1, persons.size());
        Task task1 = persons.iterator().next();
        Assert.assertEquals(new Integer(1), task1.getId());
        Assert.assertEquals("Test task", task1.getTitle());
        em.close();
    }

}
