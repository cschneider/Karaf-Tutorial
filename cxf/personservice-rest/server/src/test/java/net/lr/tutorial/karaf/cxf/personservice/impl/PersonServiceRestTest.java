package net.lr.tutorial.karaf.cxf.personservice.impl;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import net.lr.tutorial.karaf.cxf.personrest.impl.PersonServiceImpl;
import net.lr.tutorial.karaf.cxf.personrest.model.Person;
import net.lr.tutorial.karaf.cxf.personrest.model.PersonService;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonServiceRestTest {
    
    private static final String PERSONSERVICE_TESTURL = "http://localhost:8282/person";
    private static Server server;

    @BeforeClass
    public static void startServer() {
        PersonService personService = new PersonServiceImpl();;
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress(PERSONSERVICE_TESTURL);
        factory.setServiceBean(personService);
        server = factory.create();
        server.start();
    }

    @Test
    public void testInterface() {
        PersonService personService = JAXRSClientFactory.create(PERSONSERVICE_TESTURL, PersonService.class);
        Person person = new Person();
        person.setId("1002");
        person.setName("Christian Schneider");
        personService.updatePerson("1002", person);
        
        Person person2 = personService.getPerson("1002");
        assertCorrectPerson(person2);
    }
    
    @Test
    public void testWebClient() {
        WebClient client = WebClient.create(PERSONSERVICE_TESTURL + "/1001");
        putPerson(client);
        Person person = client.get(Person.class);
        assertCorrectPerson(person);
    }

    private void putPerson(WebClient client) {
        InputStream is = this.getClass().getResourceAsStream("/person1.xml");
        Response resp = client.put(is);
        System.out.println(resp);
    }
    
    @AfterClass
    public static void stopServer() {
        server.stop();
    }


    private void assertCorrectPerson(Person person) {
        Assert.assertNotNull(person);
        Assert.assertEquals("Christian Schneider", person.getName());
    }

}
