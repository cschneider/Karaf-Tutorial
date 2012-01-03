SimpleCamel
===========

This example just contains a blueprint file with a camel context and a route.

Run in Karaf
------------

features:addurl mvn:org.apache.camel.karaf/apache-camel/2.8.2/xml/features
features:install camel-blueprint camel-stream

Copy the file simplecamel.xml into the deploy folder of karaf

> list

Should show something like that:
[ 168] [Active     ] [Created     ] [       ] [   60] simplecamel.xml (0.0.0)

You will see "Hello World" printed to the karaf console every 5 seconds.

Test
----

As karaf listens to changes to the files in the deploy folder you can change the file and save to update it in karaf.
Open the file simplecamel.xml in the deploy folder using a text editor.

Change "stream:out" to "log:simplecamel" and save the file.

Now the the console should not show any more Hello World and instead the lines should go to the karaf log.

> log:display

Should show lines like:
2011-12-29 16:26:48,104 | INFO | simple | simplecamel | ache.camel.processor.CamelLogger   87 | 84 - org.apache.camel.camel-core - 2.8.2 | Exchange[ExchangePattern:InOnly, BodyType:String, Body:Hello World]

