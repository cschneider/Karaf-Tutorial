package net.lr.tutorial.karaf.cxf.personservice.person;

import javax.jws.WebService;

@WebService
public interface PersonService {

    public abstract Person[] getAll();

    public abstract Person getPerson(String id);

    public abstract void updatePerson(String id, Person person);

    public abstract void addPerson(Person person);

}
