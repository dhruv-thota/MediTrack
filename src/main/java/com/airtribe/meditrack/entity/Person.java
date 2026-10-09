package com.airtribe.meditrack.entity;

/**
 * Abstract base class representing a Person in the MediTrack system.
 * Demonstrates encapsulation, abstraction, and inheritance base.
 */
public abstract class Person {

    protected String id;
    protected String name;
    protected int age;

    /**
     * Primary constructor demonstrating constructor initialization.
     *
     * @param id   The unique person identifier.
     * @param name The person's full name.
     * @param age  The person's age.
     */
    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /**
     * Default constructor demonstrating constructor chaining.
     */
    public Person() {
        this("", "", 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Abstract method to be overridden by subclasses to return their specific domain role.
     *
     * @return Role description string.
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
