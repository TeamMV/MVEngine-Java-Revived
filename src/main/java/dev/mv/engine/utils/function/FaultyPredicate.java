package dev.mv.engine.utils.function;

@FunctionalInterface
public interface FaultyPredicate<T, E extends Throwable> {

    boolean test(T t) throws E;

}
