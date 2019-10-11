---
title: Karaf Tutorial Part 2 - Using the Configuration Admin Service
---

In the first part of the Karaf Tutorial we learned how to use maven and blueprint to offer and use pojo services and how to use the http service to publish a servlet.

In this second part we concentrate on configuration for our OSGi bundles. In this tutorial we will cover ussing the [Configuration Admin Service](https://osgi.org/specification/osgi.cmpn/7.0.0/service.cm.html) with plain OSGi APIs and blueprint and how to automatically deploy config files with your bundles. The examples can be found on github [https://github.com/cschneider/Karaf-Tutorial/tree/master/configadmin]

## The Configuration Admin Service spec

We will first get a fast overview of the Configuration Admin Service spec. There two main interfaces for us to use:

- **ConfigurationAdmin** - Allows to retrieve and change configurations. This service is offered by the Config Admin Service implementation
- **ManagedService** - Allows to react on configuration changes. You have to implement this and register it as a service to get notified

So basically a configuration in the Config Admin Service is a Dictionary that contains attributes and their values. The Dictionary is identified by a persistent identifier (pid). This is simply a String that uniquely identifies the configuration.

## How to work with configuration?

While you can retrieve a configuration using the ConfigurationAdmin.getConfiguration interface I do not recommend to do so. OSGi is very dynamic so it may happen that your bundle starts before the config admin service or that the config admin service did not yet read the configuration. So you may end up sometimes getting Null for the configuration.

The recommended way is to use a ManagedService and react on updates. If your bundle can not start without config then it is a good idea to create the pojo class to be configured on the first update received.

## Introducing our very simple class to be configured

As we want to implement a clean style of how to work with configuration the class to be configured should be a pure pojo. While it is of course possible to simply implement the ManagedService interface and work with the Dictionary directly this will make you depend on OSGi and the current Config Admin Service spec. So instead we use a simple bean class that has a title property. Additionally I added a refresh method that should be called after all configuration was changed.

```
public class MyApp {

  String title;

  public void setTitle(String title) {
    this.title = title;
  }

  public void refresh() {
    System.out.println("Configuration updated (title=" + title + ")");
  }

}
```

So our goal is to configure the title when the configuration changes and then call refresh. We will do this in pure OSGi and in blueprint.

## Get some practice. Working with configs using pure OSGi interfaces

The first practical part in this tutorial shows how to use the config admin service using just OSGi interfaces. While this is probably not the way you will do it later it helps to understand what happens under the hood.

You can find the implementation in the [subdirectory configapp](https://github.com/cschneider/Karaf-Tutorial/tree/master/configadmin/configapp).
If you start fresh you will have to use the maven-bundle-plugin to make your project a OSGi bundle and you need to add two dependencies:

```
<dependency>
  <groupId>org.osgi</groupId>
  <artifactId>org.osgi.core</artifactId>
  <version>4.2.0</version>
</dependency>
<dependency>
  <groupId>org.osgi</groupId>
  <artifactId>org.osgi.compendium</artifactId>
  <version>4.2.0</version>
</dependency>
```

Now we will care about updating the MyApp class. The following little class does the trick. We implement the ManagedService interface to talk to the Config Admin Service. So we get called whenever the config changes. The first thing is to check for null as this can happen when the config is removed. We could at this point stop our MyApp but to keep it simple we just ignore those. The next step is to create the MyApp class. Normally you would do this in the Activator but then you would have to be able to work with an empty configuration which is not always desired. The last part is to simply call the setter with the value from the config and call refresh after all settings were made.

```
private final class ConfigUpdater implements ManagedService {

  public void updated(Dictionary config) throws ConfigurationException {
    if (config == null) {
      return;
    }

    if (app == null) {
      app = new MyApp();
    }

    app.setTitle((String)config.get("title"));
    app.refresh();
  }

}
```

Of course this does not yet do anything. The last step is to register the ConfigUpdater in the Activator.start. We simply use registerService like for every other service. The only special thing is that you have to set the property SERVICE_PID to your config pid so the Config Admin Service knows what config you want to watch.

```
Hashtable<String, Object> properties = new Hashtable<String, Object>();
properties.put(Constants.SERVICE_PID, CONFIG_PID);
serviceReg = context.registerService(ManagedService.class.getName(), new ConfigUpdater() , properties);
```

## Making this simple example run 

* build the project with mvn install.
* Start a fresh Karaf instance
* Copy the configapp.jar bundle from the target dir to the Karaf deploy dir

Now we notice that nothing seems to happen. Calling list in the Karaf console you should be able to see that the bundle is indeed started but it will not do create any output as there is no config.

We still need to create the config file and set the title.

* copy the existing file /configadmin-features/src/main/resources/ConfigApp.cfg to the /etc dir of the Karaf instance

The important part here is that the filename has to be <pid>.cfg. So the config admin service will find it.

Now the fileinstall bundle will detect the new file in etc. As the ending is .cfg it will consider it to be a config admin resource and create or update the Config Admin Service configuration with the pid determined from the file name.

So you should now see the following in the Karaf console. This shows that the configuration change was correctly detected and forwarded. If you now change the file using an editor and save the change will alsobe propagated.

```
Configuration updated (title=" + title + ")
```

## Digging into the config with the Karaf config commands

Type the following in the Karaf console:

```
> config:list

Pid: ConfigApp
BundleLocation: file:...
Properties:
   service.pid = ConfigApp
   felix.fileinstall.filename = file:...
   title = my Title
```

Among other configs you should find the above config "ConfigApp". The config shows where it has been loaded from, the pid and of course all properties we set in the file.

We can also change the config:

```
> config:edit ConfigApp
> config:property-set title "A better title"
> config:property-list

   service.pid = ConfigApp
   felix.fileinstall.filename = file:...
   title = A better title

> config:update
Configuration updated (title=A better title)
```

We see that the change is directly propagated to our bundle. If you look into the config file in etc you can see that the change is also persisted to the file. So the change will still be there if we restart Karaf.

## Configuration with Blueprint

After we have worked with the Config Admin Service in pure OSGi we will now look how the same can be achieved in Blueprint. Fortunately this is quite easy as Blueprint does most of the work for us.

We simply define a cm:property-placeholder element. This works similar to property place holder with files but works with the Config Admin Service. We need to supply the config PID and the update strategy.\

As strategy we select "reload". This means that after a change the blueprint context is reloaded to reflect the changes. We also set default properties that will be used when the config PID is not found or an attribute is not present.

The integration with our bean class is mostly a simple bean definition where we define the title property and assign the placeholder which will be resolved using the config admin service. The only special thing is the init-method. This is used to give us the chance to react after all changes were made like in the pure OSGi example.

For bluenprint we do not need any maven dependencies as our Java Code is a pure Java bean. The blueprint context is simply activated by putting it in the OSGI-INF/blueprint directory and by having the blueprint extender loaded. As blueprint is always loaded in Karaf we do not need anything else.

```
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0" xmlns:cm="http://aries.apache.org/blueprint/xmlns/blueprint-cm/v1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="
http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
http://aries.apache.org/blueprint/xmlns/blueprint-cm/v1.1.0] [http://svn.apache.org/repos/asf/aries/trunk/blueprint/blueprint-cm/src/main/resources/org/apache/aries/blueprint/compendium/cm/blueprint-cm-1.1.0.xsd
">

<cm:property-placeholder persistent-id="ConfigApp" update-strategy="reload" >
  <cm:default-properties>
    <cm:property name="title" value="Default Title"/>
  </cm:default-properties>
</cm:property-placeholder>

<bean id="myApp" init-method="refresh">
  <property name="title" value="$\{title\}"></property>
</bean>

</blueprint>
```

In the above xml please remove the backslashes around title. This is just to avoid confluence interpreting it as a wiki macro.

## Deploying config files

After we have successfully used the Config Admin Service the only thing that remains to go into production is to deploy our bundle together with a default configuration. This can be done using a Karaf feature file. We define our feature with the bundles it needs and simply add a configfile element. This makes Karaf deploy the given file into the etc directory of our Karaf installation. If the file is already present then it will not be overwritten.

```
<feature name="tutorial-configadmin" version="${pom.version}">
<bundle>mvn:net.lr.tutorial.configadmin/configapp/${pom.version}</bundle>
<bundle>mvn:net.lr.tutorial.configadmin/configapp-blueprint/${pom.version}</bundle>
<configfile finalname="/etc/ConfigApp.cfg">mvn:net.lr.tutorial.configadmin/configadmin-features/${pom.version}/cfg</configfile>
</feature>
```

So one last question is how to deploy the config to maven for the configfile element to find it. This happens like for the feature with the build-helper-maven-plugin in Karaf See the pom file for details how to use it.

## Summing it up

During this tutorial we have learned how the Config Admin Service works and how to use it with pure OSGi and blueprint. We have also seen how to build and deploy our projects together with documentation.
