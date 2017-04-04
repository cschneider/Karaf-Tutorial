package net.lr.tutorial.db.service;

import java.sql.SQLException;
import java.util.List;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.Test;


public class PersonRepoTest {

    @Test
    public void testList() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:person;DB_CLOSE_DELAY=-1");
        new Migrator().prepare(ds);
        PersonRepo personRepo = new PersonRepo();
        personRepo.ds = ds;
        List<Person> persons = personRepo.list();
        Person person = persons.iterator().next();
        Assert.assertEquals(1, person.id);
        Assert.assertEquals("Chris", person.name);
        //Assert.assertEquals(42, person.age);
    }

}
