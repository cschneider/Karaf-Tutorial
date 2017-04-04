package net.lr.tutorial.db.service;

/**
 * Very simple model class. This already contains the age property 
 * to make the tutorial steps simpler.
 */
public class Person {

    public long id;
    public String name;
    public int age;

    @Override
    public String toString() {
        return id + ":" + name + ":" + age;
    }
}
