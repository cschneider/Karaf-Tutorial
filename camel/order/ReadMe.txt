Camel Order Example
====================

This is a slighty more complex example. The idea is to have a somewhat realistic business case and implement it in Camel.

Business Case
-------------
The business case in this example is a shop that partly works with external vendors. 

We receive an order as an xml file (See: src/test/resources/order1.xml).

The order contains a customer element and several item elements. 
Each item specifies the vendor. This can be either "direct" or a vendor name.
If the item vendor is direct then the item should be exported to a file in a directory with the customer name.
All other items are sent out by mail. The mail content should be customizeable. The mail address has to be fetched 
from a service that maps vendor name to mail address.       

How is it implemented
---------------------

The order is split by the order items using xpath.
The direct and other cases for the vendor are distinguished using a content based router.
As the destination directory for the direct case is dynamic we use the recipient list pattern to compute the file endpoint to send to.
For the external vendor case we send the mail using the smtp (camel-mail) component. The mail formatting is done using a velocity template (camel-velocity).
The lookup of the mail address is delegated to a java bean using the bean component and the @XPath annotation. 

Build
-----

> mvn clean install

How to run
----------

You can run the route either from the command line or in Apache Karaf

To run the route using maven from the command line:

> mvn exec:java

To deploy in Karaf

features:addurl mvn:org.apache.camel.karaf/apache-camel/2.9.0/xml/features
features:install camel-blueprint camel-mail camel-velocity camel-stream
install -s mvn:net.lr.tutorial.karaf.camel/example-order/1.0-SNAPSHOT
