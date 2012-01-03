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
package net.lr.tutorial.karaf.camel.jms2rest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Starter for the Jms2RestAdpter to run it easily from Eclipse
 */
public class Jms2RestStarter {

    
    public void run() throws Exception {
        CamelContext context = new DefaultCamelContext();
        JmsConfiguration jmsConfig = new JmsConfiguration(new ActiveMQConnectionFactory("tcp://localhost:61616"));
        context.addComponent("jms", new JmsComponent(jmsConfig ));
        context.setTracing(true);
        context.addRoutes(new Jms2RestRoute());
        context.start(); 
        System.in.read();
        context.stop();
    }
    
    public static void main(String[] args) throws Exception {
        Jms2RestStarter starter = new Jms2RestStarter();
        starter.run();
    }

}
