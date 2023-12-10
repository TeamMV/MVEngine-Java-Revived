package dev.mv.engine.logic;

public interface Interpolator<T> {
    T interpolate(T start, T end, float percent);
}
