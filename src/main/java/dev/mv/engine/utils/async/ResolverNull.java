package dev.mv.engine.utils.async;

/**
 * A parameter of null async functions, can be used to resolve a function by returning.
 *
 * @author Maxim Savenkov
 */
public interface ResolverNull {

    /**
     * Resolve the async function.
     */
    void resolve();

}
