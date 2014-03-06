dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Important Note:
Hibernate 4.3.x is not compatible with aries jpa 1.0.0 which is used in karaf 2.3.4 and 3.0.0. Hibernate exposes the PersistenceProvider OSGi service with 
package version 2.1 and aries jpa only accepts 2.0. So make sure you use hibernate 4.2.x.

Build
-----

mvn clean install

Installation
------------

First see datasources project README and install the derby db and datasource into karaf 2.x. 
For karaf 3 replace features:install with feature:install.

features:addurl mvn:net.lr.hibernate/hibernate-features/1.0.0-SNAPSHOT/xml
install -s mvn:org.apache.derby/derby/10.8.2.2
features:install jndi hibernate
#install -s mvn:commons-pool/commons-pool/1.5.4
#install -s mvn:commons-dbcp/commons-dbcp/1.4
#install -s mvn:commons-lang/commons-lang/2.6
#install -s wrap:mvn:net.sourceforge.serp/serp/1.13.1
install -s mvn:net.lr.tutorial.karaf.db/examplejpa-hibernate/1.0-SNAPSHOT

Karaf 3:
install -s mvn:org.apache.derby/derby/10.8.2.2
feature:install jdbc
jdbc:create -t derby derbyds

feature:install jndi hibernate/4.2.7
install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT


Test
----

The bundle installs two Karaf shell commands:

person:add 'Christian Schneider' @schneider_chris

karaf@root> person:list
Christian Schneider, @schneider_chris
-> Lists all persisted Persons

