/**
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
package net.lr.tutorial.karaf.camel.jpa2jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Starter for the Jms2RestAdpter to run it easily from Eclipse
 */
public class Jpa2JmsStarter {

    
    public void run() throws Exception {
        CamelContext context = new DefaultCamelContext();
        addJmsComponent(context);
        context.setTracing(true);
        context.addRoutes(new Jpa2JmsRoute());
        context.start(); 
        System.in.read();
        context.stop();
    }

    private void addJmsComponent(CamelContext context) {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
        JmsConfiguration jmsConfig = new JmsConfiguration(cf);
        context.addComponent("jms", new JmsComponent(jmsConfig));
    }
    
    public static void main(String[] args) throws Exception {
        Jpa2JmsStarter starter = new Jpa2JmsStarter();
        starter.run();
    }

}
