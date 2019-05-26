/*
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
package net.lr.tutorial.karaf.db.examplejpa.command;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.lr.tutorial.karaf.db.examplejpa.Person;
import net.lr.tutorial.karaf.db.examplejpa.PersonService;

@Component(service = PersonCommands.class,
        property = {"osgi.command.scope=person",
                "osgi.command.function=add",
                "osgi.command.function=list",
                "osgi.command.function=deleteAll"})
public class PersonCommands {
    @Reference
    private PersonService personService;
    
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void add(String name, String twitterName) {
        Person person = new Person(name, twitterName);
        personService.add(person);
    }
    
    public void deleteAll() {
        personService.deleteAll();
    }

    public void list() {
        List<Person> persons = personService.getAll();
        for (Person person : persons) {
            System.out.println(person.getName() + ", " + person.getTwitterName());
        }
    }
}
