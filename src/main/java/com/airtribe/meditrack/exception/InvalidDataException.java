package com.airtribe.meditrack.exception;

/**
 * Exception thrown when validation fails for patient, doctor, or appointment data.
 */
public class InvalidDataException extends Exception {
    
    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
