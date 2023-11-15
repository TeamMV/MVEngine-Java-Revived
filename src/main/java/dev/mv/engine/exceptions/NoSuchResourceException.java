package dev.mv.engine.exceptions;

public class NoSuchResourceException extends Exception{
    public NoSuchResourceException() {
        super();
    }

    public NoSuchResourceException(String message) {
        super(message);
    }

    public NoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchResourceException(Throwable cause) {
        super(cause);
    }

    protected NoSuchResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
