dbexample
---------

Example how to access a DataSource using jpa and declarative services.
In a real world application you might want to split into an API bundle, an service bundle and a UI bundle. 
For this example we keep all these in one bundle.

Build
-----

mvn clean install

Installation
------------

feature:install scr transaction jndi pax-jdbc-h2 pax-jdbc-config pax-jdbc-pool-dbcp2 jpa hibernate/5.4.2.Final
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/db/org.ops4j.datasource-person.cfg | tac etc/org.ops4j.datasource-person.cfg
install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT

The commands above are tested with karaf 4.2.5. For other karaf versions you might need to change the versions above to the ones available.

Test
----

The bundle installs two Karaf shell commands:

person:add 'Christian Schneider' @schneider_chris

karaf@root> person:list
Christian Schneider, @schneider_chris
-> Lists all persisted Persons
