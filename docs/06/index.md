---
title: Apache Karaf Tutorial Part 6 - Database Access
---

Shows how to access databases from OSGi applications running in Karaf and how to abstract from the DB product by installing DataSources as OSGi services. Some new Karaf shell commands can be used to work with the database from the command line. Finally JDBC and JPA examples show how to use such a DataSource from user code.

## Prerequisites
You need an installation of apache karaf 4.1.x for this tutorial.

## Example sources

The example projects sources are at [Karaf-Tutorial db](https://github.com/cschneider/Karaf-Tutorial/tree/master/db).

## Drivers and DataSources

In plain java it is quite popular to use the DriverManager to create a database connection (see this tutorial). In OSGi this does not work as the ClassLoader of your bundle will have no visibility of the database driver. So in OSGi the best practice is to create a DataSource at some place that knows about the driver and publish it as an OSGi service. The user bundle should then only use the DataSource without knowing the driver specifics. This is quite similar to the best practice in application servers where the DataSource is managed by the server and published to jndi.

So we need to learn how to create and use DataSources first.

## The DataSourceFactory services

To make it easier to create DataSources in OSGi the specs define a DataSourceFactory interface. It allows to create a DataSource using a specific driver from properties. Each database driver is expected to implement this interface and publish it with properties for the driver class name and the driver name.

# Introducing pax-jdbc

The pax-jdbc project aims at making it a lot easier to use databases in an OSGi environment. It does the following things:

* Implement the DataSourceFactory service for Databases that do not create this service directly
* Implement a pooling and XA wrapper for XADataSources (This is explained at the pax jdbc docs)
* Provide a facility to create DataSource services from config admin configurations
* Provide karaf features for many databases as well as for the above additional functionality

So it covers everything you need from driver installation to creation of production quality DataSources.

## Installing the driver

The first step is to install the driver bundles for your database system into Karaf. Most drivers are already valid bundles and available in the maven repo.

For several databases pax-jdbc already provides karadf features to install a current version of the database driver.

For H2 the following commands will work

```
feature:repo-add pax-jdbc 1.3.0
feature:install transaction jndi pax-jdbc-h2 pax-jdbc-pool-dbcp2 pax-jdbc-config
service:list DataSourceFactory
```

Strictly speaking we would only need the pax-jdbc-h2 feature but we will need the others for the next steps.

This will install the pax-jdbc feature repository and the h2 database driver. This driver already implements the DataSourceFactory so the last command will display this service.

```
[org.osgi.service.jdbc.DataSourceFactory]
-----------------------------------------
 osgi.jdbc.driver.class = org.h2.Driver
 osgi.jdbc.driver.name = H2
 osgi.jdbc.driver.version = 1.3.172
 service.bundleid = 72
 service.id = 124
 service.scope = singleton
```

## Creating the DataSource

Now we just need to create a configuration with the correct factory pid to create a DataSource as a service

So create the file etc/org.ops4j.datasource-person.cfg with the following content

```
osgi.jdbc.driver.name=H2
pool=dbcp2
xa=true
url=jdbc:h2:mem:test
dataSourceName=person
```

The config will automatically trigger the pax-jdbc-config module to create a DataSource.

The name osgi.jdbc.driver=H2 selects the H2 DataSourceFactory.
pool and xa enable pooling and XA support.
The url configures H2 to create a simple in memory database named test.
The dataSourceName will be reflected in a service property of the DataSource so we can find it later.
You could also set pooling configurations in this config but we leave it at the defaults

```
karaf@root()> service:list DataSource
[javax.sql.DataSource]
----------------------
 dataSourceName = person
 felix.fileinstall.filename = file:/Users/cschneid/java/apache-karaf-4.1.5/etc/org.ops4j.datasource-person.cfg
 osgi.jdbc.driver.name = H2
 osgi.jndi.service.name = person
 pax.jdbc.managed = true
 service.bundleid = 73
 service.factoryPid = org.ops4j.datasource
 service.id = 135
 service.pid = org.ops4j.datasource.3ffa37b1-d2f6-4130-9333-cd82d609011b
 service.scope = singleton
 url = jdbc:h2:mem:test
```
So when we search for services implementing the DataSource interface we find the person datasource we just created.

When we installed the features above we also installed the aries jndi feature. This module maps OSGi services to jndi objects. So we can also use jndi to retrieve the DataSource which will be used in the persistence.xml for jpa later.

## Karaf jdbc commands

Karaf contains some commands to manage DataSources and do queries on databases.

```
feature:install jdbc
jdbc:ds-list
jdbc:tables person
```

We first install the karaf jdbc feature which provides the jdbc commands. Then we list the DataSources and show the tables of the database accessed by the person DataSource.

```
jdbc:execute person "create table person (name varchar(100), twittername varchar(100))"
jdbc:execute person "insert into person (name, twittername) values ('Christian Schneider', '@schneider_chris')"
jdbc:query person "select * from person"
```

This creates a table person, adds a row to it and shows the table.

The output should look like this

```
NAME                | TWITTERNAME     
--------------------------------------
Christian Schneider | @schneider_chris
Accessing the database using JDBC
```

The project db/examplejdbc shows how to use the datasource we installed and execute jdbc commands on it. The example uses a blueprint.xml to refer to the OSGi service for the DataSource and injects it into the class
DbExample.The test method is then called as init method and shows some jdbc statements on the DataSource.The DbExample class is completely independent of OSGi and can be easily tested standalone using the DbExampleTest. This test shows how to manually set up the DataSource outside of OSGi.

## Build and install

Build works like always using maven

```
mvn clean install
```

In Karaf we just need our own bundle as we have no special dependencies

```
> install -s mvn:net.lr.tutorial.karaf.db/db-examplejdbc/1.0-SNAPSHOT
Using datasource H2, URL jdbc:h2:~/test
Christian Schneider, @schneider_chris,
```

After installation the bundle should directly print the db info and the persisted person.

## Accessing the database using JPA

For larger projects often JPA is used instead of hand crafted SQL. Using JPA has two big advantages over JDBC.

You need to maintain less SQL code
JPA provides dialects for the subtle differences in databases that else you would have to code yourself.
For this example we use Hibernate as the JPA Implementation. On top of it we add Apache Aries JPA which supplies an implementation of the OSGi JPA Service Specification and blueprint integration for JPA.

The project examplejpa shows a simple project that implements a PersonService managing Person objects.
Person is just a java bean annotated with JPA @Entitiy.

Additionally the project implements two Karaf shell commands person:add and person:list that allow to easily test the project.

### persistence.xml

Like in a typical JPA project the peristence.xml defines the DataSource lookup, database settings and lists the persistent classes. The datasource is refered using the jndi name "osgi:service/person".

The OSGi JPA Service Specification defines that the Manifest should contain an attribute "Meta-Persistence" that points to the persistence.xml. So this needs to be defined in the config of the maven bundle plugin in the prom. The Aries JPA container will scan for these attributes
and register an initialized EntityMangerFactory as an OSGi service on behalf of the use bundle.

### blueprint.xml

We use a blueprint.xml context to inject an EntityManager into our service implementation and to provide automatic transaction support.
The following snippet is the interesting part:

```
  <bean id="personService" class="net.lr.tutorial.karaf.db.examplejpa.impl.PersonServiceImpl">
      <jpa:context property="em" unitname="person" />
      <tx:transaction method="*" value="Required"/>
  </bean>
```

This makes a lookup for the EntityManagerFactory OSGi service that is suitable for the persistence unit person and injects a thread safe EnityManager (using a ThreadLocal under the hood) into the
PersonServiceImpl. Additionally it wraps each call to a method of PersonServiceImpl with code that opens a transaction before the method and commits on success or rollbacks on any exception thrown.

### Build and Install

```
mvn clean install
```

A prerequisite is that the derby datasource is installed like described above. Then we have to install the bundles for hibernate, aries jpa, transaction, jndi and of course our db-examplejpa bundle.
See ReadMe.txt for the exact commands to use.

### Test

```
person:add 'Christian Schneider' @schneider_chris
```

Then we list the persisted persons

```
karaf@root> person:list
Christian Schneider, @schneider_chris
```

## Summary

In this tutorial we learned how to work with databases in Apache Karaf. We installed drivers for our database and a DataSource. We were able to check and manipulate the DataSource using the jdbc:* commands. In the examplejdbc we learned how to acquire a datasource
and work with it using plain jdbc4.  Last but not least we also used jpa to access our database.

[Back to Karaf Tutorials](..)
