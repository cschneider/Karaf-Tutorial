package net.lr.tutorial.karaf.db.examplejpa;

import java.util.List;

public interface PersonService {
    void add(Person person);
    void deleteAll();
    List<Person> getAll();
}
