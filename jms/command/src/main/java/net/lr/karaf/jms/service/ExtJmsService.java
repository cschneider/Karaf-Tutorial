package net.lr.karaf.jms.service;

import java.util.List;

public interface ExtJmsService {
    void deleteQueue(final String queueName);

    List<String> listTopics();

    List<String> listQueues();

    QueueInfo getQueueInfo(final String queueName); 
}
