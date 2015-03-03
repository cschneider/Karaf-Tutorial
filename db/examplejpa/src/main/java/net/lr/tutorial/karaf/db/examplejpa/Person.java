package net.lr.tutorial.karaf.db.examplejpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    private String name;
    private String twitterName;
    
    public Person() {
    }

    public Person(String name, String twitterName) {
        super();
        this.name = name;
        this.twitterName = twitterName;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTwitterName() {
        return twitterName;
    }
    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }
    
}
