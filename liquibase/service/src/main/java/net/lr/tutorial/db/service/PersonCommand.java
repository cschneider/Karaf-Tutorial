package net.lr.tutorial.db.service;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service=PersonCommand.class,
           property={"osgi.command.scope=person",
                     "osgi.command.function=list"})
public class PersonCommand {
    
    @Reference
    PersonRepo persons;

    public void list() {
        System.out.println("Persons");
        persons.list().stream().map(Person::toString).forEach(System.out::println);
    }
}
