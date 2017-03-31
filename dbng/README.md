# Overview

Example for the "Karaf Tutorial 11 - Database next gen - liquibase and query dsl" 

# Structure

* migrator - Migration logic for the database schema based on liquibase and ops4j PreHook
* service - Repository and gogo command accessing the db using query dsl

# Build

Make sure to use JDK 8.

mvn clean install

# Installation

Make sure you use JDK 8.
Download and extract Karaf 4.1.1.

Start karaf and execute the commands below

```Shell
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/dbng/org.ops4j.datasource-person.cfg | tac -f etc/org.ops4j.datasource-person.cfg
feature:repo-add mvn:net.lr.tutorial.dbng/dbng/1.0.0-SNAPSHOT/xml/features
feature:install example-dbng
```

# Test

In gogo shell

```Shell
person:list
```
