package net.lr.tutorial.karaf.db.examplejpa.impl;

import java.util.List;

import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.lr.tutorial.karaf.db.examplejpa.Person;
import net.lr.tutorial.karaf.db.examplejpa.PersonService;

@Component
public class PersonServiceImpl implements PersonService {
    private JpaTemplate jpa;

    @Reference(target = "(osgi.unit.name=person)")
    public void setJpa(JpaTemplate jpa) {
        this.jpa = jpa;
    }

    @Override
    public void add(Person person) {
        jpa.tx(em -> {
            em.persist(person);
            em.flush();
        });
    }

    public void deleteAll() {
        jpa.tx(em -> {
            em.createQuery("delete from Person").executeUpdate();
            em.flush();
        });
    }

    @Override
    public List<Person> getAll() {
        return jpa.txExpr(em -> em.createQuery("select p from Person p", Person.class).getResultList());
    }

}
