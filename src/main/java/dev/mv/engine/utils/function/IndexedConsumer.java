package dev.mv.engine.utils.function;

@FunctionalInterface
public interface IndexedConsumer<T> {

    void accept(T item, int index);

}
