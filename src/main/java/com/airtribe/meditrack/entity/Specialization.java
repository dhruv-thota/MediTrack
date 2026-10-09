package com.airtribe.meditrack.entity;

/**
 * Enumeration representing medical specializations for Doctors.
 */
public enum Specialization {
    CARDIOLOGY("Cardiology"),
    DERMATOLOGY("Dermatology"),
    PEDIATRICS("Pediatrics"),
    NEUROLOGY("Neurology"),
    GENERAL_MEDICINE("General Medicine"),
    ORTHOPEDICS("Orthopedics"),
    GYNECOLOGY("Gynecology");

    private final String displayName;

    Specialization(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
