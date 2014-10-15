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
package net.lr.tutorial.karaf.cxf.personrest.impl;

import java.io.IOException;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;

/**
 * Start the rest service to test in the IDE
 *
 */
public class PersonServiceStarter {
    
    @SuppressWarnings("rawtypes")
    public void startRestService() {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress("http://localhost:8282/person");
        factory.setResourceClasses(PersonServiceImpl.class);
        factory.setResourceProvider(new SingletonResourceProvider(new PersonServiceImpl()));
        factory.setProvider(new JAXBElementProvider());
        Server server = factory.create();
        server.start();
    }

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        new PersonServiceStarter().startRestService();
        System.in.read();
    }

}
