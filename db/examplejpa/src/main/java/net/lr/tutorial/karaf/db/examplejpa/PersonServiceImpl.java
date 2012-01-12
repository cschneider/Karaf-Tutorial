package net.lr.tutorial.karaf.db.examplejpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public class PersonServiceImpl implements PersonService {
    private EntityManager em;
    
    public void setEntityManager (EntityManager em) { 
        this.em = em;
    }

    @Override
    public void addPersion(Person person) {
        em.persist(person);
    }

    @Override
    public List<Person> getAllPersons() {
        CriteriaQuery<Person> query = em.getCriteriaBuilder().createQuery(Person.class);
        return em.createQuery(query).getResultList();
    }

}
