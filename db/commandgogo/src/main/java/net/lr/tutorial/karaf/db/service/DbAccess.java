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
package net.lr.tutorial.karaf.db.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import net.lr.tutorial.karaf.db.command.ShellTable;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

@Component
@Service({DbAccess.class})
public class DbAccess {
    BundleContext context;
    
    @Activate
    public void setContext(BundleContext context) {
        this.context = context;
    }

    public void exec(DataSource dataSource, String sql) throws Exception {
        if (dataSource == null) {
            throw new RuntimeException("No DataSource selected");
        }
        Connection con = dataSource.getConnection();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch (Exception e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public ShellTable getTableInfo(DataSource dataSource) throws Exception {
        if (dataSource == null) {
            throw new RuntimeException("No DataSource selected");
        }
        Connection con = dataSource.getConnection();
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet rs = metaData.getTables(null, null, null, null);
        ShellTable table = printResult(rs);
        rs.close();
        return table;
    }

    public ShellTable query(DataSource dataSource, String sql) throws Exception {
        if (dataSource == null) {
            throw new RuntimeException("No DataSource selected");
        }
        Connection con = dataSource.getConnection();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ShellTable table = printResult(rs);
            rs.close();
            return table;
        } catch (Exception e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private ShellTable printResult(ResultSet rs) throws SQLException {
        ShellTable table = new ShellTable();
        ResultSetMetaData meta = rs.getMetaData();
        for (int c = 1; c <= meta.getColumnCount(); c++) {
            table.header.add(meta.getColumnLabel(c));
        }
        while (rs.next()) {
            List<String> row = table.addRow();
            for (int c = 1; c <= meta.getColumnCount(); c++) {
                row.add(rs.getString(c));
            }
        }
        return table;
    }

    public ServiceReference[] getDataSources() {
        try {
            ServiceReference[] dsRefs = context.getServiceReferences(DataSource.class.getName(), null);
            if (dsRefs == null) {
                dsRefs = new ServiceReference[]{};
            }
            return dsRefs;
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    private String getJndiName(ServiceReference ref) {
        return (String)ref.getProperty("osgi.jndi.service.name");
    }

    public Collection<String> getDataSourceNames() {
        ServiceReference[] dataSources = getDataSources();
        List<String> dsNames = new ArrayList<String>();
        for (ServiceReference ref : dataSources) {
            dsNames.add(getJndiName(ref));
        }
        return dsNames;
    }

    public DbInfo getDataSourceInfo(ServiceReference ref) {
        DbInfo info = new DbInfo();
        info.jndiName = getJndiName(ref);
        DataSource ds = (DataSource)context.getService(ref);
        try {
            Connection con = ds.getConnection();
            DatabaseMetaData dbMeta = con.getMetaData();
            info.product = dbMeta.getDatabaseProductName();
            info.version = dbMeta.getDatabaseProductVersion();
            info.url = dbMeta.getURL();
        } catch (Exception e) {
            info.url = "Connect failed " + e.getMessage();
        }
        return info; 
    }
}
