package dev.mv.engine.utils.generic.pair;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A pair of values, can be used to return two values of different types from a function. Variables are not final and can be changed after creation.
 */
public class Pair<T, U> {
    public T a;
    public U b;

    public Pair() {
        a = null;
        b = null;
    }

    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

    public static <T, U> Pair<T, U> Duo(T a, U b) {
        return new Pair<T, U>(a, b);
    }

    public static <T, U> Pair<T, U> Duo() {
        return new Pair<>();
    }

    public <R, S> Pair<R, S> map(Function<T, R> f1, Function<U, S> f2) {
        return new Pair<R, S>(f1.apply(a), f2.apply(b));
    }

    public <R> R map(Function<Pair<T, U>, R> f) {
        return f.apply(this);
    }

    public <R> R map(BiFunction<T, U, R> f) {
        return f.apply(a, b);
    }

}
