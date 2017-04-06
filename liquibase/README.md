# Karaf Tutorial 11 - Managing database schemas with liquibase

One typical pain point in deployments is the database schema and contents. The database schema changes over time and can not be deployed like bundles. Sometimes even the database contents have to be adapted when the schema changes. In a mordern deployment pipeline we of course also want to automate this part of the deployment.

This tutorial builds upon the db tutorial. We assume familiarity with how to create DataSources using pax-jdbc-config and declarative services.

# Structure

* migrator - Migration logic for the database schema based on liquibase and ops4j PreHook
* service - Repository and gogo command accessing the db using simple jdbc4

# Build

Make sure to use JDK 8.

mvn clean install

# Installation

Make sure you use JDK 8.
Download and extract Karaf 4.1.1.

Start karaf and execute the commands below

```Shell
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/liquibase/org.ops4j.datasource-person.cfg | tac etc/org.ops4j.datasource-person.cfg
feature:repo-add mvn:net.lr.tutorial.lb/lb/1.0.0-SNAPSHOT/xml/features
feature:install example-lb
```

# Test

In gogo shell

```Shell
person:list
```
