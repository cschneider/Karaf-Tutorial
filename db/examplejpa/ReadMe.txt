dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Build
-----

mvn clean install

Installation
------------

First see datasources project REDAME and install the derby db and datasource into karaf (2.2.10). 

install -s mvn:org.apache.derby/derby/10.8.2.2
features:install jndi jpa transaction
install -s mvn:commons-collections/commons-collections/3.2.1
install -s mvn:commons-pool/commons-pool/1.5.4
install -s mvn:commons-dbcp/commons-dbcp/1.4
install -s mvn:commons-lang/commons-lang/2.6
install -s wrap:mvn:net.sourceforge.serp/serp/1.13.1
install -s mvn:org.apache.openjpa/openjpa/2.1.1
install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT

Test
----

The bundle installs two Karaf shell commands:

person:add 'Christian Schneider' @schneider_chris

karaf@root> person:list
Christian Schneider, @schneider_chris
-> Lists all persisted Persons

