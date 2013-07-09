Karaf Tutorial Part 8 - Distributed OSGi
----------------------------------------

The DOSGi example simply uses the artifacts of the tasklist example. So there is no separate source code.
We will use two containers. Container_a will contain model and persistence, container_b will contain model and UI.
By also installing the DOSGi runtime we will enable the UI to talk to the persistence services that resides in a different container.

Installing the service
----------------------

To keep things simple we will install container A and B on the same system.

Download Apache Karaf 2.2.10
Unpack into folder container_a
Copy etc/jre.properties.cxf into etc/jre.properties
Start bin/karaf

config:propset -p org.apache.cxf.dosgi.discovery.zookeeper zookeeper.port 2181
config:propset -p org.apache.cxf.dosgi.discovery.zookeeper.server clientPort 2181
features:chooseurl cxf-dosgi 1.5.0
features:install cxf-dosgi-discovery-distributed cxf-dosgi-zookeeper-server
features:addurl mvn:net.lr.tasklist/tasklist-features/1.0.0-SNAPSHOT/xml
features:install example-tasklist-persistence

After these commands the tasklist persistence service should be running and be published on zookeeper.

You can check the wsdl of the exported service http://localhost:8181/cxf/net/lr/tasklist/model/TaskService?wsdl
By starting the zookeeper client zkCli.sh from a zookeeper distro you can optionally check that there is a node for the service below the osgi path.

Installing the UI
-----------------

Unpack into folder container_b
Copy etc/jre.properties.cxf into etc/jre.properties
Start bin/karaf

config:propset -p org.ops4j.pax.web org.osgi.service.http.port 8182
config:propset -p org.apache.cxf.dosgi.discovery.zookeeper zookeeper.port 2181
features:chooseurl cxf-dosgi 1.5.0
features:install cxf-dosgi-discovery-distributed
features:addurl mvn:net.lr.tasklist/tasklist-features/1.0.0-SNAPSHOT/xml
features:install example-tasklist-ui

The tasklist client ui should be in status Active/Created and the servlet should be available on http://localhost:8182/tasklist. If the ui bundle stays in status graceperiod then DOSGi did not provide a local proxy for the persistence service.

