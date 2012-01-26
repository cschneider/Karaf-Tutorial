package net.lr.karaf.jms.service;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class JmsMessage {
    private Map<String, Object> headers = new HashMap<String, Object>();
    private Map<String, Object> properties = new HashMap<String, Object>();

    private String content;
    private String charset = "UTF-8";
    private String correlationID;
    private int deliveryMode;
    private String destination;
    private long expiration;
    private String messageId;
    private int priority;
    private boolean redelivered;
    private String replyTo;
    private long timestamp;
    private String type;

    public JmsMessage(Message message) {
        try {
            initFromMessage(message);
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public void initFromMessage(Message message) throws JMSException {
        @SuppressWarnings("unchecked")
        Enumeration<String> names = message.getPropertyNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            Object value = message.getObjectProperty(key);
            properties.put(key, value);
        }
        
        correlationID = message.getJMSCorrelationID();
        deliveryMode = message.getJMSDeliveryMode();
        Destination destinationDest = message.getJMSDestination();
        if (destinationDest != null) {
            destination = destinationDest.toString();
        }
        expiration = message.getJMSExpiration();
        messageId = message.getJMSMessageID();
        priority = message.getJMSPriority();
        redelivered = message.getJMSRedelivered();
        Destination replyToDest = message.getJMSReplyTo();
        if (replyToDest != null) {
            replyTo = replyToDest.toString();
        }
        timestamp = message.getJMSTimestamp();
        type = message.getJMSType();
        content = getMessageContent(message);
    }
    

    private String getMessageContent(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            return ((TextMessage)message).getText();
        } else if (message instanceof BytesMessage) {
            BytesMessage bMessage = (BytesMessage)message;
            long length = bMessage.getBodyLength();
            byte[] content = new byte[(int)length]; 
            bMessage.readBytes(content);
            try {
                return new String(content, charset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return "";
    }
    
    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getContent() {
        return content;
    }

    public String getCharset() {
        return charset;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public String getDestination() {
        return destination;
    }

    public long getExpiration() {
        return expiration;
    }

    public String getMessageId() {
        return messageId;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isRedelivered() {
        return redelivered;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }
}
