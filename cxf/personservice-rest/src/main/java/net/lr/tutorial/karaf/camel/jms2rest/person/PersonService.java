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
package net.lr.tutorial.karaf.camel.jms2rest.person;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_XML)
@WebService
public class PersonService {
    Map<String, Person> personMap;
    
    public PersonService() {
        personMap = new HashMap<String, Person>();
        Person person = createExamplePerson();
        personMap.put("1", person);
    }

    private Person createExamplePerson() {
        Person person = new Person();
        person.setId("1");
        person.setName("Chris");
        return person;
    }
    
    @GET
    @Path("/")
    public Person[] getAll() {
        return personMap.values().toArray(new Person[]{});
    }
    
    @GET
    @Path("/{id}")
    public Person getPerson(@PathParam("id") String id) {
        return personMap.get(id);
    }

    @PUT
    @Path("/{id}")
    public void updatePerson(@PathParam("id") String id, Person person) {
        person.setId(id);
        System.out.println("Update request received for " + person.getId() + " name:" + person.getName());
        personMap.put(id, person);
    }
    
    @POST
    @Path("/")
    public void addPerson(Person person) {
        System.out.println("Add request received for " + person.getId() + " name:" + person.getName());
        personMap.put(person.getId(), person);
    }

    
    
}
