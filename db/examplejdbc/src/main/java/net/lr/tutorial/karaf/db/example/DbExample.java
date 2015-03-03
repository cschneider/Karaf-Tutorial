/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.lr.tutorial.karaf.db.example;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DbExample {

    DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void test() throws Exception {
        try (
            Connection con = dataSource.getConnection();
            Statement stmt = con.createStatement()
            ) {
            writeInfos(con);
            dropTable(con);
            stmt.execute("create table person (name varchar(100), twittername varchar(100))");
            stmt.execute("insert into person (name, twittername) values ('Christian Schneider', '@schneider_chris')");
            ResultSet rs = stmt.executeQuery("select * from person");
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                writeResult(rs, meta.getColumnCount());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private void writeInfos(Connection con) throws SQLException {
        DatabaseMetaData dbMeta = con.getMetaData();
        System.out.println("Using datasource " + dbMeta.getDatabaseProductName() + ", URL " + dbMeta.getURL());
    }

    private void dropTable(Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("drop table person");
        }
        catch (Exception e) {
            // Ignore as it will fail the first time
        }
    }

    private void writeResult(ResultSet rs, int columnCount) throws SQLException {
        for (int c = 1; c <= columnCount; c++) {
            System.out.print(rs.getString(c) + ", ");
        }
        System.out.println();
    }

}
