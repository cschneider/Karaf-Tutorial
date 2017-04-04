package net.lr.tutorial.db.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Read data from person table in the db. The commented out parts have
 * to be enabled to implement the new property age
 */
@Component(service = PersonRepo.class)
public class PersonRepo {
    private static final String QUERY = "select id, name from person";
    //private static final String QUERY = "select id, name, age from person";

    @Reference
    DataSource ds;

    public List<Person> list() {
        try (Connection con = ds.getConnection()) {
            ArrayList<Person> persons = new ArrayList<Person>();
            ResultSet rs = con.createStatement().executeQuery(QUERY);
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
        person.id = rs.getInt("id");
        person.name = rs.getString("name");
        //person.age = rs.getInt("age");
        return person;
    }

}
