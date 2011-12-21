personservice-rest
==================

Shows how to publish a simple rest service in karaf using cxf and blueprint.

To run the example you need to install the http feature of karaf. The default http port is 8080 and can be configured using the 
file "etc/org.ops4j.pax.web.cfg".

You also need to install the cxf feature. The base url of the cxf servlet is by default "/cxf". It can be configured in the file

Build and Test
--------------

> mvn clean install
> mvn java:run

Override the exports of the system bundle
-----------------------------------------

Copy https://svn.apache.org/repos/asf/karaf/branches/karaf-2.2.x/assemblies/apache-karaf/src/main/filtered-resources/etc/jre.properties.cxf
to etc/jre.properties

Run in Karaf
------------

> features:addurl mvn:org.apache.cxf.karaf/apache-cxf/2.5.0/xml/features
> features:install http
> features:install cxf
> install -s mvn:net.lr.tutorial.karaf.cxf/personservice-rest/1.0-SNAPSHOT

Check in the Browser
--------------------

The person service should show up in the list of currently installed services that can be found here
http://localhost:8181/cxf/ 

List the known persons
http://localhost:8181/cxf/person
This should show one person "chris"

Now using a firefox extension like Poster or Httprequester you can add a person.
Send the content of src/test/resources/person1.xml to the following url using PUT:
http://localhost:8181/cxf/person/1001

Or to this url using POST:
http://localhost:8181/cxf/person

Now the list of persons should show two persons
