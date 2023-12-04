package dev.mv.engine.utils.generic.pair;

import java.util.function.Function;

public class UnaryPair<T> {

    public T a;
    public T b;

    public UnaryPair() {
        a = null;
        b = null;
    }

    public UnaryPair(T a, T b) {
        this.a = a;
        this.b = b;
    }

    public <R> UnaryPair<R> map(Function<T, R> f) {
        return new UnaryPair<>(f.apply(a), f.apply(b));
    }
}
