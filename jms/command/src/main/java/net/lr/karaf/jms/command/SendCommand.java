package net.lr.karaf.jms.command;

import net.lr.karaf.jms.service.JmsService;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.service.command.CommandSession;

@Command(scope = "jms", name = "send", description = "send a message to a queue")
public class SendCommand implements Action {
    @Argument(index = 0, name = "queueName", required = true, description = "queue name", multiValued = false)
    String queueName;
    
    @Argument(index = 1, name = "body", required = true, description = "body", multiValued = false)
    String body;
    
    @Option(name = "-r", aliases="--replyTo", description = "Destination name to send the reply to")
    String replyTo;
    
    private JmsService jmsService;
    
    public void setJmsService(JmsService jmsService) {
        this.jmsService = jmsService;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        this.jmsService.send(queueName, body, replyTo);
        return null;
    }

}
