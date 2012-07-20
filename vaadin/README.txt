h1. Overview

Example for the Karaf Tutorial Vaadin that implements Vaadin based UI on top of the tasklist example from the first tutorial.
The Vaadin UI reuses the original Tasklist model and persistence service. Below we deploy it alongside the original Servlet UI. 
As both use the same persistence service the UIs keep in sync.    

h1. Build

On top level of Karaf-Tutorial do:
mvn clean install

h1. Installation

features:addurl mvn:net.lr.tutorial.karaf.vaadin/tasklist-vaadin-features/1.0.0-SNAPSHOT/xml
features:install example-tasklist-vaadin

h1. Test

Old Servlet UI:
http://localhost:8181/tasklist

Vaadin UI:
http://localhost:8181/tasklist-vaadin
