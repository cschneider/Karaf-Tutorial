package net.lr.karaf.jms.service;
import java.util.Enumeration;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;

import net.lr.karaf.jms.service.activemq.ActiveMQJmsServiceFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Assert;
import org.junit.Test;


public class JmsServiceTest extends Assert {

    private static final String TEST_QUEUE_NAME = "test";
    
    private Connection createAndStartConnection() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://test?broker.persistent=false");
        Connection con = cf.createConnection();
        con.start();
        return con;
    }

    @Test
    public void testBrowse() throws Exception {
        Connection con = createAndStartConnection();
        JmsService jmsService = new JmsService();
        jmsService.setExtJmsServiceFactory(new ActiveMQJmsServiceFactory());
        jmsService.setConnection(con);
        jmsService.send(TEST_QUEUE_NAME, "testContent", "reply");
        Enumeration<JmsMessage> messages = jmsService.browseQueue(TEST_QUEUE_NAME, null);

        JmsMessage message = messages.nextElement();
        assertEquals(0, message.getExpiration());
        
        assertNull(message.getCorrelationID());
        assertEquals(Message.DEFAULT_DELIVERY_MODE, message.getDeliveryMode());
        assertEquals("queue://" + TEST_QUEUE_NAME, message.getDestination());
        assertNotNull(message.getMessageId());
        assertEquals(Message.DEFAULT_PRIORITY, message.getPriority());
        assertEquals("queue://reply", message.getReplyTo());
        assertNotNull(message.getTimestamp());
        assertEquals(null, message.getType());
        assertEquals("testContent", message.getContent());
        assertFalse(messages.hasMoreElements());
        
        con.close();
    }
    
    @Test
    public void testListQueues() throws Exception {
        Connection con = createAndStartConnection();
        JmsService jmsService = new JmsService();
        jmsService.setExtJmsServiceFactory(new ActiveMQJmsServiceFactory());
        jmsService.setConnection(con);
        jmsService.send(TEST_QUEUE_NAME, "testContent", null);
        List<String> listQueues = jmsService.listQueues();
        listQueues.contains(TEST_QUEUE_NAME);
        con.close();
    }

}
