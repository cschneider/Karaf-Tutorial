# Karaf Tutorial part 1 - Tasklist example

## Overview

Example for the Karaf Tutorial part 1 that implements a very small application to manage a list of tasks or todo items. 

It shows how to:
*   Create bundles using maven and the maven bundle plugin
*   Wire bundles using blueprint and OSGi services
*   Use the whiteboard pattern and the pax-web whiteboard extender to publish Servlets

## Structure

| Module         | Description                                                                       |
| -------------- | --------------------------------------------------------------------------------- |
| tasklist-model       | Service interface and model classes shared between persistence and ui       |
| tasklist-persistence | Simple persistence implementation using an OSGi service and a in memory map |
| tasklist-ui          | Simple servlet based UI that connects to the persistence layer using an OSGi service reference and that offers the Servlet as an OSGi service for the pax web whiteboard extender to pickup and publish |

## Build

mvn clean install

## Installation

	feature:repo-add mvn:net.lr.tasklist/tasklist-features/1.0.0-SNAPSHOT/xml
	feature:install example-tasklist

# Test

	http:list

This should show the servlet we expose.

Open the URL below in your browser [http://localhost:8181/tasklist](http://localhost:8181/tasklist). It should show the pre defined tasks.
