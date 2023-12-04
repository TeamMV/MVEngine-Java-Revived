package dev.mv.engine.utils.generic.triplet;

/**
 * A triplet of values, can be used to return three values of different types from a function. Variables are final and cannot be changed after creation.
 */
public class FinalTriplet<T, U, R> {

    public final T a;
    public final U b;
    public final R c;

    public FinalTriplet(T a, U b, R c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
