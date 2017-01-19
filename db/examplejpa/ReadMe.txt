dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Build
-----

mvn clean install

Installation
use H2 database
------------

feature:repo-add mvn:org.ops4j.pax.jdbc/pax-jdbc-features/0.8.0/xml/features
feature:install transaction jndi pax-jdbc-h2 pax-jdbc-config pax-jdbc-pool-dbcp2 jpa/1.0.4 hibernate/4.3.6.Final

------------------------------------------------
copy cfg file form github to SERVICEMIX/etc. 
Its expected content is here :
osgi.jdbc.driver.name=H2-pool-xa
url=jdbc:h2:mem:test
dataSourceName=person

cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/db/org.ops4j.datasource-person.cfg | tac -f etc/org.ops4j.datasource-person.cfg
help : this file is present in local in this project.
------------
#install our tutorial bundle
install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT

Test
----

The bundle installs two Karaf shell commands:

person:add 'Christian Schneider' @schneider_chris

karaf@root> person:list
Christian Schneider, @schneider_chris
-> Lists all persisted Persons

Error :
Command not found: person:add
Command not found: person:list
