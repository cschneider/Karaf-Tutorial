/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.lr.karaf.jms.command;

import java.util.List;

import net.lr.karaf.jms.service.JmsInfo;
import net.lr.karaf.jms.service.JmsSelect;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;
import org.osgi.framework.ServiceReference;

@Command(scope = "jms", name = "select", description = "Selects a ConnectionFactory")
public class SelectCommand implements Action {
    @Argument(index=0, name="name", required=false, description="ConnectionFactory name", multiValued=false)
    String name;

    private JmsSelect jmsSelect;

    public void setJmsSelect(JmsSelect jmsSelect) {
        this.jmsSelect = jmsSelect;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        if (name == null) {
            ShellTable table = getDataSourcesTable();
            table.print();
        } else {
            jmsSelect.selectDataSource(name);
        }
        return null;
    }

    public ShellTable getDataSourcesTable() {
        ServiceReference[] dataSources = jmsSelect.getConnectionFactories();
        ShellTable table = new ShellTable();
        table.header.add("Sel");
        table.header.add("Name");
        table.header.add("Product");
        table.header.add("Version");
        table.header.add("URL");
        for (ServiceReference ref : dataSources) {
            List<String> row = table.addRow();
            JmsInfo info = jmsSelect.getConnectionFactoryInfo(ref);
            row.add(info.selected ? "*" : "");
            row.add(info.name);
            row.add(info.product);
            row.add(info.version);
            row.add(info.url);
        }
        return table;
    }
}
