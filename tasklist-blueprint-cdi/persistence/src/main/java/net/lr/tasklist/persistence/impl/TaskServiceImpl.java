package net.lr.tasklist.persistence.impl;

import java.util.Collection;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

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
public class TaskServiceImpl implements TaskService {
    
    @PersistenceContext(unitName="tasklist")
    EntityManager em;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Task getTask(Integer id) {
        return em.find(Task.class, id);
    }

    @Override
    @Transactional
    public void addTask(Task task) {
        em.persist(task);
        em.flush();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Collection<Task> getTasks() {
        CriteriaQuery<Task> query = em.getCriteriaBuilder().createQuery(Task.class);
        return em.createQuery(query.select(query.from(Task.class))).getResultList();
    }

    @Transactional
    @Override
    public void updateTask(Task task) {
        em.merge(task);
    }
    
    @Transactional
    @Override
    public void deleteTask(Integer id) {
        em.remove(getTask(id));
    }

}
