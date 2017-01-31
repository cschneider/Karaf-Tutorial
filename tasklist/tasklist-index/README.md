# Tasklist example bndtools style

Bndtools is an eclipse plugin that allows to work with OSGi in a very convenient way. Traditionally bndtools has a very distinguished project setup and build that is largely incompatible with maven and apache karaf.

From bndtools 3.3.0 there is support for maven builds too. This allowed to enhance the tasklist example to also be run from bndtools and be packaged into a runnable jar using bnd.

# Deployment in bnd / bndtools

In karaf deployment is defined using features and optionally a karaf custom distro. In bnd deployments are defined using one or more OBR indexes and a bndrun file. This is quite different to the way karaf features work.

## OBR indexes

The OBR index is defined using a pom file with dependencies to all bundles that could potentially be used. So this index may very well contain a bit more bundles than are needed for the actual deployments. This is especially helpful as some projects like Aries RSA started to create repository pom files that can be added to the use index pom and draw in all bundles of the project.

In this example the OBR is created automatically when doing a maven install build.

## bndrun files 

The actual deployment is defined using a bndrun file. This defines several things:
* OBR indexes to use
* OSGi framework to start
* Framework properties
* Requirements for the resolver to work on

The most interesting thing here are the requirements. Instead of defining each bundle to start the requirements just define some few top level bundles like the bundles of the user code to deploy. Each bundle defines inside its Manifest which packages, OSGi services and other arbitrary requirements it needs. The OSGi resolver calculates a closure of bundles that fulfill the requirements.

## Running from bndtools

After installing bndtools in eclipse the bndrun files are opened using a special editor. This allows to define the requirements, do a resolve and also to run or debug the application.

So the tasklist example can be run by opening the tasklist-index/tasklist.bndrun file and clicking on "run".
This also starts a gogo shell that allows to run the basic OSGi commands as well as the taks specific commands. 

## Packaging as self running jar

The maven build also runs the bnd-export-plugin which resolves the bundles and creates a runnable jar.

This jar can be run using:

java -jar target/tasklist.jar

