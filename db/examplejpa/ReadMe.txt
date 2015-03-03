dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Build
-----

mvn clean install

Installation
------------

feature:repo-add mvn:org.ops4j.pax.jdbc/pax-jdbc-features/0.5.0/xml/features
feature:install transaction jndi pax-jdbc-h2 pax-jdbc-config pax-jdbc-pool-dbcp2 jpa/2.1.0 hibernate/4.3.6.Final
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/db/org.ops4j.datasource-person.cfg | tac -f etc/org.ops4j.datasource-person.cfg
install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT

Test
----

The bundle installs two Karaf shell commands:

person:add 'Christian Schneider' @schneider_chris

karaf@root> person:list
Christian Schneider, @schneider_chris
-> Lists all persisted Persons

