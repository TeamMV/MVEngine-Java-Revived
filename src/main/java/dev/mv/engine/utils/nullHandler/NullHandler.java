package dev.mv.engine.utils.nullHandler;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Null handler which can execute certain functions if the object is null, others if it isn't, and append other conditions.
 */
public class NullHandler<T> {

    private T t;
    private boolean also = true;
    private Object returnValue;

    public NullHandler(T t) {
        this.t = t;
    }

    /**
     * If the value is not null, will execute the provided function, that takes the object as a parameter.
     *
     * @param func consumer function that takes in the object as a parameter.
     * @return itself, for chaining.
     */
    public NullHandler<T> then(Consumer<T> func) {
        if (t != null && also) {
            func.accept(t);
        }
        return this;
    }

    /**
     * If the value is null, will execute the provided runnable.
     *
     * @param func runnable to be executed.
     * @return itself, for chaining.
     */
    public NullHandler<T> otherwise(Runnable func) {
        if (t == null || !also) {
            func.run();
        }
        return this;
    }

    /**
     * If the value is not null, will set the return value to the not null value.
     *
     * @return itself, for chaining.
     */
    public NullHandler<T> thenReturn() {
        if (t != null && also) {
            returnValue = t;
        }
        return this;
    }

    /**
     * If the value is not null, will execute the provided function, that takes the object as a parameter and returns a value.
     *
     * @param func function that takes in the object and returns a value.
     * @return itself, for chaining.
     */
    public <R> NullHandler<T> thenReturn(Function<T, R> func) {
        if (t != null && also) {
            returnValue = func.apply(t);
        }
        return this;
    }

    /**
     * If the value is null, will execute the provided function, that returns a value.
     *
     * @param func supplier function that returns a value.
     * @return itself, for chaining.
     */
    public <R> NullHandler<T> otherwiseReturn(Supplier<R> func) {
        if (t == null || !also) {
            returnValue = func.get();
        }
        return this;
    }

    /**
     * If the object is not null, the return value will be set to the provided value.
     *
     * @param value the value to set return to.
     * @return itself, for chaining.
     */
    public <R> NullHandler<T> thenReturn(R value) {
        if (t != null && also) {
            returnValue = value;
        }
        return this;
    }

    /**
     * If the object is null, the return value will be set to the provided value.
     *
     * @param value the value to set return to.
     * @return itself, for chaining.
     */
    public <R> NullHandler<T> otherwiseReturn(R value) {
        if (t == null || !also) {
            returnValue = value;
        }
        return this;
    }

    /**
     * Add an extra set of conditions about the object that need to be true for 'then' function to execute.
     *
     * @param tests the tests that will be run on the object.
     * @return itself, for chaining.
     */
    public NullHandler<T> alsoIf(Predicate<T>... tests) {
        for (Predicate test : tests) {
            if (!test.test(t)) {
                also = false;
            }
        }
        return this;
    }

    /**
     * Resets the added chain of conditions, and removes any blocks that could have happened.
     *
     * @return itself, for chaining.
     */
    public NullHandler<T> resetIf() {
        also = true;
        return this;
    }

    /**
     * Get the return value of this object as a {@link NullHandlerReturn}.
     *
     * @return the return value as a {@link NullHandlerReturn}.
     */
    public NullHandlerReturn getGenericReturnValue() {
        return new NullHandlerReturn(returnValue);
    }

    /**
     * Get the return value of this object.
     *
     * @return the return value.
     */
    public Object getReturnValue() {
        return returnValue;
    }
}
