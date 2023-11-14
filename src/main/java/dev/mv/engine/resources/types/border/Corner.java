package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.DrawContext;

import java.util.function.Consumer;

public record Corner(
        int radiusX,
        int radiusY,
        DrawFunc drawFunc
) {
    interface DrawFunc {
        void draw(DrawContext ctx, int x, int y, int strokeWidth, float rotation, int ox, int oy);
    }
}
