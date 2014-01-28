Jpa2jms
========

Shows how camel-jpa, camel-jms, aries jta and Karaf can work together to build an event driven and transactional integration module.
The example reads from a database table and writes to a jms queue.

Prerequisites
-------------

You should have done and built the db and camel tutorials before.

Build
-----

Go to the example project directory and type

> mvn clean install


Run in Karaf
------------

Install Prerequisites
---------------------

Karaf 2.x:

features:addUrl mvn:org.apache.activemq/activemq-karaf/5.6.0/xml/features
features:addurl mvn:org.apache.camel.karaf/apache-camel/2.9.2/xml/features
features:install camel-blueprint camel-jms camel-jpa camel-jaxb camel-spring
features:install activemq-spring
features:install jpa jndi transaction
install -s  mvn:org.apache.derby/derby/10.8.2.2
install -s mvn:commons-dbcp/commons-dbcp/1.4
install -s mvn:org.apache.openjpa/openjpa/2.1.1

Karaf 3:

feature:repo-add activemq 5.9.0
feature:repo-add camel 2.12.0
feature:install camel-blueprint camel-jms camel-jpa camel-jaxb camel-spring activemq-blueprint jdbc jpa jndi transaction
install -s  mvn:org.apache.derby/derby/10.8.2.2
install -s mvn:commons-dbcp/commons-dbcp/1.4   

jdbc:create -t derby derbyds
copy connectionfactory-default.xml to deploy folder

# Alternatively you can create the connection factory
# and in the file created in deploy add userName 'karaf' and password 'karaf' properties on the connection factory
jms:create -t activemq -u tcp://localhost:61616 default


Install our example
-------------------

install -s mvn:net.lr.tutorial.karaf.db/db-examplejpa/1.0-SNAPSHOT 
install -s mvn:net.lr.tutorial.karaf.cameljpa/jpa2jms/1.0-SNAPSHOT

What did we install
-------------------

We first added the feature files for camel and activemq.
Then we installed the necessary features for a local ActiveMQ broker the necessary camel deps and openjpa. 

The create broker command creates a blueprint xml file in the deploy folder that starts and configures a broker. It also initializes a
PoolingConnectionFactory as an OSGi service that we need for our example.

As a last step we install the jpa2jms example and also the db-examplejpa. The second provides the karaf commands we need to fill the database
so camel-jpa can work on it.

Test
----

person:add "Christian Schneider" @schneider_chris
log:display

You should then see the following line in the log:
2012-07-19 10:27:31,133 | INFO  | Consumer[person] | personreceived ...
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<person>
    <name>Christian Schneider</name>
    <twitterName>@schneider_chris2</twitterName>
</person>
]

So what happened?

The person:add command is located in the dbexamplejpa project. It adds the data we entered to the person table in the derby DB. 
Now our new current example code kicks in. The jpa: route watches the person table, finds a row, reads it and creates a camel message with a Person
as body. This message is then routed. On the way the Person object is marshalled to xml and sent to the person queue. When all works the jpa endpoint
deletes the row from the person table.

Our second route picks up the message and logs it. In a real integration this second route would not be present here. Instead another module would process
the message.
