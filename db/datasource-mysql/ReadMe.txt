h1. Mysql DataSource

Initializes a mysql datasource and exports it as an OSGi service. To make it easier for an admin the properties
of the datasource can be configured using the config admin service.

This way to define a datasource is mainly for people who do not want to use blueprint. The blueprint way shown in the
other datasource definitions is much easier.

h2. Build

mvn clean install

h2. Install

Create the config:

This depends on how your config admin service is implemented. In the case of Karaf which uses Felix config admin and Felix FileInstall you do the following:

Create a file "etc/mysqldatasource.cfg" with the following content:

name=jdbc/mysqlds
url=jdbc:mysql://localhost:3306/world
user=test
password=test

Install the MySQL driver and datasource using:

install -s mvn:org.apache.servicemix.specs/org.apache.servicemix.specs.stax-api-1.0/1.9.0
install -s mvn:mysql/mysql-connector-java/5.1.18
install -s mvn:net.lr.tutorial.karaf.db/db-datasource-mysql/1.0-SNAPSHOT

Check it worked:

In Karaf you can execute:
> list
> bundle:services -p <your bundle id>

You should see the ManagedService and the DataSource we created
