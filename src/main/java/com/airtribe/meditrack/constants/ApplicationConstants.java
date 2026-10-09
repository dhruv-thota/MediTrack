package com.airtribe.meditrack.constants;

/**
 * Application-wide constants for MediTrack.
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String APP_NAME = "MediTrack";
    public static final String APP_VERSION = "1.0.0";
    
    // ID prefixes
    public static final String PATIENT_ID_PREFIX = "PAT-";
    public static final String DOCTOR_ID_PREFIX = "DOC-";
    public static final String APPOINTMENT_ID_PREFIX = "APT-";
    public static final String BILL_ID_PREFIX = "BILL-";

    // Date formatting pattern
    public static final String DATE_FORMAT = "yyyy-MM-dd";
}
