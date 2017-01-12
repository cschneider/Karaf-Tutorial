SimpleCamel
===========

This example just contains a blueprint file with a camel context and a route.

Run in Karaf
------------

feature:repo-add camel 2.16.2
feature:list

feature:install camel-blueprint camel-stream

Copy the file simple-camel-blueprint.xml into the deploy folder of karaf

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
2017-01-12 20:30:27,045 | INFO  | - timer://simple | display                          | 42 - org.apache.camel.camel-core - 2.16.4 | Exchange[ExchangePattern: InOnly, BodyType: String, Body: Hello World]