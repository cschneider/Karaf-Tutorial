package net.lr.tutorial.karaf.db.examplejpa;

import java.util.Hashtable;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.lr.tutorial.karaf.db.examplejpa.impl.PersonServiceImpl;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Assert;
import org.junit.Test;

public class PersonServiceImplTest {

    public final class MyInitialcontextFactoryBuilder implements InitialContextFactoryBuilder {
        @Override
        public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
            return new MyInitialContextFactory();
        }
    }

    @Test
    public void testWriteRead() throws Exception {
        NamingManager.setInitialContextFactoryBuilder(new MyInitialcontextFactoryBuilder());
        InitialContext ic = new InitialContext();
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("target/test");
        ds.setCreateDatabase("create");
        ic.bind("osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/derbyds)", ds);
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
