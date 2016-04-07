Person Service Example
======================

Shows how to publish and use a simple REST service in karaf using cxf and blueprint.

To run the example you need the http feature of karaf. The default http port is 8181 and can be configured using the 
config admin pid "org.ops4j.pax.web". You also need to install the cxf feature. The base url of the cxf servlet is by default "/cxf". 
It can be configured in the config pid "org.apache.cxf.osgi". 

The "business case" is to manage a list of persons. As service should provide the typical CRUD operations. Front ends should are a REST service and a web UI.

The example consists of:

- model: Person class and PersonService interface
- server: Service implementation and logic to publish the service using REST
- webui: Provides a simple servlet based web ui to list and add persons. Uses the REST service

Some remarks
------------

The service is implemented java first. REST is used quite transparently. This is very suitable to communicate between a client and server of the same application. If the service
is to be used by other applications the WADL first approach is more suitable. In this case the model project should be configured to generate the data classes and service interface from
a WADL.

The example uses blueprint instead of spring dm as it works much better in an OSGi environment. The bundles are created using the maven bundle plugin. A fact that shows how well blueprint works
is that the maven bundle plugin is just used with default settings. In spring dm the imports have to be configured as spring needs access to many implementation classes of cxf. For spring dm examples 
take a look at the Talend Service Factory examples (https://github.com/Talend/tsf/tree/master/examples).

Build and Test
--------------

> mvn clean install

Run the service in Karaf
------------------------

Download Apache Karaf 4 here: http://karaf.apache.org/index/community/download.html

feature:repo-add cxf 3.1.5
feature:install cxf-jaxrs http http-whiteboard

install -s mvn:net.lr.tutorial.karaf.cxf.personrest/personrest-model/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personrest/personrest-server/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personrest/personrest-webui/1.0-SNAPSHOT

Test the service
----------------

The person service should show up in the list of currently installed services that can be found here
http://localhost:8181/cxf/ 

List the known persons
http://localhost:8181/cxf/person
This should show one person "chris"

Now using a firefox extension like Poster or Httprequester you can add a person.
Send the content of server/src/test/resources/person1.xml to the following url using PUT:
http://localhost:8181/cxf/person/1001

Or to this url using POST:
http://localhost:8181/cxf/person

Now the list of persons should show two persons


Test the web UI
-------------------------

http://localhost:8181/personuirest

You should see the list of persons managed by the personservice and be able to add new persons.

How it works
------------

See blog post
