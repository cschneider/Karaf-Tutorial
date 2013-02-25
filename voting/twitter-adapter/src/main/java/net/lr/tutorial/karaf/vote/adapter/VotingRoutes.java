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
 * Receives a person record on a jms queue and sends a HTTP PUT request to the PersonService with the same body data.
 * As the put request needs the person id in the header the id is extracted from the person record and the URI
 * of the rest service is created dynamically
 */
public class VotingRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("twitter://search?type=polling&delay=10&keywords=#esbvote")
        .bean(new TweetToVoteConverter())
        .choice().when(body().isNotNull()).to("bean:voteService").end()
        .to("log:INFO");
    }
}
