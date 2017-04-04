# Karaf Tutorial 11 - Managing database schema with liquibase

A typical pain point in continuous delivery is the database schema and contents. The database schema changes over time and can not be deployed like bundles. Sometimes even the database contents have to be adapted when the schema changes. In a modern deployment pipeline we of course also want to automate this part of the deployment. This tutorial builds upon the db tutorial. We assume familiarity with how to create DataSources using pax-jdbc-config and declarative services.

## Declare schema using liquibase changesets

Liquibase manages a database schema over time using changesets. A changeset is created at least for every release that needs to change the schema.
In our example the first changeset creates a simple table and populates it with a record.

    <changeSet id="1" author="cs">
      <createTable tableName="person">
        <column name="id" type="bigint" autoIncrement="true">
          <constraints primaryKey="true" nullable="false"  />
        </column>
        <column name="name" type="varchar(255)" />
      </createTable>
      <insert tableName="person">
        <column name="id">1</column>
        <column name="name">Chris</column>
      </insert>
    </changeSet>

The changeset can be stored in different places. For me the schema is closely related to the application code. So it makes sense to store it inside a bundle. In the example the changesets can be found in migrator/src/main/resources/db/changesets.xml.

## Applying the changeset

Liquibase provides many ways to apply the schema. It can be done programmatically, as a servlet filter, from spring or from maven. In many cases it makes sense to apply the schema changes before the application starts. So when the user code starts it knows that the schema is in the correct state.

In case the application has no db admin rights liquibase can also create a SQL script tailored to the database that an administrator can apply. While this is necessary in some settings it breaks the idea of fully unattended deployments.

In our example we want to apply the schema to the DataSource that is given to our application and we want to make sure that no user code can work on the DataSource before the schema is updated.

We create the DataSource from an OSGi config using pax-jdbc-config. Luckily pax-jdbc-config 1.1.0 now supports a feature called PreHook. This allows to define code that runs on the DataSource before it is published as a service.

## Using PreHook to apply the database changes

To register a PreHook we implement the PreHook interface and publish our implementation as an OSGi service. We also give it a name using the service property "name".

Our PreHook to do the Liquibase schema update looks like this:

    @Component(property="name=persondb")
    public class Migrator implements PreHook {

        @Override
        public void prepare(DataSource ds) throws SQLException {
            try (Connection connection = ds.getConnection()) {
                prepare(connection);
            } catch (LiquibaseException e) {
                throw new RuntimeException(e);
            }
        }

        private void prepare(Connection connection) throws DatabaseException, LiquibaseException {
            DatabaseConnection databaseConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(databaseConnection);
            ClassLoader classLoader = this.getClass().getClassLoader();
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(classLoader);
            Liquibase liquibase = new Liquibase("db/changesets.xml", resourceAccessor, database);
            liquibase.update(new Contexts());
        }

    }

By itself this service would not be called. We also need to reference it in our DataSource config using the property "ops4j.preHook":

    osgi.jdbc.driver.name=H2
    url=jdbc:h2:data/person
    dataSourceName=person
    ops4j.preHook=persondb

## Accessing the DB

The PersonRepo class uses simple jdbc4 code to query the person table and return a List of Person objects. Person is a imply bean with id and name properties.

    @Component(service = PersonRepo.class)
    public class PersonRepo {
        @Reference
        DataSource ds;

        public List<Person> list() {
            try (Connection con = ds.getConnection()) {
                ArrayList<Person> persons = new ArrayList<Person>();
                ResultSet rs = con.createStatement().executeQuery("select id, name from person");
                while (rs.next()) {
                    persons.add(read(rs));
                }
                return persons;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        private Person read(ResultSet rs) throws SQLException {
            Person person = new Person();
            person.setId(rs.getInt("id"));
            person.setName(rs.getString("name"));
            return person;
        }

    }

## Testing the code

    mvn clean install

Download and start Apache Karaf 4.1.1. Then install the example-lb feature

    cat https://raw.githubusercontent.com/cschneider/Karaf-Tutorial/master/liquibase/org.ops4j.datasource-person.cfg | tac etc/org.ops4j.datasource-person.cfg
    feature:repo-add mvn:net.lr.tutorial.lb/lb/1.0.0-SNAPSHOT/xml/features
    feature:install example-lb jdbc

    jdbc:tables person

This shows the list of tables for the DataSource person. In our case it should contain a table person with the columns id and name.

    person:list
  
This should display one person named Chris with id 1. The schema as well as the data was created using liquibase.

## Introducing a new column

Now a typical case is that we want to add a new column to a table in the next release of the software. We will do this in  code and schema step by step.

After our first test run with the old code the database will exist in the old state. So we of course want all data be preserved when we update to the new version. 

### Add a new changeset to liquibase

We add the new changeset to the file net.lr.tutorial.lb.migrator/src/main/resources/db/changesets.xml

    <changeSet id="2" author="cs">
      <addColumn tableName="person">
        <column name="age" type="int" defaultValue="42"/>
      </addColumn>
    </changeSet>

When liquibase updates the database it will see that the current state does not include the new changeset and apply it.
So all old data should still be present and the person table should have a new column age with all ages of persons set to the default value 42.

### Use the new column in the code

The Person model object is already prepared for the new property to keep things simple.

So we only need to adapt the PersonRepo. We add age to the select:

    select id, name, age from person

and also make sure we read the age from the resultset and store it in the person record:

    person.age = rs.getInt("age");

Note that this code will break if there is no age column. So this will show that the new column is  applied correctly.

## Test the new code

    mvn clean install

In karaf we need to readd the feature repo and install the featuere again so karaf updates to the new code it.

    update 

First we do a quick check to see the column is actually added

    jdbc:tables person

The person table should now have three columns id, name and age

     person:list

The person Chris should name have the default age of 42.
