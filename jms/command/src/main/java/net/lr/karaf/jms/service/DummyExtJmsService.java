package net.lr.karaf.jms.service;

import java.util.List;

public class DummyExtJmsService implements ExtJmsService {

    private RuntimeException getNotSupportedException() throws RuntimeException {
        return new RuntimeException("No extended jms service available for this jms provider so this method is not supported");
    }

    @Override
    public void deleteQueue(String queueName) {
        getNotSupportedException();
    }

    @Override
    public List<String> listTopics() {
        throw getNotSupportedException();
    }

    @Override
    public List<String> listQueues() {
        throw getNotSupportedException();
    }

    @Override
    public QueueInfo getQueueInfo(String queueName) {
        throw getNotSupportedException();
    }
    
}
