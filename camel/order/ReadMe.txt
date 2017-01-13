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

his example again uses maven to build, a blueprint.xml context to boot up camel and a java class OrderRouteBuilder for the camel routes. So from an OSGi perspective it works almost the same as the jms2rest example.
The routes are defined in net.lr.tutorial.karaf.camel.order.OrderRouteBuilder. The "order" route listens on the directory "orderin" and expects xml order files to be placed there. The route uses xpath to extract several attributes of the order into message headers. A splitter is used to handle each (/order/item) spearately. Then a content based router is used to handle "direct" items different from others.
In the case of a direct item the recipientlist pattern is used to build the destination folder dynamically using a simple language expression.
recipientList(simple("file:ordersout/${header.customer}"))
If the vendor is not "direct" then the route "mailtovendor" is called to create and send a mail to the vendor. Some subject and to address are set using special header names that the mail component understands. The content of the mail is expected in the message body. As the body also should be comfigureable the velocity component is used to fill the mailtemplate.txt with values from the headers that were extracted before.

To run the route using maven from the command line:

> mvn clean install

To deploy in Karaf

The deployment is also very similar to the previous example but a little simpler as we do not need jms. Type the following in karaf
feature:repo-add camel 2.16.2
feature:install camel-blueprint camel-mail camel-velocity camel-stream
install -s mvn:net.lr.tutorial.karaf.camel/example-order/1.0-SNAPSHOT

To be able to receive the mail you have to edit the configuration pid. You can either do this by placing a properties file
into etc/net.lr.tutorial.karaf.cxf.personservice.cfg or editing the config pid using the karaf webconsole. (See part 2 and part 3 of the Karaf Tutorial series).
Basically you have to set these two properties according to your own mail environment.
mailserver=yourmailserver.com
testVendorEmail=youmail@yourdomain.com

Test the order example
----------
Copy the file order1.xml into the folder "ordersin" below the karaf dir.
The Karaf console will show:
Order from Christian Schneider
 
Count: 1, Article: Flatscreen TV
The same should be in a mail in your inbox. At the same time a file should be created in ordersout/Christian Schneider/order1.xml that contains the book item.
