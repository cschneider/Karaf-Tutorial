dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Installation
------------

First see datasources project REDAME and install the derby db and datasource into karaf. 

> install -s mvn:net.lr.tutorial.karaf.db/db-examplejdbc/1.0-SNAPSHOT

Test
----

The example creates and populates a table on startup, queries it and outputs the result

The output should look like this:
-------------------------------------------
Using datasource H2, URL jdbc:h2:~/test
Christian Schneider, @schneider_chris,
-------------------------------------------
