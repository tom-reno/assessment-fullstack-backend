package de.tomreno.assessment.fullstack.backend.exception;

/**
 * A RuntimeException that is thrown when necessary initialization could not be done.
 */
public class BackendInitializationException extends RuntimeException {

    public BackendInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackendInitializationException(String message) {
        super(message);
    }

}
