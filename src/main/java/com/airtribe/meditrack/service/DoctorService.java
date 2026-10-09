package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Service managing Doctor domain operations including registration, retrieval,
 * updates, deletion, and overloaded searching.
 */
public class DoctorService implements Searchable<Doctor> {

    private final DataStore<Doctor> doctorStore;

    public DoctorService() {
        this.doctorStore = new DataStore<>(Doctor::getId);
    }

    /**
     * Registers a new doctor with age after validation and ID generation.
     *
     * @param name           Doctor name.
     * @param age            Doctor age.
     * @param specialization Doctor medical specialization.
     * @return Registered Doctor instance.
     * @throws InvalidDataException if validation fails.
     */
    public Doctor registerDoctor(String name, int age, Specialization specialization) throws InvalidDataException {
        Validator.validateDoctorData(name, specialization);
        String id = IdGenerator.generateDoctorId();
        Doctor doctor = new Doctor(id, name, age, specialization);

        boolean saved = doctorStore.save(doctor);
        if (!saved) {
            throw new InvalidDataException("Failed to register doctor with ID " + id);
        }
        return doctor;
    }

    /**
     * Convenience method to register a doctor without specifying age.
     *
     * @param name           Doctor name.
     * @param specialization Doctor medical specialization.
     * @return Registered Doctor instance.
     * @throws InvalidDataException if validation fails.
     */
    public Doctor registerDoctor(String name, Specialization specialization) throws InvalidDataException {
        return registerDoctor(name, 0, specialization);
    }

    /**
     * Retrieves a doctor by ID.
     *
     * @param id Doctor ID.
     * @return Doctor instance or null if not found.
     */
    public Doctor getDoctorById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return doctorStore.getById(id).orElse(null);
    }

    /**
     * Retrieves all registered doctors.
     *
     * @return Unmodifiable list of doctors.
     */
    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    /**
     * Updates details of an existing doctor.
     *
     * @param doctor Updated doctor instance.
     * @return true if updated, false otherwise.
     * @throws InvalidDataException if validation fails.
     */
    public boolean updateDoctor(Doctor doctor) throws InvalidDataException {
        Validator.validateDoctor(doctor);
        return doctorStore.update(doctor);
    }

    /**
     * Deletes a doctor by ID.
     *
     * @param id Doctor ID to delete.
     * @return true if deleted, false otherwise.
     */
    public boolean deleteDoctor(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return doctorStore.deleteById(id);
    }

    // --- Searchable<Doctor> Implementation & Overloading ---

    @Override
    public Doctor searchById(String id) {
        return getDoctorById(id);
    }

    @Override
    public List<Doctor> searchByName(String name) {
        List<Doctor> result = new ArrayList<>();
        if (name == null || name.isBlank()) {
            return result;
        }
        String searchLower = name.trim().toLowerCase();
        for (Doctor d : doctorStore.getAll()) {
            if (d.getName() != null && d.getName().toLowerCase().contains(searchLower)) {
                result.add(d);
            }
        }
        return result;
    }

    /**
     * Overloaded search method to search doctors by medical specialization.
     *
     * @param specialization Target specialization.
     * @return List of matching doctors.
     */
    public List<Doctor> searchBySpecialization(Specialization specialization) {
        List<Doctor> result = new ArrayList<>();
        if (specialization == null) {
            return result;
        }
        for (Doctor d : doctorStore.getAll()) {
            if (d.getSpecialization() == specialization) {
                result.add(d);
            }
        }
        return result;
    }
}
