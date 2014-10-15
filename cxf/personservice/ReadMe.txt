Person Service Example
======================

Shows how to publish and use a simple SOAP service in karaf using cxf and blueprint.

To run the example you need to install the http feature of karaf. The default http port is 8181 and can be configured using the 
config admin pid "org.ops4j.pax.web". You also need to install the cxf feature. The base url of the cxf servlet is by default "/cxf". 
It can be configured in the config pid "org.apache.cxf.osgi". 

The "business case" is to manage a list of persons. As service should provide the typical CRUD operations. Front ends should be a SOAP service and a web UI.

The example consists of four projects:

- model: Person class and PersonService interface
- server: Service implementation and logic to publish the service using SOAP
- proxy: Accesses the SOAP service and publishes it as an OSGi service
- webui: Provides a simple servlet based web ui to list and add persons. Uses the OSGi service

Some remarks
------------

The encapsulating the service client as an OSGi service is not strictly necessary but it has the advantage that the webui is then completely independent of cxf. So it is very easy to
exchange the way the service is accessed.

The service is implemented java first. This is very suitable to communicate between a client and server of the same application. If the service
is to be used by other applications the wsdl first approach is more suitable. In this case the model project should be configured to generate the data classes and service interface from
a wsdl (see cxf wsdl_first example).

Build and Test
--------------

> mvn clean install

Run the service in Karaf
------------------------

Download Apache Karaf here: http://karaf.apache.org/index/community/download.html

Karaf 2.x
features:chooseurl cxf 2.7.10
features:install http cxf-jaxws

Karaf 3
feature:repo-add cxf 3.0.1-SNAPSHOT
feature:install cxf-jaxrs

install -s mvn:javax.annotation/javax.annotation-api/1.2
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-model/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-server/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-proxy/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-webui/1.0-SNAPSHOT

Test the service
----------------

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

Test the web UI
-------------------------

http://localhost:8181/personui

You should see the list of persons managed by the personservice and be able to add new persons.

How it works
------------

See blog post
