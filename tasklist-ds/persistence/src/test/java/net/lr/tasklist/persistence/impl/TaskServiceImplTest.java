package net.lr.tasklist.persistence.impl;

import java.util.Collection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.lr.tasklist.model.Task;

import org.apache.aries.jpa.supplier.EmSupplier;
import org.apache.aries.jpa.support.impl.EMSupplierImpl;
import org.apache.aries.jpa.support.impl.ResourceLocalJpaTemplate;
import org.apache.aries.jpa.template.JpaTemplate;
import org.junit.Assert;
import org.junit.Test;

public class TaskServiceImplTest {

    @Test
    public void testWriteRead() throws Exception {
        JpaTemplate jpaTemplate = createJpaTemplate();
        TaskServiceImpl taskService = new TaskServiceImpl();
        taskService.setJpa(jpaTemplate);

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test task");
        taskService.addTask(task);
        Collection<Task> persons = taskService.getTasks();

        Assert.assertEquals(1, persons.size());
        Task task1 = persons.iterator().next();
        Assert.assertEquals(new Integer(1), task1.getId());
        Assert.assertEquals("Test task", task1.getTitle());
    }

    private ResourceLocalJpaTemplate createJpaTemplate() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tasklist", System.getProperties());
        EmSupplier emSupplier = new EMSupplierImpl(emf);
        return new ResourceLocalJpaTemplate(emSupplier);
    }

}
