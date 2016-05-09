# Apache Karaf Tutorial Part 9 - Annotation based blueprint and JPA

A small application to manage a list of tasks to do like in tutorial 1. Uses JEE annotations to avoid handwritten xml.

The blueprint-maven-plugin allows to use subset of the JEE annotations in source code and creates standard blueprint xml from it. This allows to build an example with JPA persistence, Transactions and a Servlet UI using zero hand written blueprint xml.

Shows how to:

* Create DataSources using pax-jdbc
* Create bundles using maven and the maven bundle plugin
* Wire bundles using CDI annotations and OSGi services
* Write JPA DAO classes like in JEE using @PersistenceContext and @Transactional
* Use the whiteboard pattern and the pax-web whiteboard extender to publish Servlets
* Interface with modern UI frameworks like Angular JS

# Structure

* model - Service interface and model classes shared between persistence and ui
* persistence - Full persistence implementation using JPA and hibernate
* ui - Servlet based UI. Uses TaskService and publishes a servlet 
* angular-ui - Angular/Bootstrap based UI

# Build

mvn clean install

# Installation

Download and start Karaf 4.0.5

Start karaf and execute the commands below

```Shell
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/tasklist-blueprint-cdi/org.ops4j.datasource-tasklist.cfg | tac -f etc/org.ops4j.datasource-tasklist.cfg
feature:repo-add mvn:net.lr.tasklist.blueprint.cdi/features/1.0.0-SNAPSHOT/xml
feature:install example-tasklist-cdi-persistence example-tasklist-cdi-ui example-tasklist-cdi-service
```

# Test

Open the UI in your browser <http://localhost:8181/tasklist> and work with the tasks.

Alternatively use the REST endpoint <http://localhost:8181/cxf/tasklistRest>

Create Task2 using the rest service

	curl -i -X POST -H "Content-Type: application/json" -d '{task:{"id":2,"title":"Task2"}}'  http://localhost:8181/cxf/tasklistRest

Retrieve Task2

	curl -i http://localhost:8181/cxf/tasklistRest/2

# Import the source in eclipse

	Import... 
	-> Existing maven projects 
	-> Browse to tasklist-ds folder 
	-> Select all projects 
	Change option Advanced -> Name template to _[groupId].[artifactId]_. This will make sure we can also import other examples.
	-> Finish
 
