h1. Overview

Example for the "Karaf Tutorial 9 - CDI and JavaEE meet OSGi" that implements a very small application to manage a list of tasks or to dos like in tutorial 1. Instead of blueprint this example is using pax cdi to leverage the Java EE dependency injection. 

It shows how to:

- Create bundles using maven and the maven bundle plugin
- Wire bundles using cdi and OSGi services
- Use the whiteboard pattern and the pax-web whiteboard extender to publish Servlets

h1. Structure

model - Service interface and model classes shared between persistence and ui
persistence - Simple persistence implementation using an OSGi service and a in memory map
ui - Simple servlet based UI that connects to the persistence layer using an OSGi service reference and that offers the Servlet as an OSGi service for the pax web whiteboard extender to pickup and publish

h1. Build

mvn clean install

h1. Installation

Start karaf 2.3.3
features:addurl mvn:org.ops4j.pax.cdi/pax-cdi-features/0.6.0/xml/features
features:addurl mvn:net.lr.tasklist.cdi/tasklist-features/1.0.0-SNAPSHOT/xml
features:install example-tasklist-cdi-persistence-simple example-tasklist-cdi-ui

Start Karaf 3.0.0-SNAPSHOT

feature:repo-add mvn:org.ops4j.pax.cdi/pax-cdi-features/0.6./xml/features
feature:repo-add mvn:net.lr.tasklist.cdi/tasklist-features/1.0.0-SNAPSHOT/xml
feature:install example-tasklist-cdi-persistence-simple example-tasklist-cdi-ui

h1. Test

Open the url below in your browser.
http://localhost:8181/tasklist
