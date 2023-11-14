package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;

public class RoundRectangleBorder extends Border{
    private int radius = 10;

    public RoundRectangleBorder(int strokeWidth, Color color, int radius) {
        super(strokeWidth, color);
        this.radius = radius;
        createCorners();
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, int width, int height, float rot, int ox, int oy) {
        createCorners();
        super.draw(ctx, x, y, width, height, rot, ox, oy);
    }

    @Override
    public Corner createCorner(int index) {
        return switch (index) {
            default -> null;
            case 0 -> new Corner(radius, radius, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidArc(x + radius, y + radius, radius - strokeWidth / 2, strokeWidth, 90, 180, radius, rot, ox, oy));
            case 1 -> new Corner(radius, radius, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidArc(x + radius, y - radius, radius - strokeWidth / 2, strokeWidth, 90, 90, radius, rot, ox, oy));
            case 2 -> new Corner(radius, radius, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidArc(x - radius, y - radius, radius - strokeWidth / 2, strokeWidth, 90, 0, radius, rot, ox, oy));
            case 3 -> new Corner(radius, radius, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidArc(x - radius, y + radius, radius - strokeWidth / 2, strokeWidth, 90, 270, radius, rot, ox, oy));
        };
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        for (int i = 0; i < 4; i++) {
            corners[i] = createCorner(i);
        }
    }
}
