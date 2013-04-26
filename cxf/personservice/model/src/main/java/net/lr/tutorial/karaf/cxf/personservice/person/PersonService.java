package net.lr.tutorial.karaf.cxf.personservice.person;

import javax.jws.WebService;

@WebService
public interface PersonService {
    public Person[] getAll();
    public Person getPerson(String id);
    public void updatePerson(String id, Person person);
    public void addPerson(Person person);
}
