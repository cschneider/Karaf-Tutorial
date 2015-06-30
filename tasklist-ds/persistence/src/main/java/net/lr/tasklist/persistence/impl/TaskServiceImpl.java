package net.lr.tasklist.persistence.impl;

import java.util.Collection;

import javax.persistence.criteria.CriteriaQuery;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		// Allows to export this service using distributed OSGi
		property="osgi.remote.interfaces:String=*"
)
public class TaskServiceImpl implements TaskService {

    private JpaTemplate jpa;

    @Override
    public Task getTask(Integer id) {
        return jpa.txExpr(em -> em.find(Task.class, id));
    }

    @Override
    public void addTask(Task task) {
        jpa.tx(em -> {
            em.persist(task);
            em.flush();
        });
    }

    public Collection<Task> getTasks() {
        return jpa.txExpr(em -> {
            CriteriaQuery<Task> query = em.getCriteriaBuilder().createQuery(Task.class);
            return em.createQuery(query.select(query.from(Task.class))).getResultList();
        });
    }

    @Override
    public void updateTask(Task task) {
        jpa.tx(em -> em.merge(task));
    }

    @Override
    public void deleteTask(Integer id) {
        jpa.tx(em -> em.remove(getTask(id)));
    }
    
    @Reference(target = "(osgi.unit.name=tasklist)")
    public void setJpa(JpaTemplate jpa) {
        this.jpa = jpa;
    }

}
