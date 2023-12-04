package dev.mv.engine.utils.function;

@FunctionalInterface
public interface UnaryBiPredicate<T> {

    boolean compare(T a, T b);

}
