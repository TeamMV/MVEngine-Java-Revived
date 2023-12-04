package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.Color;

public abstract class RadiusBorder extends Border {
    protected int radiusX, radiusY;

    public RadiusBorder(int strokeWidth, Color color, int radiusX, int radiusY) {
        super(strokeWidth, color);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    public int getRadiusX() {
        return radiusX;
    }
    public int getRadiusY() {
        return radiusY;
    }

    public void setRadius(int radiusX, int radiusY) {
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        for (int i = 0; i < 4; i++) {
            corners[i] = createCorner(i);
        }
    }
}
