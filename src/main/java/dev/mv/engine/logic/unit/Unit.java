package dev.mv.engine.logic.unit;

public enum Unit {
    MM,
    CM,
    M,
    KM,
    IN,
    MI,
    PX;

    public int intoPx(float value, int dpi) {
        return switch (this) {
            case MM -> (int) (value * dpi / 25.4); // 1 inch = 25.4 mm
            case CM -> (int) (value * dpi / 2.54); // 1 inch = 2.54 cm
            case M ->  (int) (value * dpi / 0.0254); // 1 inch = 0.0254 m
            case KM -> (int) (value * dpi / 0.0000254); // 1 inch = 0.0000254 km
            case IN -> (int) (value * dpi); // 1 inch = dpi pixels
            case MI -> (int) (value * dpi * 63360); // 1 mile = 63360 inches
            case PX -> (int) value; // No conversion needed for pixels
        };
    }
}
