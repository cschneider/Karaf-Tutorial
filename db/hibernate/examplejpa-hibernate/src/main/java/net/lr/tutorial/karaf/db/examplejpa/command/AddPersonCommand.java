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

import net.lr.tutorial.karaf.db.examplejpa.Person;
import net.lr.tutorial.karaf.db.examplejpa.PersonService;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

@Command(scope = "person", name = "add", description = "Adds a person")
public class AddPersonCommand implements Action {
    @Argument(index=0, name="Name", required=true, description="Name", multiValued=false)
    String name;
    @Argument(index=1, name="Twitter Name", required=true, description="Twitter Name", multiValued=false)
    String twitterName;
    private PersonService personService;
    
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        Person person = new Person(name, twitterName);
        personService.add(person);
        return null;
    }

}
