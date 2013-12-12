package net.lr.tasklist.persistence.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

@OsgiServiceProvider
@Properties({
    @Property(name = "service.exported.interfaces", value = "*")
})
public class TaskServiceImpl implements TaskService {
    /*
    @Inject
    EntityManager em;
    */
    
    @Inject @OsgiService
    EntityManagerFactory emf;

    public TaskServiceImpl() {
    }
    
    @PostConstruct
    public void addDemoTasks() {
        System.out.println(getTasks().size());
        Task task1 = new Task();
        task1.setTitle("Just a sample task");
        addTask(task1);
        System.out.println(getTasks().size());
    }

    @Override
    public Task getTask(String id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Task.class, id);
    }

    @Override
    @Transactional
    public void addTask(Task task) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(task);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    @Override
    @Transactional
    public Collection<Task> getTasks() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Task> query = em.createQuery("select t from Task t", Task.class);
        List<Task> list = query.getResultList();
        em.close();
        return list;
    }

    @Override
    public void updateTask(Task task) {
        EntityManager em = emf.createEntityManager();
        em.persist(task);
    }
    
    @Override
    public void deleteTask(String id) {
        EntityManager em = emf.createEntityManager();
        Task task = getTask(id);
        em.remove(task);
    }

}
