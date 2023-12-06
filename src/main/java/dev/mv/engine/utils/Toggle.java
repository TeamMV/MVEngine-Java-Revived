package dev.mv.engine.utils;

public interface Toggle {

    default void enable() {
        setEnabled(true);
    }

    default void disable() {
        setEnabled(false);
    }

    default void toggle() {
        setEnabled(!isEnabled());
    }

    void setEnabled(boolean enabled);

    boolean isEnabled();
}
