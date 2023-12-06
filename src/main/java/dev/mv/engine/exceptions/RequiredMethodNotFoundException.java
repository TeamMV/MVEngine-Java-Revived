package dev.mv.engine.exceptions;

public class RequiredMethodNotFoundException extends Exception{
    public RequiredMethodNotFoundException() {
        super();
    }

    public RequiredMethodNotFoundException(String message) {
        super(message);
    }

    public RequiredMethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredMethodNotFoundException(Throwable cause) {
        super(cause);
    }

    protected RequiredMethodNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
