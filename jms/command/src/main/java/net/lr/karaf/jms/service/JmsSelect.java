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
package net.lr.karaf.jms.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class JmsSelect {
    private BundleContext context;
    private JmsService jmsService;
    
    public void init() {
        if (jmsService.getConnectionFactory() == null) {
            selectFirstConnectionFactory();
        }
    }

    private void selectFirstConnectionFactory() {
        Collection<String> names = getConnectionFactoryNames();
        if (names.size() > 0) {
            selectDataSource(names.iterator().next());
        }
    }

    public ServiceReference[] getConnectionFactories() {
        try {
            ServiceReference[] dsRefs = context.getServiceReferences(ConnectionFactory.class.getName(), null);
            if (dsRefs == null) {
                dsRefs = new ServiceReference[]{};
            }
            return dsRefs;
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<String> getConnectionFactoryNames() {
        ServiceReference[] dataSources = getConnectionFactories();
        List<String> dsNames = new ArrayList<String>();
        for (ServiceReference ref : dataSources) {
            dsNames.add(getName(ref));
        }
        return dsNames;
    }

    private String getName(ServiceReference ref) {
        String jndiName = (String)ref.getProperty("name");
        return jndiName;
    }

    public void selectDataSource(String name) {
        ServiceReference[] dataSources = getConnectionFactories();
        for (ServiceReference ref : dataSources) {
            String jndiName = getName(ref);
            if (name.equals(jndiName)) {
                ConnectionFactory cf = (ConnectionFactory)context.getService(ref);
                jmsService.setConnectionFactory(cf);
                return;
            }
        }
        throw new RuntimeException("No ConnectionFactory with name " + name + " found");
    }

    public JmsInfo getConnectionFactoryInfo(ServiceReference ref) {
        JmsInfo info = new JmsInfo();
        info.name = getName(ref);
        ConnectionFactory cf = (ConnectionFactory)context.getService(ref);
        info.selected = (cf == jmsService.getConnectionFactory());
        try {
            Connection con = cf.createConnection();
            ConnectionMetaData meta = con.getMetaData();
            info.product = meta.getJMSProviderName();
            info.version = meta.getProviderVersion();
        } catch (Exception e) {
            info.url = "Connect failed " + e.getMessage();
        }
        return info; 
    }
    

    public void setContext(BundleContext context) {
        this.context = context;
    }

    public void setJmsService(JmsService jmsService) {
        this.jmsService = jmsService;
    }

}
