dbexample
---------

Example how to access a DataSource using OSGi services and jdbc.

Installation
------------

First see datasources project REDAME and install the derby db and datasource into karaf. 

install -s mvn:org.apache.geronimo.specs/geronimo-annotation_1.0_spec/1.1.1
install -s mvn:commons-lang/commons-lang/2.6
install -s mvn:commons-pool/commons-pool/1.5.4
install -s mvn:commons-dbcp/commons-dbcp/1.4
install -s mvn:commons-collections/commons-collections/3.2.1
install -s mvn:org.apache.geronimo.specs/geronimo-jpa_2.0_spec/1.1
install -s wrap:mvn:net.sourceforge.serp/serp/1.13.1
install -s mvn:org.apache.openjpa/openjpa/2.1.1
install -s mvn:org.apache.aries.transaction/org.apache.aries.transaction.blueprint/0.3
install -s mvn:org.apache.aries.transaction/org.apache.aries.transaction.manager/0.3
install -s mvn:org.apache.aries.jpa/org.apache.aries.jpa.api/0.3
install -s mvn:org.apache.aries.proxy/org.apache.aries.proxy/0.3
install -s mvn:org.apache.aries.jpa/org.apache.aries.jpa.container/0.3
install -s mvn:org.apache.aries.jpa/org.apache.aries.jpa.blueprint.aries/0.3
install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT

Test
----

The example creates and populates a table on startup, queries it and outputs the result

The output should look like this:
Using datasource H2 ver jdbc:h2:~/test
Christian Schneider, @schneider_chris,
