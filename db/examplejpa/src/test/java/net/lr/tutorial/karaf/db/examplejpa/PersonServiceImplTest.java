package net.lr.tutorial.karaf.db.examplejpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.lr.tutorial.karaf.db.examplejpa.impl.PersonServiceImpl;

import org.junit.Assert;
import org.junit.Test;

/**
 * To test a persistence class we need to create and inject the EntityManager and
 * we need to manage the transaction by hand.
 */
public class PersonServiceImplTest {

    @Test
    public void testWriteRead() throws Exception {
        PersonServiceImpl personService = new PersonServiceImpl();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("person", System.getProperties());
        EntityManager em = emf.createEntityManager();
        personService.setEntityManager(em);
        
        em.getTransaction().begin();
        personService.deleteAll();
        personService.add(new Person("Christian Schneider", "@schneider_chris"));
        em.getTransaction().commit();

        List<Person> persons = personService.getAll();
        Assert.assertEquals(1, persons.size());
        Assert.assertEquals("Christian Schneider", persons.get(0).getName());
        Assert.assertEquals("@schneider_chris", persons.get(0).getTwitterName());
    }

}
