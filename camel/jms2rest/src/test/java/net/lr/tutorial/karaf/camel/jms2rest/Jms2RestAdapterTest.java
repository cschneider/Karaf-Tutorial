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

import java.io.InputStream;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.XPathBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class Jms2RestAdapterTest extends CamelTestSupport {
    @EndpointInject(uri="mock:received")
    MockEndpoint received;

    /**
     * We send the person record to the person queue and expect it to be added to the persons the rest service manages
     * @throws Exception
     */
    @Ignore
    public void testSendPerson() throws Exception {
        received.expectedMessageCount(1);
        InputStream is = this.getClass().getResourceAsStream("/person1.xml");
        
        template.requestBody("jms:person", is);

        received.assertIsSatisfied();
        Exchange ex = received.getExchanges().get(0);
        String id = XPathBuilder.xpath("//id").namespace("ns1", "http://person.jms2rest.camel.karaf.tutorial.lr.net").stringResult().evaluate(ex, String.class);
        Assert.assertEquals("1001", id);
    }

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {
        context.setTracing(true);
        PropertiesComponent properties = new PropertiesComponent("person2jms.cfg");
        context.addComponent("properties", properties);
        addTestJmsComponent();
        
        return new RouteBuilder[]{new Jms2RestRoute(), new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {
                from("jetty:{{personServiceUri}}?matchOnUriPrefix=true").to(received);
            }
        }};
    }

    /*
     * Adds a local in memory JMS component for the test
     */
    private void addTestJmsComponent() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://test?broker.persistent=false");
        JmsConfiguration jmsConfig = new JmsConfiguration(connectionFactory);
        JmsComponent component = new JmsComponent(jmsConfig);
        context.addComponent("jms", component);
    }
    
}
