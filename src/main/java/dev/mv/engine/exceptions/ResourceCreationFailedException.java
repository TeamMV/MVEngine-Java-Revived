package dev.mv.engine.exceptions;

public class ResourceCreationFailedException extends Exception{
    public ResourceCreationFailedException() {
        super();
    }

    public ResourceCreationFailedException(String message) {
        super(message);
    }

    public ResourceCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceCreationFailedException(Throwable cause) {
        super(cause);
    }

    protected ResourceCreationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
