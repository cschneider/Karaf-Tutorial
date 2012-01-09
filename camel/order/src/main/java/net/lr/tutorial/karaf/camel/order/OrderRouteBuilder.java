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
package net.lr.tutorial.karaf.camel.order;

import org.apache.camel.builder.RouteBuilder;

/**
 * Implements a sample order system of a internet poral like amazon.
 * 
 * Orders come  
 */
public class OrderRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:ordersin?noop=true").id("order")
       .setHeader("customer", xpath("/order/customer/text()").stringResult())
       .split(xpath("/order/item"))
       .setHeader("count", xpath("/item/@count").stringResult())
       .setHeader("name", xpath("/item/@name").stringResult())
       .choice()
            .when(xpath("/item/@vendor != 'direct'")).to("direct:vendor")
            .otherwise().recipientList(simple("file:ordersout/${header.customer}"));

        from("direct:vendor").id("mailtovendor")
        .setHeader("to", bean("vendorMailService"))
        .setHeader("subject", simple("Bestellung fuer ${header.customer}"))
        .to("velocity:mailtemplate.txt")
        .to("stream:out")
        .to("smtp:{{mailserver}}");
    }
}
