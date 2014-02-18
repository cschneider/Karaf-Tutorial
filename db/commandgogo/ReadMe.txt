Database commands for the Karaf shell
-------------------------------------

Demo for the experimental action exporter. See https://issues.apache.org/jira/browse/KARAF-2762

Allows to browse the defined DataSources and do queries and commands on them

db:select allows to browse datasources and select one for the other commands to work on.

Build
-----

mvn clean install

Prerequisites
-------------

Requires Apache Karaf 2.x. Will not work on apache karaf 3 as the blueprint shell namespace changed in an incompatible way.

Karaf 3 contains the jdbc feature by default though. So an improved version of these commands can be installed using feature:install jdbc.

Installation
------------

First see datasources project REDAME and install one or more databases and datasources into karaf 

feature:install scr
install -s mvn:net.lr.tutorial.karaf.db/db-command-gogo/1.0-SNAPSHOT

> db:select <your datasource name>
> db:exec "create table person (name VARCHAR(100))"
> db:exec "insert into person (name) values ('Chris')"
> db:query "select * from person"
