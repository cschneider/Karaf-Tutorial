package net.lr.karaf.jms.template;

import javax.jms.JMSException;
import javax.jms.Session;

public interface SessionExecutor<T> {
    T execute(Session session) throws JMSException;
}
