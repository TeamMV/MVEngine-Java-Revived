package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;

public class RectangleBorder extends Border{
    public RectangleBorder(int strokeWidth, Color color) {
        super(strokeWidth, color);
        createCorners();
    }

    public RectangleBorder() {
        createCorners();
    }

    @Override
    public Corner createCorner(int index) {
        return new Corner(0, 0, (ctx, x, y, strokeWidth, r, ox, oy) -> {});
    }
}
