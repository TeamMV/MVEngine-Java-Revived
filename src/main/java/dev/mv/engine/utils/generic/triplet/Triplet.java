package dev.mv.engine.utils.generic.triplet;

/**
 * A triplet of values, can be used to return three values of different types from a function. Variables are not final and can be changed after creation.
 */
public class Triplet<T, U, R> {

    public T a;
    public U b;
    public R c;

    public Triplet() {
        a = null;
        b = null;
        c = null;
    }

    public Triplet(T a, U b, R c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static <T, U, R> Triplet<T, U, R> Trio(T a, U b, R c) {
        return new Triplet<>(a, b, c);
    }

    public static <T, U, R> Triplet<T, U, R> Trio() {
        return new Triplet<>();
    }

}
