package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.ApplicationConstants;

/**
 * Sequential unique ID generator using static counters and static initialization.
 * Generates category-prefixed IDs for Patients, Doctors, and Appointments.
 */
public final class IdGenerator {

    private static int patientCounter;
    private static int doctorCounter;
    private static int appointmentCounter;

    // Static initialization block demonstrating static scope and counter initialization
    static {
        resetCounters();
    }

    private IdGenerator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Resets all static counters to their initial baseline values.
     * Useful for deterministic testing.
     */
    public static void resetCounters() {
        patientCounter = 1000;
        doctorCounter = 2000;
        appointmentCounter = 3000;
    }

    /**
     * Generates a unique sequential Patient ID.
     *
     * @return Formatted patient ID string (e.g., "PAT-1001").
     */
    public static String generatePatientId() {
        patientCounter++;
        return ApplicationConstants.PATIENT_ID_PREFIX + patientCounter;
    }

    /**
     * Generates a unique sequential Doctor ID.
     *
     * @return Formatted doctor ID string (e.g., "DOC-2001").
     */
    public static String generateDoctorId() {
        doctorCounter++;
        return ApplicationConstants.DOCTOR_ID_PREFIX + doctorCounter;
    }

    /**
     * Generates a unique sequential Appointment ID.
     *
     * @return Formatted appointment ID string (e.g., "APT-3001").
     */
    public static String generateAppointmentId() {
        appointmentCounter++;
        return ApplicationConstants.APPOINTMENT_ID_PREFIX + appointmentCounter;
    }
}
