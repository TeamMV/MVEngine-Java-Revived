package dev.mv.engine.utils.function;

@FunctionalInterface
public interface FaultyConsumer<T, E extends Throwable> {

    void accept(T t) throws E;

}
