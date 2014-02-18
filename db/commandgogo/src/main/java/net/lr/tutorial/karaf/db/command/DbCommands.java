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
package net.lr.tutorial.karaf.db.command;

import java.util.List;

import javax.sql.DataSource;

import net.lr.tutorial.karaf.db.service.DbAccess;
import net.lr.tutorial.karaf.db.service.DbInfo;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@Component
@Service({DbCommands.class})
@Properties({
    @Property(name = "osgi.command.scope", value = "db"),
    @Property(name = "osgi.command.function", value = {"select", "sources", "execute", "query", "tables"})
})
public class DbCommands {
    @Reference
    private DbAccess dbAccess;
    
    BundleContext context;

    private DataSource dataSource;
    
    @Activate
    public void setContext(BundleContext context) {
        this.context = context;
    }

    private String getJndiName(ServiceReference ref) {
        return (String)ref.getProperty("osgi.jndi.service.name");
    }
    
    @Descriptor("show data sources")
    public void sources() {
        getDataSourcesTable().print();
    }
    
    @Descriptor("select data source")
    public void select(@Descriptor("DataSource JNDI name") String name) {
        ServiceReference[] dataSources = dbAccess.getDataSources();
        for (ServiceReference ref : dataSources) {
            String jndiName = getJndiName(ref);
            if (name.equals(jndiName)) {
                this.dataSource = (DataSource)context.getService(ref);
                return;
            }
        }
        throw new RuntimeException("No DataSource with name " + name + " found");
    }
    
    @Descriptor("Executes a SQL query and displays the result")
    public void query(@Descriptor("SQL Query") String sql) throws Exception {
        dbAccess.query(this.dataSource, sql).print();
    }
    
    @Descriptor("Shows all tables in this db")
    public void tables() throws Exception {
        ShellTable table = dbAccess.getTableInfo(this.dataSource);
        table.print();
    }
    
    @Descriptor("Executes a SQL command")
    public void execute(@Descriptor("SQL command") String sql) throws Exception {
        dbAccess.exec(this.dataSource, sql);
    }

    public ShellTable getDataSourcesTable() {
        ServiceReference[] dataSources = dbAccess.getDataSources();
        ShellTable table = new ShellTable();
        table.header.add("Sel");
        table.header.add("Name");
        table.header.add("Product");
        table.header.add("Version");
        table.header.add("URL");
        for (ServiceReference ref : dataSources) {
            List<String> row = table.addRow();
            DbInfo info = dbAccess.getDataSourceInfo(ref);
            row.add(info.selected ? "*" : "");
            row.add(info.jndiName);
            row.add(info.product);
            row.add(info.version);
            row.add(info.url);
        }
        return table;
    }
}
