package net.lr.tutorial.db.migration;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ops4j.pax.jdbc.hook.PreHook;
import org.osgi.service.component.annotations.Component;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

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
