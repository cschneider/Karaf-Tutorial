# Overview

Example for the "Karaf Tutorial 10 - Declarative services and JPA" 

In this tutorial we reimplement the TaskService example using declarative services (DS).
Aries JPA 2 is used to bring jpa support to DS. The eclipsesource jax-rs-connector is used to
export an annotated class as REST servoce.

The tutorial skips details about DataSource creation and the model bundle as they are exactly the
same as in the blueprint example. 

# Structure

* model - Service interface and model classes shared between persistence and ui
* persistence - Full persistence implementation using JPA and hibernate
* ui -Servlet based UI. Uses TaskService and publishes a servlet 

# Build

Make sure to use JDK 8.

mvn clean install

# Installation

Make sure you use JDK 8.
Download and extract Karaf 4.0.3.

Start karaf and execute the commands below

```Shell
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/tasklist-ds/org.ops4j.datasource-tasklist.cfg | tac -f etc/org.ops4j.datasource-tasklist.cfg
feature:repo-add mvn:net.lr.tasklist.ds/tasklist/1.0.0-SNAPSHOT/xml/features
feature:install example-tasklist-ds
```

# Test

Open the url below in your browser to view all known tasks:
<http://localhost:8181/tasklist>

View the tasks as a rest resource
<http://localhost:8181/cxf/tasklistRest>

Create Task2 using the rest service

	curl -i -X POST -H "Content-Type: application/json" -d '{task:{"id":2,"title":"Task2"}}'  http://localhost:8181/cxf/tasklistRest

Retrieve Task2

	curl -i http://localhost:8181/cxf/net/lr/tasklist/service/TaskServiceRest/2

# Import the source in eclipse

Import ... -> Existing maven projects -> Browse to tasklist-ds folder -> Select all projects  -> Finish

Switch Advanced -> Name template to _[groupId].[artifactId]_. This will make sure we can also import other examples.
