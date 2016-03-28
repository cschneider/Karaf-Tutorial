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

Download Apache Karaf 4 here: http://karaf.apache.org/index/community/download.html

feature:repo-add cxf 3.1.5
feature:install http http-whiteboard cxf-jaxws

install -s mvn:javax.annotation/javax.annotation-api/1.2
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-model/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-server/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-proxy/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-webui/1.0-SNAPSHOT

If you use the blueprint authz namespace you will also need to install:
install -s mvn:org.apache.aries.blueprint/org.apache.aries.blueprint.authz/1.0.0

Test the service
----------------

The person service should show up in the list of currently installed services that can be found here
http://localhost:8181/cxf/ 

The SOAP service can be reached at 
http://localhost:8181/cxf/personService


Test the web UI
-------------------------

http://localhost:8181/personui

You should see the list of persons managed by the personservice and be able to add new persons.

How it works
------------

See blog post
