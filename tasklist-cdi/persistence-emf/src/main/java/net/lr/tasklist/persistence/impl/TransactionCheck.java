package net.lr.tasklist.persistence.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

import net.lr.tasklist.model.Task;

import org.ops4j.pax.cdi.api.OsgiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TransactionCheck {
    Logger LOG = LoggerFactory.getLogger(TransactionCheck.class);
    //&((osgi.unit.name=tasklist)
    @Inject @OsgiService(filter="(!(org.apache.aries.jpa.proxy.factory=*))")
    EntityManagerFactory emf;
    
    @Inject @OsgiService
    UserTransaction tm;
    
    @PostConstruct
    public void addDemoTasks() {
        deleteAll();
        Runnable command = new Runnable() {
            @Override
            public void run() {
                taskWatcher();
            }
        };
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        newSingleThreadExecutor.execute(command);
        sleep(2000);
        try {
            tm.begin();
            EntityManager em = emf.createEntityManager();
            em.joinTransaction();
            LOG.info("Adding sample task");
            em.persist(createTask());
            //em.flush();
            LOG.info("Number of tasks is now: " + getTasks(em).size());
            sleep(8000);
            tm.commit();
            LOG.info("Committed " + getTasks(em).size());
            em.close();
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void deleteAll() {
        EntityManager em = emf.createEntityManager();
        try {
            tm.begin();
            em.joinTransaction();
            List<Task> tasks = getTasks(em);
            for (Task task : tasks) {
                em.remove(task);
            }
            tm.commit();
            LOG.info("Number of tasks is now: " + getTasks(em).size());
        }
        catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    private void taskWatcher() {
        EntityManager em = emf.createEntityManager();
        Thread.currentThread().setName("taskWatcher");
        for (int c=0; c<20;c++) {
            LOG.info("Number in check " + getTasks(em).size());
            sleep(1000);
        }
        em.close();
    }

    private List<Task> getTasks(EntityManager em) {
        return em.createQuery("select t from Task t", Task.class).getResultList();
    }

    private Task createTask() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Just a sample task");
        task1.setDescription("Some more info");
        return task1;
    }
}
