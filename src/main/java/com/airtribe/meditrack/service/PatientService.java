package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service managing Patient domain operations including creation, retrieval, updates,
 * deletion, and overloaded searching.
 */
public class PatientService implements Searchable<Patient> {

    private final DataStore<Patient> patientStore;
    private AppointmentService appointmentService;

    public PatientService() {
        this.patientStore = new DataStore<>(Patient::getId);
    }

    /**
     * Optional dependency injector for AppointmentService to check active appointments prior to deletion.
     *
     * @param appointmentService The AppointmentService instance.
     */
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Adds a new patient after generating a unique ID and performing validation.
     *
     * @param name          Patient full name.
     * @param age           Patient age.
     * @param contactNumber Patient contact details.
     * @return The created Patient instance.
     * @throws InvalidDataException if validation fails or saving fails.
     */
    public Patient addPatient(String name, int age, String contactNumber) throws InvalidDataException {
        Validator.validatePatientData(name, age);
        String id = IdGenerator.generatePatientId();
        Patient patient = new Patient(id, name, age, contactNumber);
        
        boolean saved = patientStore.save(patient);
        if (!saved) {
            throw new InvalidDataException("Failed to save patient with ID " + id);
        }
        return patient;
    }

    /**
     * Retrieves a patient by ID.
     *
     * @param id The patient ID.
     * @return Patient instance or null if not found.
     */
    public Patient getPatientById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return patientStore.getById(id).orElse(null);
    }

    /**
     * Retrieves all registered patients.
     *
     * @return Unmodifiable list of patients.
     */
    public List<Patient> getAllPatients() {
        return patientStore.getAll();
    }

    /**
     * Updates details of an existing patient.
     *
     * @param patient The updated patient object.
     * @return true if update succeeded, false otherwise.
     * @throws InvalidDataException if patient validation fails.
     */
    public boolean updatePatient(Patient patient) throws InvalidDataException {
        Validator.validatePatient(patient);
        return patientStore.update(patient);
    }

    /**
     * Deletes a patient by ID, provided they have no active appointments.
     *
     * @param patientId The ID of the patient to delete.
     * @return true if deleted successfully, false otherwise.
     * @throws InvalidDataException if patient has active appointments.
     */
    public boolean deletePatient(String patientId) throws InvalidDataException {
        if (patientId == null || patientId.isBlank()) {
            return false;
        }
        if (appointmentService != null && appointmentService.hasActiveAppointmentsForPatient(patientId)) {
            throw new InvalidDataException("Cannot delete patient " + patientId + " because they have active appointments.");
        }
        return patientStore.deleteById(patientId);
    }

    // --- Searchable<Patient> Implementation & Overloading ---

    @Override
    public Patient searchById(String id) {
        return getPatientById(id);
    }

    @Override
    public List<Patient> searchByName(String name) {
        List<Patient> result = new ArrayList<>();
        if (name == null || name.isBlank()) {
            return result;
        }
        String searchLower = name.trim().toLowerCase();
        for (Patient p : patientStore.getAll()) {
            if (p.getName() != null && p.getName().toLowerCase().contains(searchLower)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Overloaded search method to search patients by age.
     *
     * @param age Patient age to match.
     * @return List of matching patients.
     */
    public List<Patient> searchByAge(int age) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patientStore.getAll()) {
            if (p.getAge() == age) {
                result.add(p);
            }
        }
        return result;
    }
}
