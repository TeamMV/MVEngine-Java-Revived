package dev.mv.engine.utils.function;

@FunctionalInterface
public interface FaultyIndexedConsumer<T, E extends Throwable> {
    void accept(T t, int index) throws E;
}
