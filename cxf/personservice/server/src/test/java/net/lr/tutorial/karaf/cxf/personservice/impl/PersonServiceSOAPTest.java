package net.lr.tutorial.karaf.cxf.personservice.impl;

import net.lr.tutorial.karaf.cxf.personservice.model.Person;
import net.lr.tutorial.karaf.cxf.personservice.model.PersonService;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonServiceSOAPTest {
    
    private static final String PERSONSERVICE_TESTURL = "http://localhost:8282/personService";
    private static Server server;

    @BeforeClass
    public static void startServer() {
        PersonService personService = new PersonServiceImpl();
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setAddress(PERSONSERVICE_TESTURL);
        factory.setServiceBean(personService);
        server = factory.create();
        server.start();
    }
    
    @Test
    public void testPutPerson() {
        PersonService personService = getService();
        personService.addPerson(createPerson());
        Person person2 = personService.getPerson("1001");
        assertCorrectPerson(person2);
    }
    
    @AfterClass
    public static void stopServer() {
        server.stop();
    }

    private PersonService getService() {
        JaxWsProxyFactoryBean proxy = new JaxWsProxyFactoryBean();
        proxy.setServiceClass(PersonService.class);
        proxy.setAddress(PERSONSERVICE_TESTURL);
        PersonService personService = (PersonService)proxy.create();
        return personService;
    }

    private Person createPerson() {
        Person person = new Person();
        person.setId("1001");
        person.setName("Christian Schneider");
        return person;
    }

    private void assertCorrectPerson(Person person) {
        Assert.assertNotNull(person);
        Assert.assertEquals("Christian Schneider", person.getName());
    }
    
}
