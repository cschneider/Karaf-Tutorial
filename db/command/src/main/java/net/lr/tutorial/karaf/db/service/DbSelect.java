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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class DbSelect {
    private DbAccess dbAccess;
    private BundleContext context;

    public void setContext(BundleContext context) {
        this.context = context;
    }

    public void setDbAccess(DbAccess dbAccess) {
        this.dbAccess = dbAccess;
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

    public Collection<String> getDataSourceNames() {
        ServiceReference[] dataSources = getDataSources();
        List<String> dsNames = new ArrayList<String>();
        for (ServiceReference ref : dataSources) {
            dsNames.add(getJndiName(ref));
        }
        return dsNames;
    }

    private String getJndiName(ServiceReference ref) {
        String jndiName = (String)ref.getProperty("osgi.jndi.service.name");
        return jndiName;
    }

    public void selectDataSource(String name) {
        ServiceReference[] dataSources = getDataSources();
        for (ServiceReference ref : dataSources) {
            String jndiName = getJndiName(ref);
            if (name.equals(jndiName)) {
                DataSource ds = (DataSource)context.getService(ref);
                dbAccess.setDataSource(ds);
                return;
            }
        }
        throw new RuntimeException("No DataSource with name " + name + " found");
    }

    public DbInfo getDataSourceInfo(ServiceReference ref) {
        DbInfo info = new DbInfo();
        info.jndiName = getJndiName(ref);
        DataSource ds = (DataSource)context.getService(ref);
        info.selected = (ds == dbAccess.getDataSource());
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
