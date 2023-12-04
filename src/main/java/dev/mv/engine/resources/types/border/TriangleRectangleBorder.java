package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.Color;

public class TriangleRectangleBorder extends RadiusBorder {

    public TriangleRectangleBorder(int strokeWidth, Color color, int radiusX, int radiusY) {
        super(strokeWidth, color, radiusX, radiusY);
        createCorners();
    }

    @Override
    public Corner createCorner(int index) {
        return null;
    }
}
