Some hints to boost your productivity with declarative services

The declarative services (DS) spec has some hidden gems that really help to make the most out of your application.

Use the DS spec annotations to define your component
----------------------------------------------------

Some older articles about DS define the components using xml. While this is still possible it is much simpler to use annotations for this purpose.  
There are 3 sets of annotations available bnd style, felix style and OSGi DS spec style. While the first two set can still be seen in the wild you  
should only use the OSGi spec annotations for new code as the other sets are deprecated.

At runtime DS only works with the xml so make sure your build creates xml descriptors from your annotated components. Recent versions of bnd, maven-bundle-plugin  
and bnd-maven-plugin all handle the spec DS annoations by default. So no additional settings are required.

Activate component by configuration
-----------------------------------

@Component(  
name = "mycomponent",  
immediate = true,  
configurationPolicy = ConfigurationPolicy.REQUIRE,  
)

In some cases it makes sense to always install a bundle but to be able to activate and deactivate a service it provides.  
By using configurationPolicy = REQUIRE the component is only activated if the configuration pid "myComponent" exists.  
Do not forget immediate=true as by defaullt the component would be lazy and thus not activate unless someone requires it.

Override service properties using config
----------------------------------------

By default a DS component is published as a service with all properties that are set in the @Component annotation.  
Every component is also configurable using a config pid that matches the component name. It is less well known that the  
configuration properties also show on the service properties and override the settings in the annotation.

One use case for this is to publish a componeent using Remote Service Admin that was not marked by the developer.  
Another use case is to override the topic a EventAdmin EventHandler listens on. See [https://github.com/apache/karaf-decanter/blob/master/appender/kafka/src/main/java/org/apache/karaf/decanter/appender/kafka/KafkaAppender.java#L43](https://github.com/apache/karaf-decanter/blob/master/appender/kafka/src/main/java/org/apache/karaf/decanter/appender/kafka/KafkaAppender.java#L43)

Overide injected services of a component using config
-----------------------------------------------------

If a component is injected with a service using @Reference then the service is normally statically filtered using the target property of the annotation in the  
form of an ldap filter.  
This filter can be overridden using a config property target.refname where refname is the name of the property the service is injected into.

Create multiple instances of a component using config
-----------------------------------------------------

Another not so well known fact is that a DS component not only reacts on a single configuation pid but also on factory configs. If the pid of your component config is "myconfig" then in apache karaf you can create configs named myconfig-1.cfg and myconfig-2.cfg and DS will create two instances of your component.

Typesafe configuration and Metatype information
-----------------------------------------------

Starting with DS 1.3 you can define type safe configs and also have them available as meta type information for config UIs.

@ObjectClassDefinition(name = "Server Configuration")
@interface ServerConfig {
  String host() default "0.0.0.0";
  int port() default 8080;
  boolean enableSSL() default false;
}


@Component
@Designate(ocd = ServerConfig.class)
public class ServerComponent {

  @Activate
  public void activate(ServerConfig cfg) {
    ServerSocket sock = new ServerSocket();
    sock.bind(new InetSocketAddress(cfg.host(), cfg.port()));
    // ...
  }

}

See [Neil Bartletts post](http://njbartlett.name/2015/08/17/osgir6-declarative-services.html) for the details.

Internal wiring
---------------

In DS every component publishes a service. So compared to blueprint DS seems to miss a feature for creating internal components / beans that are only visible inside the bundle.  
This can be achieved by putting a component into a private package and setting the service property to the class of the component. The component is still exported as a service  
but the service will not be visible to the outside as the package is private. Still the service can be injected into other classes of the bundle using the component class.

Field injection and constructor injection
-----------------------------------------

Since DS 1.3 (part of the OSGi 6 specs) you can also inject services directly into a field like:

@Reference
EventAdmin eventAdmin;

You can even inject into a private field but remember this will make it very difficult to write a unit test for your component. I personally always use package visibility for  
fields I inject stuff into. I then put the unit test into the same package and can set the field inside the test without doing any special magic.

Constructor injection is not possible at the time of writing this article but it is part of DS 1.4 (part of the OSGi spec 7). The implementation of this spec is currently on the way at felix scr.

Injecting multiple matching services into a List<MyService>
-----------------------------------------------------------

Since DS 1.3 it is possible to inject all services matching the interface and an optional filter into a List

@Reference
List<MyService> myservices;

By default DS assume the static policy. This means that whenever the list of services changes the component is deactivated and activated again. While this is the safest way it might be too slow for your use case.  
So injecting services dynamically can make sense.

Injecting services dynamically
------------------------------

By default DS will restart your component on reference changes. If this is too slow in your case you can allow DS to dynamically change the injected service(s).

@Reference
volatile MyService myService;
