package net.lr.tutorial.karaf.camel.jpa2jms;

import net.lr.tutorial.karaf.camel.jpa2jms.model.Person;

public class ExceptionDecider {
    public void decideException(Person person) {
        if ("error".equals(person.getName())) {
            throw new RuntimeException("Exception to check if transactions and redelivery work");
        }
    }
}
