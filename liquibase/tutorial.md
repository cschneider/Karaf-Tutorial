# Karaf Tutorial 11 - Managing database schema with liquibase

One typical pain point in deployments is the database schema and contents. The database schema changes over time and can not be deployed like bundles. Sometimes even the database contents have to be adapted when the schema changes. In a mordern deployment pipeline we of course also want to automate this part of the deployment. This tutorial builds upon the db tutorial. We assume familiarity with how to create DataSources using pax-jdbc-config and declarative services.

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

## Applying the changeset

Liquibase provides many ways to apply the schema. It can be done programmatically, as a servlet filter, from spring or from maven. 
In many cases it makes sense to apply the schema changes before the application starts. So when the user code starts it knowns that the schema is in the correct state.

In case the application has no rights the change the schema liquibase can also create a SQL script tailored to the database that an administrator can apply. While this is necessary in some settings it breaks the idea of fully unattended deployments.

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

By itself this service would not be used. We also need to reference it in our DataSource config using the property "ops4j.preHook":

    osgi.jdbc.driver.name=H2
    url=jdbc:h2:mem:person;DB_CLOSE_DELAY=-1
    dataSourceName=person
    ops4j.preHook=persondb

When using H2 with a memory db it is very important to use DB_CLOSE_DELAY=1. Without it the db would be deleted after the connection is closed. So liquibase would update the schema and the db would be deleted deirectly afterwards. So when the user code later accesses the DB it would be empty again.

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

    
## Introducing a new column

