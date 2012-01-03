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

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.language.XPath;

/**
 * Resolves the mail address of a vendor by extracting the vendor name from the 
 * message and mapping it to a mail address.
 * 
 * This shows how to use a java bean in a camel route and how to map parts of the
 * message to input parameters.
 *
 */
public class GetVendorMail {
    Map<String, String> mailMap = new HashMap<String, String>();
    
    public GetVendorMail(String testMailAdress) {
            mailMap.put("vendor1", testMailAdress);
    }
    
    public String getMail(@XPath("/item/@vendor") String vendor) {
            return mailMap.get(vendor);
    }
}
