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
package net.lr.tutorial.karaf.vote.adapter;

import net.lr.tutorial.karaf.vote.adapter.VotingRoutes;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class VotingAdapterTest extends CamelTestSupport {
    @EndpointInject(uri="mock:received")
    MockEndpoint received;

    /**
     * We send the person record to the person queue and expect it to be added to the persons the rest service manages
     * @throws Exception
     */
    @Test
    public void testSendPerson() throws Exception {
    }

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {
        context.setTracing(true);
        //PropertiesComponent properties = new PropertiesComponent("person2jms.cfg");
        //context.addComponent("properties", properties);
        
        return new RouteBuilder[]{new VotingRoutes()};
    }
    
}
