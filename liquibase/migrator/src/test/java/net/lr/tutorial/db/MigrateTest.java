package net.lr.tutorial.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.Test;

import net.lr.tutorial.db.migration.Migrator;


public class MigrateTest {

    @Test
    public void testMigrate() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:person;DB_CLOSE_DELAY=-1");
        new Migrator().prepare(ds);
        try (Connection con = ds.getConnection()) {
            testQuery(con);
        }
    }

    private void testQuery(Connection con) throws SQLException {
        try (ResultSet rs = con.createStatement().executeQuery("select id, name, age from person")) {
            rs.next();
            Assert.assertEquals(1, rs.getInt(1));
            Assert.assertEquals("Chris", rs.getString(2));
            Assert.assertEquals(29, rs.getInt(3));
        }
    }
}
