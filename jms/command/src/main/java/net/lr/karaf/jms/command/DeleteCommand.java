package net.lr.karaf.jms.command;

import net.lr.karaf.jms.service.JmsService;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.service.command.CommandSession;

@Command(scope = "jms", name = "delete", description = "delete one or more messages from a queue")
public class DeleteCommand implements Action {
    @Argument(index = 0, name = "queueName", required = true, description = "queue name", multiValued = false)
    String queueName;
    
    @Argument(index = 1, name = "messageId", required = false, description = "Message ID", multiValued = false)
    String messageId;
    
    @Option(name = "-s", description = "JMS Selector for the messages to delete")
    String selector;
    
    @Option(name = "-a", description = "Delete all messages")
    boolean all;
    
    private JmsService jmsService;

    @Override
    public Object execute(CommandSession session) throws Exception {
        if (all) {
            selector = "";
        } else if (messageId != null) {
            selector = String.format("JMSMessageID = '%s'", messageId);
        } else if (selector == null){
            throw new RuntimeException("You have to use either -all, -sel or a message id");
        }
        int numDeleted = jmsService.deleteMessages(queueName, selector);
        System.out.println(numDeleted + " messages deleted from queue " + queueName + " using selector " + selector);
        return null;
    }

    public void setJmsService(JmsService jmsService) {
        this.jmsService = jmsService;
    }
}
