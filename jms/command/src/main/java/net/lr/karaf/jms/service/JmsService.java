package net.lr.karaf.jms.service;

import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import net.lr.karaf.jms.template.JmsTemplate;
import net.lr.karaf.jms.template.SessionExecutor;
import net.lr.karaf.jms.template.VoidSessionExecutor;

@Resource
public class JmsService {
    private Connection connection;
    private JmsTemplate jmsTemplate = new JmsTemplate();
    private ExtJmsService extJmsService;
    private ExtJmsServiceFactory extJmsServiceFactory;
    private ConnectionFactory connectionFactory;

    public void setConnection(Connection connection) {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (JMSException e) {
                // Ignore
            }
        }
        this.connection = connection;
        this.jmsTemplate = new JmsTemplate();
        this.jmsTemplate.setConnection(connection);
        this.extJmsService = extJmsServiceFactory.create(this.connection);
    }
    
    public void assertConnection() {
        if (this.connection == null) {
            throw new RuntimeException("No JMS connection selected");
        }
    }

    @SuppressWarnings("unchecked")
    public Enumeration<JmsMessage> browseQueue(final String queueName, final String selector) {
        assertConnection();
        return jmsTemplate.doInSessionNoClose(new SessionExecutor<Enumeration<JmsMessage>>() {
            public Enumeration<JmsMessage> execute(Session session) throws JMSException {
                try {
                    QueueBrowser browser = session.createBrowser(session.createQueue(queueName), selector);
                    return new JmsMessageIterator(session, browser.getEnumeration());
                } catch (InvalidSelectorException e) {
                    throw new RuntimeException("Invalid selector: " + e.getMessage(), e);
                }
                
            }
        });
    }

    public void send(final String queueName, final String body, final String replyTo) {
        assertConnection();
        jmsTemplate.doInSession(new VoidSessionExecutor() {
            public void execute(Session session) throws JMSException {
                Message message = session.createTextMessage(body);
                if (replyTo != null) {
                    message.setJMSReplyTo(session.createQueue(replyTo));
                }
                jmsTemplate.send(session, session.createQueue(queueName), message);
            }
        });
    }
    
    public Integer deleteMessages(final String queueName, final String selector) {
        assertConnection();
        return jmsTemplate.doInSession(new SessionExecutor<Integer>() {
            public Integer execute(Session session) throws JMSException {
                MessageConsumer consumer = session.createConsumer(session.createQueue(queueName), selector);
                Message message;
                int count = 0;
                do {
                    message = consumer.receiveNoWait();
                    if (message != null) {
                        count ++;
                    }
                } while (message != null);
                return count;
            }
        });
    }

    public int deleteMessage(final String queueName, final String messageId) {
        assertConnection();
        String selector = String.format("JMSMessageID = '%s'", messageId);
        return deleteMessages(queueName, selector);
    }

    public List<String> listQueues() {
        assertConnection();
        return extJmsService.listQueues(); 
    }

    public List<String> listTopics() throws JMSException {
        assertConnection();
        return extJmsService.listTopics();
    }

    public void deleteQueue(final String queueName) {
        assertConnection();
    }

    public QueueInfo getQueueInfo(String queueName) {
        assertConnection();
        return extJmsService.getQueueInfo(queueName);
    }

    public void setExtJmsServiceFactory(ExtJmsServiceFactory extJmsServiceFactory) {
        this.extJmsServiceFactory = extJmsServiceFactory;
    }

    public void setConnectionFactory(ConnectionFactory cf) {
        try {
            this.connectionFactory = cf;
            Connection con = cf.createConnection();
            con.start();
            setConnection(con);
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

}
