package net.lr.tutorial.karaf.db.examplejpa;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.lr.tutorial.karaf.db.examplejpa.impl.PersonServiceImpl;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Assert;
import org.junit.Test;

public class PersonServiceImplTest {

    public void testWriteRead() throws Exception {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "net.lr.tutorial.karaf.db.examplejpa.MyInitialContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");            
        InitialContext ic = new InitialContext();
        ic.createSubcontext("jdbc");
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ic.bind("jdbc/derbyds", ds);
        PersonServiceImpl personService = new PersonServiceImpl();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("person", System.getProperties());
        EntityManager em = emf.createEntityManager();
        personService.setEntityManager(em);
        
        personService.addPersion(new Person("Christian Schneider", "@schneider_chris"));
        List<Person> persons = personService.getAllPersons();
        Assert.assertEquals(1, persons.size());
        Assert.assertEquals("Christian Schneider", persons.get(0).getName());
        Assert.assertEquals("@schneider_chris", persons.get(0).getTwitterName());
    }

}
