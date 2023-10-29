package dev.mv.engine.exceptions;

public class ParserInvalidAttribException extends Exception {
    public ParserInvalidAttribException() {
    }

    public ParserInvalidAttribException(String message) {
        super(message);
    }

    public ParserInvalidAttribException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserInvalidAttribException(Throwable cause) {
        super(cause);
    }

    public ParserInvalidAttribException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
