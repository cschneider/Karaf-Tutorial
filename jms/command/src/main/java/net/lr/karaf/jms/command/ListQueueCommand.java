package net.lr.karaf.jms.command;

import java.util.List;

import net.lr.karaf.jms.service.JmsService;
import net.lr.karaf.jms.service.QueueInfo;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

@Command(scope = "jms", name = "list-queue", description = "List queues")
public class ListQueueCommand implements Action {
    private JmsService jmsService;
    
    public void setJmsService(JmsService jmsService) {
        this.jmsService = jmsService;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        ShellTable table = new ShellTable();
        table.header.add("Name");
        table.header.add("Messages");
        List<String> queues = this.jmsService.listQueues();
        for (String name : queues) {
            List<String> row = table.addRow();
            QueueInfo qi = jmsService.getQueueInfo(name);
            row.add(name);
            row.add("" + qi.getNumPendingMessages());
        }
        table.print();
        return null;
    }



}
