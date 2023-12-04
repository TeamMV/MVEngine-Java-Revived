package dev.mv.engine.utils.async;

/**
 * A parameter of non-null async functions, can be used to resolve a function by returning a value.
 *
 * @author Maxim Savenkov
 */
public interface Resolver<T> {

    /**
     * Resolve the async function and return the value.
     *
     * @param t the value.
     */
    void resolve(T t);

}
