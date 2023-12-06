package dev.mv.engine.input;

public enum InputState {
    PRESSED,
    RELEASED,
    JUST_PRESSED,
    JUST_RELEASED;

    public boolean pressed() {
        return this == PRESSED || this == JUST_PRESSED;
    }

    public boolean released() {
        return this == RELEASED || this == JUST_RELEASED;
    }
}
