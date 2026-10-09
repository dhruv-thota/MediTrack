package com.airtribe.meditrack.entity;

/**
 * Entity representing a Doctor, extending {@link Person}.
 * Demonstrates inheritance, constructor chaining, polymorphism, and enum usage.
 */
public class Doctor extends Person {

    private Specialization specialization;

    /**
     * Primary constructor chaining to superclass.
     *
     * @param id             Doctor ID.
     * @param name           Doctor name.
     * @param age            Doctor age.
     * @param specialization Doctor medical specialization.
     */
    public Doctor(String id, String name, int age, Specialization specialization) {
        super(id, name, age);
        this.specialization = specialization;
    }

    /**
     * Overloaded constructor chaining to primary constructor with default age.
     *
     * @param id             Doctor ID.
     * @param name           Doctor name.
     * @param specialization Doctor medical specialization.
     */
    public Doctor(String id, String name, Specialization specialization) {
        this(id, name, 0, specialization);
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", specialization=" + specialization +
                '}';
    }
}
