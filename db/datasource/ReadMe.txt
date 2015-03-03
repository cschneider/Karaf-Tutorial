h1. Installing DataSources for databases

As OSGi is modular we want to be quite flexible in the kind of DB we use. For Java EE this is typically done by configuring a DataSource
as a JNDI object and then referencing this JNDI object from your application.

In Karaf we first need to install the DB driver. Most drivers are already valid bundles and available in the maven repo. So this is tpyically only
one Karaf command. Where the driver is available in maven but no bundle we can most times use the wrap: protocol of Karaf. If the driver is not even
in the maven repo we have to install the file into the maven repo first.

When the driver bundle is loaded we need to define a DataSource and publish it as an OSGi service. As we can deploy simple blueprint xml files in Karaf
this is really easy. We define a bean with the class of the specific datasource impl and configure it. Then we define an OSGi service to publish that
bean as a javax.sql.DataSource. This works because Karaf uses dynamic imports when it deploys blueprint context files so all classes are available. 

For you convenience I have prepared one blueprint file for each database flavour. It also explains how to install the specific driver.

h2. Test

> list

The table should show your datasource "bundles" as "Active" and "Created". If not check the log with:

> log:display


After the datasource is installed you can use the db-commands to check if it works.

h1. Deprecation

Using blueprint to declare a DataSource is still possible but is regarded as deprecated now.
Instead use pax-jdbc-config to create a DataSource from config.
