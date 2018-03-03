# Karaf Tutorial Part 1 - Installation and First application

# Getting Started
{excerpt:atlassian-macro-output-type=INLINE}With this post I am beginning a series of posts about Apache Karaf, an OSGi container based on Equinox or Felix. The main difference to these frameworks is that it brings excellent management features with it.{excerpt}

Outstanding features of Karaf:

* Extensible Console with Bash like completion features
* ssh console
* deployment of bundles and features from maven repositories
* easy creation of new instances from command line
All together these features make developing server based OSGi applications almost as easy as regular java applications. Deployment and management is on a level that is much better than all applications servers I have seen till now. All this is combined with a small footprint as well of karaf as the resulting applications. In my opinion this allows a light weight development style like JEE 6 together with the flexibility of spring applications.


# Installation and first startup

* Download Karaf 4.0.7 from the [Karaf web site|http://karaf.apache.org/].
* Extract and start with bin/karaf
You should see the welcome screen:

```
        __ __                  ____
       / //_/____ __________ _/ __/
      / ,<  / __ `/ ___/ __ `/ /_
     / /| |/ /_/ / /  / /_/ / __/
    /_/ |_|\__,_/_/   \__,_/_/

  Apache Karaf (4.0.7)

Hit '<tab>' for a list of available commands
and '[cmd] \--help' for help on a specific command.
Hit '<ctrl-d>' or 'osgi:shutdown' to shutdown Karaf.

karaf@root()>

```


# Some handy commands
 
||Command||Description||
|la|Shows all installed bundles|
|list|Show user bundles|
|service:list|Shows the active OSGi services. This list is quite long. Here it is quite handy that you can use unix pipes like "ls | grep admin"|
|exports|Shows exported packages and bundles providing them. This helps to find out where a package may come from.|
|feature:list|Shows which features are installed and can be installed.|
|feature:install webconsole|Install features (a list of bundles and other features). Using the above command we install the Karaf webconsole.\\ \\
It can be reached at [http://localhost:8181/system/console|http://localhost:8181/system/console] . Log in with karaf/karaf and take some time to see what it has to offer.|
|diag|Show diagnostic information for bundles that could not be started|
|log:tail|Show the log. Use ctrl-c to  go back to Console|
|Ctrl-d|Exit the console. If this is the main console karaf will also be stopped.|


```
OSGi containers preserve state after restarts}Please note that Karaf like all osgi containers maintains it´s last state of installed and started bundles. So if something should not work anymore a restart is not sure to help. To really start fresh again stop karaf and delete the data directory or start with bin/karaf clean.
```

```
Karaf is very silent. To not miss error messages always keep a tail -f data/karaf.log open !!
```


# Tasklist - A small osgi application

Without any useful application Karaf is a nice but useless container. So let´s create our first application. The good news is that creating an OSGi application is quite easy and\\
maven can help a lot. The difference to a normal maven project is quite small. To write the application I recommend to use Eclipse 4 with the m2eclipse plugin which is installed by default on current versions.

Get the source code from the [Karaf-Tutorial repo at github|https://github.com/cschneider/Karaf-Tutorial/tree/master/tasklist].

	git clone https://github.com/cschneider/Karaf-Tutorial.git

or download the sample project from [https://github.com/cschneider/Karaf-Tutorial/zipball/master|https://github.com/cschneider/Karaf-Tutorial/zipball/master] and extract to a directory.

Import into Eclipse

* Start Eclipse Neon or newer
* In Eclipse Package explorer: Import -> Existing maven project -> Browse to the extracted directory into the tasklist sub dir
* Eclipse will show all maven projects it finds
* Click through to import all projects with defaults
Eclipse will now import the projects and wire all dependencies using m2eclipse.

The [tasklist example|https://github.com/cschneider/Karaf-Tutorial/tree/master/tasklist] consists of these projects

||Module||Description||
|tasklist-model|Service interface and Task class|
|tasklist-persistence|Simple persistence implementation that offers a TaskService|
|tasklist-ui|Servlet that displays the tasklist using a TaskService|
|tasklist-features|Features descriptor for the application that makes installing in Karaf very easy|

# Parent pom and general project setup

The pom.xml is of packaging bundle and the maven-bundle-plugin creates the jar with an OSGi Manifest. By default the plugin imports all packages that are imported in java files or referenced in the blueprint context.

It also exports all packages that do not contain the string impl or internal. In our case we want the model package to be imported but not the persistence.impl package. As the naming convention is used\\
we need no additional configuration.


## Tasklist-model

This project contains the domain model in our case it is the Task class and a TaskService interface. The model is used by both the persistence implementation and the user interface.  Any user of the TaskService will only need the model. So it is never directly bound to our current implementation.


## Tasklist-persistence

The very simple persistence implementation TaskServiceImpl manages tasks in a simple HashMap. The class uses the @Singleton annotation to expose the class as an blueprint bean.

The annotation  @OsgiServiceProvider will expose the bean as an OSGi service and the @Properties annotation allows to add serice properties. In our case the property service.exported.interfaces we set can be used by CXF-DOSGi which we present  in a later tutorial. For this tutorial the properties could also be removed.

```
@OsgiServiceProvider
@Properties(@Property(name = "service.exported.interfaces", value = "*"))
@Singleton
public class TaskServiceImpl implements TaskService {
	...
}
```

The blueprint-maven-plugin will process the class above and automatically create the suitable blueprint xml. So this saves us from writing blueprint xml by hand.

Automatically created blueprint xml can be found in target/generated-resources
```
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0">
	<bean id="taskService" class="net.lr.tasklist.persistence.impl.TaskServiceImpl" />
	<service ref="taskService" interface="net.lr.tasklist.model.TaskService" />
</blueprint>
```

## Tasklist-ui

The ui project contains a small servlet TaskServlet to display the tasklist and individual tasks. To work with the tasks the servlet needs the TaskService. We inject the TaskService by using the annotation @Inject which is able to inject any bean by type and the annotation @OsgiService which creates a blueprint reference to an OSGiSerivce of the given type.

The whole class is exposed as an OSGi service of interface java.http.Servlet with a special property alias=/tasklist. This triggers the whiteboard extender of pax web which picks up the service and exports it as a servlet at the relative url /tasklist.

Snippet of the relevant code:
```
@OsgiServiceProvider(classes = Servlet.class)
@Properties(@Property(name = "alias", value = "/tasklist"))
@Singleton
public class TaskListServlet extends HttpServlet {
    @Inject @OsgiService
    TaskService taskService;
}
```

Automatically generated xml:
```
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0">
	<reference id="taskService" availability="mandatory" interface="net.lr.tasklist.model.TaskService" />
	<bean id="taskServlet" class="net.lr.tasklist.ui.TaskListServlet">
		<property name="taskService" ref="taskService"></property>
	</bean>
	<service ref="taskServlet" interface="javax.servlet.http.HttpServlet">
		<service-properties>
			<entry key="alias" value="/tasklist" />
		</service-properties>
	</service>
</blueprint>
```

See also: [http://wiki.ops4j.org/display/paxweb/Whiteboard+Extender|http://wiki.ops4j.org/display/paxweb/Whiteboard+Extender]


## Tasklist-features

The last project only installs a feature descriptor to the maven repository so we can install it easily in Karaf. The descriptor defines a feature named tasklist and the bundles to be installed from\\
the maven repository.

```
<feature name="example-tasklist-persistence" version="${pom.version}">
    <bundle>mvn:net.lr.tasklist/tasklist-model/${pom.version}</bundle>
    <bundle>mvn:net.lr.tasklist/tasklist-persistence/${pom.version}</bundle>
</feature>

<feature name="example-tasklist-ui" version="${pom.version}">
    <feature>http</feature>
    <feature>http-whiteboard</feature>
    <bundle>mvn:net.lr.tasklist/tasklist-model/${pom.version}</bundle>
    <bundle>mvn:net.lr.tasklist/tasklist-ui/${pom.version}</bundle>
</feature>
```

A feature can consist of other features that also should be installed and bundles to be installed. The bundles typically use mvn urls. This means they are loaded from the configured maven repositories or your local maven repositiory in ~/.m2/repository.


# Installing the Application in Karaf

```
feature:repo-add mvn:net.lr.tasklist/tasklist-features/1.0.0-SNAPSHOT/xml
feature:install example-tasklist-persistence example-tasklist-ui
```

Add the features descriptor to Karaf so it is added to the available features, then Install and start the tasklist feature. After this command the tasklist application should run
```
list 
```

Check that all bundles of tasklist are active. If not try to start them and check the log.

## http:list

ID | Servlet         | Servlet-Name   | State       | Alias     | Url
-------------------------------------------------------------------------------
56 | TaskListServlet | ServletModel-2 | Deployed    | /tasklist | [/tasklist/*]

Should show the TaskListServlet. By default the example will start at [http://localhost:8181/tasklist|http://localhost:8181/tasklist] .

You can change the port by creating aa text file in "etc/org.ops4j.pax.web.cfg" with the content "org.osgi.service.http.port=8080". This will tell the HttpService to use the port 8080. Now the tasklist application should be available at [http://localhost:8080/tasklist|http://localhost:8080/tasklist]


# Summary

In this tutorial we have installed Karaf and learned some commands. Then we created a small OSGi application that shows servlets, OSGi services, blueprint and the whiteboard pattern.
