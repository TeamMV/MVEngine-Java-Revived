package dev.mv.engine.exceptions;

public class DuplicateResourceIdException extends Exception{
    public DuplicateResourceIdException() {
        super();
    }

    public DuplicateResourceIdException(String message) {
        super(message);
    }

    public DuplicateResourceIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateResourceIdException(Throwable cause) {
        super(cause);
    }

    protected DuplicateResourceIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
