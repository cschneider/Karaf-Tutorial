JMS commands for the Karaf shell
--------------------------------

Allows to browse the defined connection factories and do commands on them

Build
-----

mvn clean install

Installation
------------

The easiest way to test this is with activemq as it is easily installable in Apache Karaf. 

Install ActiveMQ
> features:addurl mvn:org.apache.activemq/activemq-karaf/5.5.1/xml/features
> features:install activemq activemq-spring activemq-blueprint

Create and start a broker instance and a Connection Factory
> activemq:create-broker
 
 Install the jms commands
> install -s mvn:net.lr.karaf.jms/jms-command/1.0-SNAPSHOT

Now you can use the commands on the installed activemq connection factory named localhost
> jms:select localhost
> jms:send testqueue "Some content"
> jms:listqueues
> jms:browse testqueue
> jms:delete -a testqueue
