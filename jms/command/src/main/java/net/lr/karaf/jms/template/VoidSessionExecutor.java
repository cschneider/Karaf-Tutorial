package net.lr.karaf.jms.template;

import javax.jms.JMSException;
import javax.jms.Session;

public interface VoidSessionExecutor {
    void execute(Session session) throws JMSException;
}
