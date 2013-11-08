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

import org.apache.camel.builder.RouteBuilder;

/**
 * Receives votes based on twitter searches and direct messages 
 */
public class VotingRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("twitter://search?type=polling&delay=10&keywords=#esbvote")
        .bean(new TwitterConverter())
        .to("direct:vote");
        
        from("twitter://directmessage?type=polling&delay=60")
        .bean(new TwitterConverter())
        .to("direct:vote");
        
    	from("irc:karafvoting@cameron.freenode.net:6667/karafvoting")
    	.bean(new IrcConverter())
    	.to("direct:vote");
        
        from("direct:vote")
        .choice().when(body().isNotNull()).to("bean:voteService").end()
        .to("log:INFO");
    }
}
