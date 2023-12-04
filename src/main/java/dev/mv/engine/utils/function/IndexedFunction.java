package dev.mv.engine.utils.function;

public interface IndexedFunction<T, R> {

    R apply(T t, int index);

}
