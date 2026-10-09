package com.airtribe.meditrack.exception;

/**
 * Exception thrown when an appointment with a specified ID cannot be found.
 */
public class AppointmentNotFoundException extends Exception {
    
    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
