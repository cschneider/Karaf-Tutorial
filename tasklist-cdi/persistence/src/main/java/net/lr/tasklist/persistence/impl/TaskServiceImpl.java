package net.lr.tasklist.persistence.impl;

import java.util.Collection;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.transaction.cdi.Transactional;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;

@OsgiServiceProvider(classes = {TaskService.class})
// The properties below allow to transparently export the service as a web service using Distributed OSGi
@Properties({
    @Property(name = "service.exported.interfaces", value = "*")
})
@Singleton
@Transactional
public class TaskServiceImpl implements TaskService {
    
    @PersistenceUnit(unitName="tasklist")
    EntityManager em;

    @Override
    public Task getTask(Integer id) {
        return em.find(Task.class, id);
    }

    @Override
    public void addTask(Task task) {
        em.persist(task);
        em.flush();
    }

    public Collection<Task> getTasks() {
        return em.createQuery("select t from Task t", Task.class).getResultList();
    }

    @Override
    public void updateTask(Task task) {
        em.persist(task);
    }
    
    @Override
    public void deleteTask(Integer id) {
        em.remove(getTask(id));
    }

}
