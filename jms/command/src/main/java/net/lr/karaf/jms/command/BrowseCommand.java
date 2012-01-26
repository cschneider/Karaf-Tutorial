package net.lr.karaf.jms.command;

import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;

import net.lr.karaf.jms.service.JmsMessage;
import net.lr.karaf.jms.service.JmsService;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.service.command.CommandSession;

@Command(scope = "jms", name = "browse", description = "Browse a queue")
public class BrowseCommand implements Action {
    @Argument(index = 0, name = "queueName", required = false, description = "queue name", multiValued = false)
    String queueName;
    
    @Option(name = "-s", required = false, description = "JMS selector to filter the view", multiValued = false)
    String selector;
    
    @Resource
    private JmsService jmsService;
    
    @Override
    public Object execute(CommandSession session) throws Exception {
        ShellTable table = new ShellTable();
        table.maxColSize = 70;
        table.header.add("ID");
        table.header.add("CorrelationID");
        table.header.add("ReplyTo");
        table.header.add("Redelivered");
        table.header.add("body");
        Enumeration<JmsMessage> messages = this.jmsService.browseQueue(this.queueName, this.selector);
        while (messages.hasMoreElements()) {
            List<String> row = table.addRow();
            JmsMessage jmsMessage = messages.nextElement();
            row.add(jmsMessage.getMessageId());
            row.add(jmsMessage.getCorrelationID());
            row.add(jmsMessage.getReplyTo());
            row.add(jmsMessage.isRedelivered() ? "x" : "");
            row.add(jmsMessage.getContent());
        }
        table.print();
        return null;
    }

}
