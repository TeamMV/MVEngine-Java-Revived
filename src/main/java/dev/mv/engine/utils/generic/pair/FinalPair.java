package dev.mv.engine.utils.generic.pair;

/**
 * A pair of values, can be used to return two values of different types from a function. Variables are final and cannot be changed after creation.
 */
public class FinalPair<T, U> {

    public final T a;
    public final U b;

    public FinalPair(T a, U b) {
        this.a = a;
        this.b = b;
    }

}
