package net.lr.tutorial.karaf.db.examplejpa;

import javax.persistence.EntityManager;

import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;

public class TestTemplate implements JpaTemplate {

    private EntityManager em;

    public TestTemplate(EntityManager em) {
        this.em = em;
    }

    @Override
    public <R> R txExpr(TransactionType type, EmFunction<R> code) {
        em.getTransaction().begin();
        try {
            R result = code.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void tx(TransactionType type, EmConsumer code) {
        txExpr(type, em -> {code.accept(em); return null;});
    }

    @Override
    public <R> R txExpr(EmFunction<R> code) {
        return txExpr(TransactionType.Required, code);
    }

    @Override
    public void tx(EmConsumer code) {
        tx(TransactionType.Required, code);
    }

}
