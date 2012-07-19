package net.lr.tutorial.karaf.camel.jpa2jms.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlType
@XmlRootElement
public class Person {
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
