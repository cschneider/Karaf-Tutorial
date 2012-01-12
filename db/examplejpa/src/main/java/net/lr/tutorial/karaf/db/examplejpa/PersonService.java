package net.lr.tutorial.karaf.db.examplejpa;

import java.util.List;

public interface PersonService {
    void addPersion(Person person);
    List<Person> getAllPersons();
}
