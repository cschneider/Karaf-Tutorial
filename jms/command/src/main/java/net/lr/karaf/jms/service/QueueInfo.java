package net.lr.karaf.jms.service;

public class QueueInfo {
    private String name;
    private int numPendingMessages;
    private int numConsumers;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNumPendingMessages() {
        return numPendingMessages;
    }
    public void setNumPendingMessages(int numPendingMessages) {
        this.numPendingMessages = numPendingMessages;
    }
    public int getNumConsumers() {
        return numConsumers;
    }
    public void setNumConsumers(int numConsumers) {
        this.numConsumers = numConsumers;
    }
    
}
