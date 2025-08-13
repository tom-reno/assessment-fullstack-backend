package de.tomreno.assessment.fullstack.backend.exception;

/**
 * A RuntimeException that is thrown when CSV tasks failed.
 */
public class BackendCsvException extends RuntimeException {

    public BackendCsvException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackendCsvException(String message) {
        super(message);
    }

}
