package dev.mv.engine.utils;

import org.jetbrains.annotations.NotNull;

public enum Infallible {
    ;

    public static @NotNull Infallible exit(int status) {
        System.exit(status);
        return loop();
    }

    public static @NotNull Infallible loop() {
        while (true) {}
    }
}
