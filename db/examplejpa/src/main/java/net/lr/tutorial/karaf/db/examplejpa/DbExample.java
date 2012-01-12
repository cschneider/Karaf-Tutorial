package net.lr.tutorial.karaf.db.examplejpa;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.sql.DataSource;

public class DbExample {
    DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void test() throws Exception {
        Connection con = dataSource.getConnection();
        Statement stmt = null;
        DatabaseMetaData dbMeta = con.getMetaData();
        System.out.println("Using datasource " + dbMeta.getDatabaseProductName() + " ver " + dbMeta.getURL());
        try {
            stmt = con.createStatement();
            try {
                stmt.execute("drop table person");
            } catch (Exception e) {
                // Ignore as it will fail the first time
            }
            stmt.execute("create table person (name varchar(100), twittername varchar(100))");
            stmt.execute("insert into person (name, twittername) values ('Christian Schneider', '@schneider_chris')");
            ResultSet rs = stmt.executeQuery("select * from person");
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                for (int c = 1; c <= meta.getColumnCount(); c++) {
                    System.out.print(rs.getString(c) + ", ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

}
