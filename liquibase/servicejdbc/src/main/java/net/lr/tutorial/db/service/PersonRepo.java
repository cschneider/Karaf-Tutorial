package net.lr.tutorial.db.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service=PersonRepo.class)
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
