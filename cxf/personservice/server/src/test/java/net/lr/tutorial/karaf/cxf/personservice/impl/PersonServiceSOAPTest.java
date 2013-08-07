package net.lr.tutorial.karaf.cxf.personservice.impl;

import net.lr.tutorial.karaf.cxf.personservice.person.Person;
import net.lr.tutorial.karaf.cxf.personservice.person.PersonService;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Assert;
import org.junit.Test;

public class PersonServiceSOAPTest {
    
    private static final String PERSONSERVICE_TESTURL = "http://localhost:8282/personService";


    @Test
    public void testPutPerson() {
        PersonService personService = new PersonServiceImpl();;
        Server server = startPersonService(personService);
        
        JaxWsProxyFactoryBean proxy = new JaxWsProxyFactoryBean();
        proxy.setServiceClass(PersonService.class);
        proxy.setAddress(PERSONSERVICE_TESTURL);
        PersonService personServiceProxy = (PersonService)proxy.create();
        
        personServiceProxy.addPerson(createPerson());

        // Get from impl directly
        Person person = personService.getPerson("1001");
        assertCorrectPerson(person);

        // Get through proxy
        Person person2 = personServiceProxy.getPerson("1001");
        assertCorrectPerson(person2);

        server.stop();
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

    
    private Server startPersonService(PersonService personService) {
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setAddress(PERSONSERVICE_TESTURL);
        factory.setServiceBean(personService);
        Server server = factory.create();
        server.start();
        return server;
    }

}
