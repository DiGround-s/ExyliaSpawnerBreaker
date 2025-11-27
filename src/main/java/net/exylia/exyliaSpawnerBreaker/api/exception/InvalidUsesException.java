package net.exylia.exyliaSpawnerBreaker.api.exception;

public class InvalidUsesException extends RuntimeException {
    public InvalidUsesException(String message) {
        super(message);
    }

    public InvalidUsesException(String message, Throwable cause) {
        super(message, cause);
    }
}
