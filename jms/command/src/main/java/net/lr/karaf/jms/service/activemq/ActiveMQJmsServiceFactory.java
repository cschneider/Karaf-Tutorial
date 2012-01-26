package net.lr.karaf.jms.service.activemq;

import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.pool.PooledConnection;

import net.lr.karaf.jms.service.ExtJmsService;
import net.lr.karaf.jms.service.ExtJmsServiceFactory;

public class ActiveMQJmsServiceFactory implements ExtJmsServiceFactory {

    @Override
    public ExtJmsService create(final Connection connection) {
        return new ActiveMQJmsService(connection);
    }

    @Override
    public boolean canHandle(Connection connection) {
        return (connection instanceof PooledConnection) || (connection instanceof ActiveMQConnection);
    }

}
