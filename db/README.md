Karaf Tutorial 6 - Database access
==================================

See [Karaf Tutorial 6 on the web](http://cschneider.github.io/Karaf-Tutorial/06/).

Installing Database drivers
===========================

H2
--

    feature:install pax-jdbc-h2

Derby
-----

    feature:install pax-jdbc-derby

HSQL
----

    feature:install pax-jdbc-hsqldb

Oracle
------

As Oracle does not publish the driver in the maven repo we will first have to install the driver in our local maven repo.
You will have eventually have to adjust the path and version. The command below is for windows and Oracle 11 Express.

    mvn install:install-file -Dfile="C:\oraclexe\app\oracle\product\11.2.0\server\jdbc\lib\ojdbc6.jar" -DgroupId=ojdbc -DartifactId=ojdbc -Dversion=11.2.0.2.0 -Dpackaging=jar

Then we can install the driver in Karaf. As it is no bundle we use the wrap protocol to create a suitable Manifest on the fly:

    install -s wrap:mvn:ojdbc/ojdbc/11.2.0.2.0
    feature:install pax-jdbc-oracle

Postgresql
----------

Postgres does not build their driver as a bundle so we have to use the war protocol.

    install -s wrap:mvn:postgresql/postgresql/9.1-901-1.jdbc4
    feature:install pax-jdbc-postgresql

DB2
---

As IBM does not publish the driver in the maven repo we will first have to install the driver in our local maven repo.

    mvn install:install-file -DgroupId=com.ibm.db2.jdbc -DartifactId=db2jcc -Dversion=9.7 -Dpackaging=jar -Dfile="C:\Program Files (x86)\IBM\SQLLIB\java\db2jcc.jar"

Then we can install the driver in Karaf. As it is no bundle we use the wrap protocol to create a suitable Manifest on the fly:

    install -s wrap:mvn:com.ibm.db2.jdbc/db2jcc/9.7
    feature:install pax-jdbc-db2
