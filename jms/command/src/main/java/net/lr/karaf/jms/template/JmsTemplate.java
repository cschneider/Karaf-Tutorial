package net.lr.karaf.jms.template;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;


public class JmsTemplate {
    private Connection connection;
    private boolean transacted = false;
    private int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
    
    public JmsTemplate() {
    }
    
    public JmsTemplate(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public <T> T doInSessionNoClose(SessionExecutor<T> executor) {
        assertConnection();
        Session session = null;
        try {
             session = this.connection.createSession(this.transacted, this.acknowledgeMode);
             return executor.execute(session);
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    private void assertConnection() {
        if (connection == null) {
            throw new RuntimeException("No JMS connection was set");
        }
    }

    public <T> T doInSession(SessionExecutor<T> executor) {
        Session session = null;
        try {
             session = this.connection.createSession(this.transacted, this.acknowledgeMode);
             return executor.execute(session);
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }
    
    public void doInSession(VoidSessionExecutor executor) {
        Session session = null;
        try {
             session = this.connection.createSession(this.transacted, this.acknowledgeMode);
             executor.execute(session);
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }
    
    public void send(Session session, Destination dest, Message message) throws JMSException {
        MessageProducer producer = session.createProducer(dest);
        try {
            producer.send(message);
            producer.close();
        } finally {
            producer.close();
        }
    }


}
