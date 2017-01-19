dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Build
-----
mvn clean install

Installation
------------

feature:repo-add mvn:org.ops4j.pax.jdbc/pax-jdbc-features/0.8.0/xml/features
#feature:install transaction jndi pax-jdbc-h2 pax-jdbc-pool-dbcp2 pax-jdbc-config
feature:install transaction jndi-jdbc-pax h2 pax-jdbc-config pax-jdbc-pool-dbcp2 jpa/1.0.4 hibernate/4.3.6.Final 

Copy cfg fle from guitub to SERVICEMIX/etc dir. Its content is here :
osgi.jdbc.driver.name=H2-pool-xa
url=jdbc:h2:mem:test
dataSourceName=person
----
cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/db/org.ops4j.datasource-person.cfg | tac -f etc/org.ops4j.datasource-person.cfg

install -s mvn:net.lr.tutorial.karaf.db/db-examplejdbc/1.0-SNAPSHOT

Test
----

The example creates and populates a table on startup, queries it and outputs the result

The output should look like this:
-------------------------------------------
Using datasource H2, URL jdbc:h2:mem:test
Christian Schneider, @schneider_chris,
-------------------------------------------
From web tutorial Part6 :
http://www.liquid-reality.de/display/liquid/2012/01/13/Apache+Karaf+Tutorial+Part+6+-+Database+Access#
jdbc:execute person create table person(name varchar(100),twittername varchar(100))
