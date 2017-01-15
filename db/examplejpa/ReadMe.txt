dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Build
-----

mvn clean install

Installation
------------

feature:install transaction jndi pax-jdbc-h2 pax-jdbc-config pax-jdbc-pool-dbcp2 jpa/1.0.4 hibernate/4.3.6.Final
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/db/org.ops4j.datasource-person.cfg | tac -f etc/org.ops4j.datasource-person.cfg
install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT

The commands above are tested with karaf 4.0.8. For other karaf versions you might need to change the versions above to the ones available.

Test
----

The bundle installs two Karaf shell commands:

person:add 'Christian Schneider' @schneider_chris

karaf@root> person:list
Christian Schneider, @schneider_chris
-> Lists all persisted Persons

