package net.lr.karaf.jms.service;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class JmsMessageIterator implements Enumeration<JmsMessage> {
    Session session;
    private Enumeration<Message> messageEnum;
    
    public JmsMessageIterator(Session session, Enumeration<Message> messageEnum) {
        this.session = session;
        this.messageEnum = messageEnum;
    }

    @Override
    public boolean hasMoreElements() {
        boolean more = this.messageEnum.hasMoreElements();
        if (!more) {
            try {
                session.close();
            } catch (JMSException e) {
                // Ignore
            }
        }
        return more;
    }

    @Override
    public JmsMessage nextElement() {
        Message message = this.messageEnum.nextElement();
        return new JmsMessage(message);
    }

}
