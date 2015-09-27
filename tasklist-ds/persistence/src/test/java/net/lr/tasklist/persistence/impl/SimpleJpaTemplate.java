package net.lr.tasklist.persistence.impl;

import javax.persistence.EntityManager;

import org.apache.aries.jpa.support.impl.AbstractJpaTemplate;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.TransactionType;

/**
 * Simple JPA Template without any transaction support
 */
public class SimpleJpaTemplate extends AbstractJpaTemplate {
    
    private EntityManager em;

    public SimpleJpaTemplate(EntityManager em) {
        this.em = em;
    }

    @Override
    public <R> R txExpr(TransactionType type, EmFunction<R> code) {
        return code.apply(em);
    }

}
