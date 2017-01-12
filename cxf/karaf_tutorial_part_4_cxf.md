{excerpt}
This tutorial shows how to install CXF on Karaf and how to run a CXF based Rest Service in Karaf
{excerpt}

h1. Person Service Example

Shows how to publish and use a simple REST and SOAP service in karaf using cxf and blueprint.

To run the example you need to install the http feature of karaf. The default http port is 8080 and can be configured using the 
config admin pid "org.ops4j.pax.web". You also need to install the cxf feature. The base url of the cxf servlet is by default "/cxf". 
It can be configured in the config pid "org.apache.cxf.osgi". 

The "business case" is to manage a list of persons. As service should provide the typical CRUD operations. Front ends should be a REST service, a SOAP service and a web UI.

The example consists of four projects

* model: Person class and PersonService interface
* server: Service implementation and logic to publish the service using REST and SOAP
* proxy: Accesses the SOAP service and publishes it as an OSGi service
* webui: Provides a simple servlet based web ui to list and add persons. Uses the OSGi service

You can find the full source on github:  https://github.com/cschneider/Karaf-Tutorial/tree/master/cxf/personservice

h1. Installation and test run

First we build, install and run the example to give an overview of what it does. The following main chapter then explains in detail how it works.

h2. Installing Karaf and preparing for CXF

We start with a fresh Karaf 4.0.4
Unpack Karaf from http://karaf.apache.org/index/community/download.html
Run Karaf using bin/karaf

h2. Installing CXF

In Karaf Console run

{code}
feature:repo-add cxf 3.1.5
feature:install http cxf-jaxws http-whiteboard
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-model/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-server/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-proxy/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-webui/1.0-SNAPSHOT
{code}

h2. Build and Test

> mvn clean install


h2. Install service and ui in karaf

{code}
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-model/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-server/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-proxy/1.0-SNAPSHOT
install -s mvn:net.lr.tutorial.karaf.cxf.personservice/personservice-webui/1.0-SNAPSHOT
{code}

h2. Test the service

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

h2. Test the proxy and web UI

http://localhost:8181/personui

You should see the list of persons managed by the personservice and be able to add new persons.

h1. How it works

h2. Defining the model

The model project is a simple java maven project that defines a JAX-WS service and a JAXB data class. It has no dependencies to cxf. The service interface is just a plain java interface with the @WebService annotation.

{code}
@WebService
public interface PersonService {
    public abstract Person[] getAll();
    public abstract Person getPerson(String id);
    public abstract void updatePerson(String id, Person person);
    public abstract void addPerson(Person person);
}
{code}

The [Person|https://github.com/cschneider/Karaf-Tutorial/blob/master/cxf/personservice/model/src/main/java/net/lr/tutorial/karaf/cxf/personservice/person/Person.java] class is just a simple pojo with getters and setters for id, name and url and the necessary JAXB annotations. Additionally you need an ObjectFactory to tell JAXB what xml element to use for the Person class. 
There is also no special code for OSGi in this project. So the model works perfectly inside and outside of an OSGi container.

{note}
The service is defined java first. SOAP and rest are used quite transparently. This is very suitable to communicate between a client and server of the same application. If the service
is to be used by other applications the wsdl first approach is more suitable. In this case the model project should be configured to generate the data classes and service interface from
a wsdl (see cxf wsdl_first example pom file). For rest services the java first approach is quite common in general as the client typically does not use proxy classes anyway.
{note}

h2. Service implementation (server)

[PersonServiceImpl|https://github.com/cschneider/Karaf-Tutorial/blob/master/cxf/personservice/server/src/main/java/net/lr/tutorial/karaf/cxf/personservice/impl/PersonServiceImpl.java] is a java class the implements the service interface and contains some additional JAX-RS annotations. The way the class is defined allows it to implement a REST service and a SOAP service at the same time.

The server project also contains a small starter class that allows the service to be published directly from eclipse. This class is not necessary for deployment in karaf.

The production deployment of the service is done in [src/main/resources/OSGI-INF/blueprint/blueprint.xml|https://github.com/cschneider/Karaf-Tutorial/blob/master/cxf/personservice/server/src/main/resources/OSGI-INF/blueprint/blueprint.xml].

As the file is in the special location OSGI-INF/blueprint it is automatically processed by the blueprint implementation aries in karaf. The REST service is published using the jaxrs:server element and the SOAP service is published using the jaxws:endpoint element. The blueprint namespaces are different from spring but apart from this the xml is very similar to a spring xml.

h2. Service proxy

The service proxy project only contains a blueprint xml that uses the CXF JAXWS client to consume the SOAP service and exports it as an OSGi Service. Encapsulating the service client as an OSGi service (proxy project) is not strictly necessary but it has the advantage that the webui is then completely independent of cxf. So it is very easy to change the way the service is accessed. So this is considered a best practice in OSGi.

See [blueprint.xml|https://github.com/cschneider/Karaf-Tutorial/blob/master/cxf/personservice/proxy/src/main/resources/OSGI-INF/blueprint/blueprint.xml]

h2. Web UI (webui)

This project consumes the PersonService OSGi service and exports the PersonServlet as an OSGi service. The pax web whiteboard extender will then publish the servlet on the location /personui.
The [PersonServlet|https://github.com/cschneider/Karaf-Tutorial/blob/master/cxf/personservice/webui/src/main/java/net/lr/tutorial/karaf/cxf/personservice/webui/PersonServlet.java] gets the PersonService injected and uses to get all persons and also to add persons.

The wiring is done using a [blueprint context|https://github.com/cschneider/Karaf-Tutorial/blob/master/cxf/personservice/webui/src/main/resources/OSGI-INF/blueprint/blueprint.xml].

h2. Some further remarks

The example uses blueprint instead of spring dm as it works much better in an OSGi environment. The bundles are created using the maven bundle plugin. A fact that shows how well blueprint works
is that the maven bundle plugin is just used with default settings. In spring dm the imports have to be configured as spring needs access to many implementation classes of cxf. For spring dm examples 
take a look at the Talend Service Factory examples (https://github.com/Talend/tsf/tree/master/examples).

The example shows that writing OSGi applications is quite simple with aries and blueprint. It needs only 153 lines of java code (without comments) for a complete little application. 
The blueprint xml is also quite small and readable.    
