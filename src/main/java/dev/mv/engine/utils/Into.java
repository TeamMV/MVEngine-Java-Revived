package dev.mv.engine.utils;

public class Into {
    public static <T> T into(Object o) {
        return (T) o;
    }

    public <T> T into() {
        return (T) this;
    }
}
