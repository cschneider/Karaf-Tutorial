h1. Tutorial 2: Using the config admin service

This tutorial will show how to use the config admin service in general as well as the special features Karaf provides for it.
Please also read the tutorial article on my blog:


h2. Content

- app: A little application that uses the config admin service to read config and be notified of config changes
- app-blueprint: The same as above but now using blueprint to abstract away from the details of the config admin service

h2. Build

mvn clean install

h2. Installation

There is only an automated installation for the blueprint case.

features:addurl mvn:net.lr.tutorial.configadmin/configadmin-features/1.0/xml
features:install  tutorial-configadmin
