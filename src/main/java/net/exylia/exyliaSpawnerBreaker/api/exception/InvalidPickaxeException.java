package net.exylia.exyliaSpawnerBreaker.api.exception;

public class InvalidPickaxeException extends RuntimeException {
    public InvalidPickaxeException(String message) {
        super(message);
    }

    public InvalidPickaxeException(String message, Throwable cause) {
        super(message, cause);
    }
}
