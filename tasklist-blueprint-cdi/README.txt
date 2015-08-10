h1. Overview

Example for the "Karaf Tutorial 9 - JPA made easy" 
that implements a very small application to manage a list of tasks or to dos like in tutorial 1. 
The goal was to use JEE annotations to avoid handwritten xml. The blueprint-maven-plugin allows to 
use subset of the JEE annotations in source code and creates standard blueprint xml from it.
 
This allows to build an example with JPA persistence, Transactions and a Servlet UI using zero hand written blueprint xml.

It shows how to:

- Create DataSources using pax-jdbc
- Create bundles using maven and the maven bundle plugin
- Wire bundles using CDI annotations and OSGi services
- Write JPA DAO classes like in JEE using @PersistenceContext and @Transactional
- Use the whiteboard pattern and the pax-web whiteboard extender to publish Servlets

h1. Structure

Module             | Description  
-------------------|------------------------------------------------------------------------------------
model              | Service interface and model classes shared between persistence and ui
persistence        | Full persistence implementation using JPA and hibernate
ui                 | Servlet based UI. Uses TaskService and publishes a servlet 

h1. Build example

mvn clean install

h1. Installation

Download and start Karaf 4.0.1 

Start karaf and execute the commands below

# Install H2 DB and create DataSource tasklist
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/tasklist-blueprint-cdi/org.ops4j.datasource-tasklist.cfg | tac -f etc/org.ops4j.datasource-tasklist.cfg
feature:repo-add mvn:org.ops4j.pax.jdbc/pax-jdbc-features/0.7.0-SNAPSHOT/xml/features
feature:install transaction pax-jdbc-h2 pax-jdbc-config pax-jdbc-pool-dbcp2
service:list DataSource

feature:repo-add mvn:net.lr.tasklist.cdi/tasklist-features/1.0.0-SNAPSHOT/xml
feature:install example-tasklist-cdi-persistence example-tasklist-cdi-ui

# If you also want to use the rest service do
feature:install example-tasklist-cdi-service

h1. Test

Open the url below in your browser.
http://localhost:8181/tasklist

http://localhost:8181/cxf/tasklistRest

Create Task2 using the rest service
curl -i -X POST -H "Content-Type: application/json" -d '{task:{"id":2,"title":"Task2"}}'  http://localhost:8181/cxf/tasklistRest

Retrieve Task2
curl -i http://localhost:8181/cxf/tasklistRest/3
