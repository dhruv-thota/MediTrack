package com.airtribe.meditrack.entity;

/**
 * Entity representing a Patient, extending {@link Person}.
 * Demonstrates inheritance, constructor chaining (super/this), and method overriding.
 */
public class Patient extends Person {

    private String contactNumber;

    /**
     * Primary constructor demonstrating constructor chaining to superclass.
     *
     * @param id            Patient ID.
     * @param name          Patient name.
     * @param age           Patient age.
     * @param contactNumber Patient contact telephone/email.
     */
    public Patient(String id, String name, int age, String contactNumber) {
        super(id, name, age);
        this.contactNumber = contactNumber;
    }

    /**
     * Convenience constructor chaining to primary constructor.
     *
     * @param id   Patient ID.
     * @param name Patient name.
     * @param age  Patient age.
     */
    public Patient(String id, String name, int age) {
        this(id, name, age, "");
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
