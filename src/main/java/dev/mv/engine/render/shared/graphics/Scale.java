package dev.mv.engine.render.shared.graphics;

public class Scale {
    public int x, y;

    public Scale(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Scale equal(int value) {
        return new Scale(value, value);
    }
}
