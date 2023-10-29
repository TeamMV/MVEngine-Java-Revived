package dev.mv.engine.utils;

import java.util.function.BiFunction;

@FunctionalInterface
public interface BinaryFunction<T, R> extends BiFunction<T, T, R> {}
