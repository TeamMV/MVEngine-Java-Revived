package dev.mv.engine.utils.async;

/**
 * The exception that is thrown when an async function ({@link Promise} or {@link PromiseNull}) rejects.
 *
 * @author Maxim Savenkov
 */
public class PromiseRejectedException extends Exception {

    public PromiseRejectedException() {
        super();
    }

    public PromiseRejectedException(String message) {
        super(message);
    }

    public PromiseRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PromiseRejectedException(Throwable cause) {
        super(cause);
    }

    protected PromiseRejectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
