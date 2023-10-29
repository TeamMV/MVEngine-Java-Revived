package dev.mv.engine.gui;

public enum Origin {
    TOP_LEFT,
    BOTTOM_LEFT,
    TOP_RIGHT,
    BOTTOM_RIGHT,
    CENTER;

    public boolean isRight() {
        return this == TOP_RIGHT || this == BOTTOM_RIGHT;
    }

    public boolean isTop() {
        return this == TOP_RIGHT || this == TOP_LEFT;
    }
}
