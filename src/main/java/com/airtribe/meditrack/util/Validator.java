package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;

import java.time.LocalDate;

/**
 * Utility class providing centralized validation methods for domain entities and inputs.
 * Throws {@link InvalidDataException} with descriptive messages on rule violations.
 */
public final class Validator {

    private Validator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates that a string is neither null nor blank.
     *
     * @param value     The string value to check.
     * @param fieldName Descriptive field name for error reporting.
     * @throws InvalidDataException if value is null or blank.
     */
    public static void validateString(String value, String fieldName) throws InvalidDataException {
        if (value == null || value.isBlank()) {
            throw new InvalidDataException(fieldName + " cannot be null or blank");
        }
    }

    /**
     * Validates patient input parameters.
     *
     * @param name Patient name.
     * @param age  Patient age.
     * @throws InvalidDataException if validation fails.
     */
    public static void validatePatientData(String name, int age) throws InvalidDataException {
        validateString(name, "Patient name");
        if (age <= 0 || age >= 150) {
            throw new InvalidDataException("Patient age must be greater than 0 and less than 150");
        }
    }

    /**
     * Validates a Patient object instance.
     *
     * @param patient The patient instance.
     * @throws InvalidDataException if patient is null or fields are invalid.
     */
    public static void validatePatient(Patient patient) throws InvalidDataException {
        if (patient == null) {
            throw new InvalidDataException("Patient object cannot be null");
        }
        validatePatientData(patient.getName(), patient.getAge());
    }

    /**
     * Validates doctor input parameters.
     *
     * @param name           Doctor name.
     * @param specialization Doctor medical specialization.
     * @throws InvalidDataException if validation fails.
     */
    public static void validateDoctorData(String name, Specialization specialization) throws InvalidDataException {
        validateString(name, "Doctor name");
        if (specialization == null) {
            throw new InvalidDataException("Doctor specialization cannot be null");
        }
    }

    /**
     * Validates a Doctor object instance.
     *
     * @param doctor The doctor instance.
     * @throws InvalidDataException if doctor is null or fields are invalid.
     */
    public static void validateDoctor(Doctor doctor) throws InvalidDataException {
        if (doctor == null) {
            throw new InvalidDataException("Doctor object cannot be null");
        }
        validateDoctorData(doctor.getName(), doctor.getSpecialization());
    }

    /**
     * Validates appointment input parameters.
     *
     * @param patientId       Patient ID.
     * @param doctorId        Doctor ID.
     * @param appointmentDate Appointment date.
     * @param status          Appointment status.
     * @throws InvalidDataException if validation fails.
     */
    public static void validateAppointmentData(String patientId, String doctorId,
                                              LocalDate appointmentDate, AppointmentStatus status)
            throws InvalidDataException {
        validateString(patientId, "Patient ID");
        validateString(doctorId, "Doctor ID");
        if (appointmentDate == null) {
            throw new InvalidDataException("Appointment date cannot be null");
        }
        if (status == null) {
            throw new InvalidDataException("Appointment status cannot be null");
        }
    }

    /**
     * Validates an Appointment object instance.
     *
     * @param appointment The appointment instance.
     * @throws InvalidDataException if appointment is null or fields are invalid.
     */
    public static void validateAppointment(Appointment appointment) throws InvalidDataException {
        if (appointment == null) {
            throw new InvalidDataException("Appointment object cannot be null");
        }
        validateAppointmentData(
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getStatus()
        );
    }
}
