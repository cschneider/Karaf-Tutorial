Database commands for the Karaf shell
-------------------------------------

Allows to browse the defined DataSources and do queries and commands on them

db:select allows to browse datasources and select one for the other commands to work on.

Build
-----

mvn clean install

Installation
------------

First see datasources project REDAME and install one or more databases and datasources into karaf 

> install -s mvn:net.lr.tutorial.karaf.db/db-command/1.0-SNAPSHOT

> db:select <your datasource name>
> db:exec "create table person (name VARCHAR(100))"
> db:exec "insert into test (name) values ('Chris')"
> db:query "select * from test"
