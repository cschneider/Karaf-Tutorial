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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import net.lr.tutorial.karaf.camel.jpa2jms.model.Person;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

/**
 * Watches the person table in the db. For each row found there the camel route is triggered with 
 * a Person object as message body. Then this object is marshalled to xml using jaxb and sent to the 
 * jms queue person.
 * 
 * The second route is just to show it worked. It listens on the jms queue person and writes the xml content to the log
 */
public class Jpa2JmsRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JaxbDataFormat df = createPersonJaxbDataFormat();

        from("jpa://net.lr.tutorial.karaf.camel.jpa2jms.model.Person?consumer.delay=2000").id("jpa2jms")
        .onException(Exception.class).maximumRedeliveries(3).backOffMultiplier(2).handled(true).to("file:error").end()
        .transacted("PROPAGATION_REQUIRED")
        .marshal(df)
        .to("log:person marshalled")
        .bean(new ExceptionDecider())
        .to("jms:person");
        
        from("jms:person").id("jms2log")
        .onException(Exception.class).maximumRedeliveries(3).backOffMultiplier(2).handled(true).to("file:error").end()
        .transacted("PROPAGATION_REQUIRED")
        .convertBodyTo(String.class)
        .to("log:personreceived");
    }

    private JaxbDataFormat createPersonJaxbDataFormat() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
        JaxbDataFormat df = new JaxbDataFormat(jaxbContext);
        df.setContextPath(Person.class.getPackage().toString());
        return df;
    }
}
