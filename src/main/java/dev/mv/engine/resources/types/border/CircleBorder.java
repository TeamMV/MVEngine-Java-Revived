package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;

import java.util.function.BinaryOperator;

public class CircleBorder extends Border {
    private int radius;

    public CircleBorder(int strokeWidth, Color color, int radius) {
        super(strokeWidth, color);
        this.radius = radius;
        createCorners();
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, int width, int height, float rot, int ox, int oy) {
        radius = width;
        createCorners();
        super.draw(ctx, x, y, width, height, rot, ox, oy);
    }

    @Override
    public Corner createCorner(int index) {
        if (index == 0) {
            return new Corner(radius / 2, radius / 2, (ctx, x, y, strokeWidth, rotation, ox, oy) -> {
                ctx.voidCircle(x + radius, y + radius, this.radius, strokeWidth, radius, rotation, ox, oy);
            });
        }
        return new Corner(radius / 2, radius / 2, (ctx, x, y, strokeWidth, rotation, ox, oy) -> {});
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
