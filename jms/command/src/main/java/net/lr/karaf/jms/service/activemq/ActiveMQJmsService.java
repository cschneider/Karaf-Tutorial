package net.lr.karaf.jms.service.activemq;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import net.lr.karaf.jms.service.ExtJmsService;
import net.lr.karaf.jms.service.QueueInfo;
import net.lr.karaf.jms.template.JmsTemplate;
import net.lr.karaf.jms.template.SessionExecutor;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnection;

public class ActiveMQJmsService implements ExtJmsService {

    private ActiveMQConnection connection;

    public ActiveMQJmsService(Connection connection) {
        this.connection = getActiveMQConnection(connection);
    }

    @Override
    public List<String> listQueues() {
        try {
            DestinationSource destSource = connection.getDestinationSource();
            Set<ActiveMQQueue> queues = destSource.getQueues();
            List<String> names = new ArrayList<String>();
            for (ActiveMQQueue queue : queues) {
                names.add(queue.getQueueName());
            }
            return names;
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> listTopics() {
        try {
            DestinationSource destSource = connection.getDestinationSource();
            Set<ActiveMQTopic> topics = destSource.getTopics();
            List<String> names = new ArrayList<String>();
            for (ActiveMQTopic queue : topics) {
                names.add(queue.getTopicName());
            }
            return names;
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ActiveMQConnection getActiveMQConnection(final Connection connection) {
        Connection physConnection = connection;
        if (connection instanceof PooledConnection) {
            try {
                physConnection = ((PooledConnection)connection).getConnection();
            } catch (JMSException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        ActiveMQConnection aConnection = (ActiveMQConnection)physConnection;
        return aConnection;
    }

    @Override
    public void deleteQueue(final String queueName) {
    }

    private Integer getMessagesInQueue(final String queueName) {
        return new JmsTemplate(this.connection).doInSession(new SessionExecutor<Integer>() {
            public Integer execute(Session session) throws JMSException {
                QueueBrowser browser = session.createBrowser(session.createQueue(queueName));
                @SuppressWarnings("unchecked")
                Enumeration<Message> en = browser.getEnumeration();
                int count = 0;
                while (en.hasMoreElements()) {
                    en.nextElement();
                    count ++;
                }
                return count;
            }
        });
    }

    @Override
    public QueueInfo getQueueInfo(String queueName) {
        QueueInfo qi = new QueueInfo();
        qi.setName(queueName);
        qi.setNumPendingMessages(getMessagesInQueue(queueName));
        return qi;
    }

}
