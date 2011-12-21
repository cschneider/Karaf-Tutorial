package net.lr.tutorial.karaf.camel.jms2rest;

import java.io.InputStream;

import net.lr.tutorial.karaf.camel.jms2rest.person.Person;
import net.lr.tutorial.karaf.camel.jms2rest.person.PersonService;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.junit.Assert;
import org.junit.Test;

public class PersonServiceTest {
    
    private static final String PERSONSERVICE_TESTURL = "http://localhost:8282/person";


    @Test
    public void testPutPerson() {
        PersonService personService = new PersonService();;
        Server server = startPersonService(personService);
        WebClient client = WebClient.create(PERSONSERVICE_TESTURL + "/1001");

        InputStream is = this.getClass().getResourceAsStream("/person1.xml");
        client.put(is);

        Person person = personService.getPerson("1001");
        assertCorrectPerson(person);

        Person person2 = client.get(Person.class);
        assertCorrectPerson(person2);

        server.stop();
    }


    private void assertCorrectPerson(Person person) {
        Assert.assertNotNull(person);
        Assert.assertEquals("Christian Schneider", person.getName());
    }

    
    private Server startPersonService(PersonService personService) {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress(PERSONSERVICE_TESTURL);
        factory.setResourceClasses(PersonService.class);
        factory.setResourceProvider(PersonService.class, new SingletonResourceProvider(personService));
        Server server = factory.create();
        server.start();
        return server;
    }

}
