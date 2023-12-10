package dev.mv.engine.logic.unit;

public enum TimeUnit {
    MS,
    S,
    M;

    public int getMS(float value) {
        return switch (this) {
            case MS -> (int) value;
            case S -> (int) value * 1000;
            case M -> (int) value * 60000;
        };
    }
}
