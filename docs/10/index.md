---
title: Apache Karaf Tutorial part 10 - Declarative services
---

This tutorial shows how to use Declarative Services together with the new Aries JPA 2.0.You can find the full source code on [github Karaf-Tutorial/tasklist-ds](https://github.com/cschneider/Karaf-Tutorial/tree/master/tasklist-ds)

Declarative Services
--------------------

Declarative Services (DS) is the biggest contender to blueprint. It is a slim service injection framework that is completely focused on OSGi. DS allows you to offer and consume OSGi services and to work with configurations.

At the core DS works with xml files to define scr components and their dependencies. They typically live in the OSGI-INF directory and are announced in the Manifest using the header "Service-Component" with the path to the component descriptor file. Luckily it is not necessary to directly work with this xml as there is also support for DS annotations. These are processed by the maven-bundle-plugin. The only prerequisite is that they have to be enabled by a setting in the configuration instructions of the plugin.

<\_dsannotations>*</\_dsannotations>

For more details see [http://www.aqute.biz/Bnd/Components](http://www.aqute.biz/Bnd/Components)

DS vs Blueprint
---------------

Let us look into DS by comparing it to the already better known blueprint. There are some important differences:

1.  Blueprint always works on a complete blueprint context. So the context will be started when all mandatory service deps are present. It then publishes all offered services. As a consequence a blueprint context can not depend on services it offers itself. DS works on Components. A component is a class that offers a service and can depend on other services and configuration. In DS you can manage each component separately like start and stop it. It is also possible that a bundle offers two components but only one is started as the dependencies of the other are not yet there.
2.  DS supports the OSGi service dynamics better than blueprint. Lets look into a simple example:  
    You have a DS and blueprint module component that offers a service A and depends on a mandatory service B. Blueprint will wait on the first start for the mandatory service to be available. If it does not come up it will fail after a timeout and will not be able to recover from this. Once the blueprint context is up it stays up even if the mandatory service goes away. This is called service damping and has the goal to avoid restarting blueprint contexts too often. Services are injected into blueprint beans as dynamic proxies. Internally the proxy handles the replacement and unavailability of services. One problem this causes is that calls to a non available service will block the thread until a timeout and then throw a RuntimeException.  
    In DS on the other hand a component lifecycle is directly bound to dependent services. So a component will only be activated when all mandatory services are present and deactivated as soon as one goes away. The advantage is that the service injected into the component does not have to be proxied and calls to it should always work.
3.  Every DS component must be a service. While blueprint can have internal beans that are just there to wire internal classes to each other this is not possible in DS. So DS is not a complete dependency injection framework and lacks many of the features blueprint offers in this regard.
4.  DS does not support extension namespaces. Aries blueprint has support for quite a few other Apache projects using extension namespaces. Examples are: Aries jpa, Aries transactions, Aries authz, CXF, Camel. So using these technologies in DS can be a bit more difficult.
5.  DS does not support support interceptors. In blueprint an extension namespace can introduce and interceptor that is always called before or after a bean. This is for example used for security as well as transation handling. For this reason DS did not support JPA very well as normal usage mandates to have interceptors. See below how jpa can work on DS.

So if DS is a good match for your project depends on how much you need the service dynamics and how well you can integrate DS with other projects.

JEE and JPA
-----------

The JPA spec is based on JEE which has a very special thread and interceptor model. In JEE you use session beans with a container managed EntityManger  
to manipulate JPA Entities. It looks like this:

**JPA**

@Stateless
class TaskServiceImpl implements TaskService {

  @PersistenceContext(unitName="tasklist")
  private EntityManager em;

  public Task getTask(Integer id) {
    return em.find(Task.class, id);
  }
}

In JEE calling getTask will by default participate in or start a transaction. If the method call succeeds the transaction will be committed, if there is an exception it will be rolled back.  
The calls go to a pool of TaskServiceImpl instances. Each of these instances will only be used by one thread at a time. As a result of this the EntityManager interface is not thread safe!

So the advantage of this model is that it looks simple and allows pretty small code. On the other hand it is a bit difficult to test such code outside a container as you have to mimic the way the container works with this class. It is also difficult to access e.g. em  
as it is private and there is not setter.

Blueprint supports a coding style similar to the JEE example and implements this using a special jpa and tx namespace and  
interceptors that handle the transaction / em management.

DS and JPA
----------

In DS each component is a singleton. So there is only one instance of it that needs to cope with multi threaded access. So working with the plain JEE concepts for JPA is not possible in DS.

Of course it would be possible to inject an EntityManagerFactory and handle the EntityManager lifecycle and transactions by hand but this results in quite verbose and error prone code.

Aries JPA 2.0.0 is the first version that offers special support for frameworks like DS that do not offer interceptors. The solution here is the concept of a JPATemplate together with support for closures in Java 8. To see how the code looks like peek below at chapter persistence.

Instead of the EntityManager we inject a thread safe JpaTemplate into our code. We need to put the jpa code inside a closure and run it with jpa.txEpr() or jpa.tx(). The JPATemplate will then guarantee the same environment like JEE inside the closure. As each closure runs as its own  
instance there is one em per thread. The code will also participate/create a transaction and the transaction commit/rollback also works like in JEE.

So this requires a little more code but the advantage is that there is no need for a special framework integration.  
The code can also be tested much easier. See [TaskServiceImplTest](https://github.com/cschneider/Karaf-Tutorial/blob/master/tasklist-ds/persistence/src/test/java/net/lr/tasklist/persistence/impl/TaskServiceImplTest.java) in the example.

Structure
---------

*   features
*   model
*   persistence
*   ui

Features
--------

Defines the karaf features to install the example as well as all necessary dependencies.

Model
-----

This module defines the Task JPA entity, a TaskService interface and the persistence.xml. For a detailed description of model see the tasklist-blueprint example. The model is exactly the same here.

Persistence
-----------

**TaskServiceImpl**

@Component
public class TaskServiceImpl implements TaskService {

    private JpaTemplate jpa;

    public Task getTask(Integer id) {
        return jpa.txExpr(em -> em.find(Task.class, id));
    }

    @Reference(target = "(osgi.unit.name=tasklist)")
    public void setJpa(JpaTemplate jpa) {
        this.jpa = jpa;
    }
}

We define that we need an OSGi service with interface TaskService and a property "osgi.unit.name" with the value "tasklist".

**InitHelper**

@Component
public class InitHelper {
    Logger LOG = LoggerFactory.getLogger(InitHelper.class);
    TaskService taskService;

    @Activate
    public void addDemoTasks() {
        try {
            Task task = new Task(1, "Just a sample task", "Some more info");
            taskService.addTask(task);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }
    @Reference
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}

The class InitHelper creates and persists a first task so the UI has something to show. It is also an example how business code that works with the task service can look like.  
@Reference TaskService taskService injects the TaskService into the field taskService.  
@Activate makes sure that addDemoTasks() is called after injection of this component.  

Another interesting point in the module is the test [TaskServiceImplTest](https://github.com/cschneider/Karaf-Tutorial/blob/master/tasklist-ds/persistence/src/test/java/net/lr/tasklist/persistence/impl/TaskServiceImplTest.java). It runs outside OSGi and uses a special  
persistence.xml for testing to create the EntityManagerFactory. It also shows how to instantiate a ResourceLocalJpaTemplate  
to avoid having to install a JTA transaction manager for the test. The test code shows that indeed the TaskServiceImpl can  
be used as plain java code without any special tricks.

UI
--

The tasklist-ui module uses the TaskService as an OSGi service and publishes a Servlet as an OSGi service. The Pax-web whiteboard bundle will then pick up the exported servlet and publish it using the HttpService so it is available on http.

**TaskListServlet**

@Component(immediate = true,
service = { Servlet.class },
property = { "alias:String=/tasklist" }
)
public class TaskListServlet extends HttpServlet {
    private TaskService taskService;
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException {
        // Actual code omitted
    }

    @Reference
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}

The above snippet shows how to specify which interface to use when exporting a service as well as how to define service properties.

The TaskListServlet is exported with the interface javax.servlet.Servlet with the service property alias="/tasklist".  
So it is available on the url [http://localhost:8181/tasklist](http://localhost:8181/tasklist).

Build
-----

Make sure you use JDK 8 and run:

mvn clean install

Installation
------------

Make sure you use JDK 8.  
Download and extract Karaf 4.0.0.  
Start karaf and execute the commands below  


**Create DataSource config and Install Example**

cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/tasklist-blueprint-cdi/org.ops4j.datasource-tasklist.cfg | tac -f etc/org.ops4j.datasource-tasklist.cfg
feature:repo-add mvn:net.lr.tasklist.ds/tasklist/1.0.0-SNAPSHOT/xml/features
feature:install example-tasklist-ds-persistence example-tasklist-ds-ui

Validate Installation
---------------------

First we check that the JpaTemplate service is present for our persistence unit.

service:list JpaTemplate

\[org.apache.aries.jpa.template.JpaTemplate\]
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-
 osgi.unit.name = tasklist
 transaction.type = JTA
 service.id = 164
 service.bundleid = 57
 service.scope = singleton
Provided by :
 tasklist-model (57)
Used by:
 tasklist-persistence (58)

Aries JPA should have created this service for us from our model bundle. If this did not work then check the log for messages from Aries JPA. It should print what it tried and what it is waiting for. You can also check for the presence of an EntityManagerFactory and EmSupplier service which are used by JpaTemplate.

A likely problem would be that the DataSource is missing so lets also check it:

service:list DataSource

\[javax.sql.DataSource\]
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-
 dataSourceName = tasklist
 felix.fileinstall.filename = file:/home/cschneider/java/apache-karaf-4.0.0/etc/org.ops4j.datasource-tasklist.cfg
 osgi.jdbc.driver.name = H2-pool-xa
 osgi.jndi.service.name = tasklist
 service.factoryPid = org.ops4j.datasource
 service.pid = org.ops4j.datasource.cdc87e75-f024-4b8c-a318-687ff83257cf
 url = jdbc:h2:mem:test
 service.id = 156
 service.bundleid = 113
 service.scope = singleton
Provided by :
 OPS4J Pax JDBC Config (113)
Used by:
 Apache Aries JPA container (62)

This is like it should look like. Pax-jdbc-config created the DataSource out of the configuration in "etc/org.ops4j.datasource-tasklist.cfg". By using a DataSourceFactory wit the property "osgi.jdbc.driver.name=H2-pool-xa". So the resulting DataSource should be pooled and fully ready for XA transactions.

Next we check that the DS components started:

scr:list

ID | State  | Component Name
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-
1  | ACTIVE | net.lr.tasklist.persistence.impl.InitHelper
2  | ACTIVE | net.lr.tasklist.persistence.impl.TaskServiceImpl
3  | ACTIVE | net.lr.tasklist.ui.TaskListServlet

If any of the components is not active you can inspect it in detail like this:

scr:details net.lr.tasklist.persistence.impl.TaskServiceImpl

Component Details
  Name                : net.lr.tasklist.persistence.impl.TaskServiceImpl
  State               : ACTIVE
  Properties          :
    component.name=net.lr.tasklist.persistence.impl.TaskServiceImpl
    component.id=2
    Jpa.target=(osgi.unit.name=tasklist)
References
  Reference           : Jpa
    State             : satisfied
    Multiple          : single
    Optional          : mandatory
    Policy            : static
    Service Reference : Bound Service ID 164

Test
----

Open the url below in your browser.  
[http://localhost:8181/tasklist](http://localhost:8181/tasklist)

You should see a list of one task

[http://localhost:8181/tasklist?add&taskId=2&title=Another Task](http://localhost:8181/tasklist?add&taskId=2&title=Another Task)M
