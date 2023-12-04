package dev.mv.engine.utils.async;

/**
 * A parameter of async functions, can be used to reject a function by throwing a throwable during execution of the async function.
 *
 * @author Maxim Savenkov
 */
public interface Rejector {

    /**
     * Rejects the current async function with a throwable as a reason.
     *
     * @param t the throwable.
     */
    void reject(Throwable t);

    /**
     * Rejects the current async function with a string as a reason.
     *
     * @param s the string.
     */
    void reject(String s);

    /**
     * Rejects the current async function with a throwable and a string as a reason.
     *
     * @param s the string.
     * @param t the throwable.
     */
    void reject(String s, Throwable t);

}
