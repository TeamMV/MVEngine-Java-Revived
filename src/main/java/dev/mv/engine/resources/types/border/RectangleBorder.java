package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.DrawContext;

public class RectangleBorder extends Border{
    @Override
    public Corner createCorner(int index) {
        return new Corner(0, (ctx, x, y, radius, strokeWidth, r, ox, oy) -> {});
    }
}
