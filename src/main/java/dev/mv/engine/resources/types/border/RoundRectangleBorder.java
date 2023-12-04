package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;

public class RoundRectangleBorder extends RadiusBorder{

    public RoundRectangleBorder(int strokeWidth, Color color, int radiusX, int radiusY) {
        super(strokeWidth, color, radiusX, radiusY);
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
            case 0 -> new Corner(radiusX, radiusY, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidEllipseArc(x + radiusX + strokeWidth / 2, y + radiusY + strokeWidth / 2, radiusX, radiusY, strokeWidth, 90, 180, (float) (radiusX + radiusY) / 2f, rot, ox, oy));
            case 1 -> new Corner(radiusX, radiusY, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidEllipseArc(x + radiusX + strokeWidth / 2, y - radiusY - strokeWidth / 2, radiusX, radiusY, strokeWidth, 90, 90, (float) (radiusX + radiusY) / 2f, rot, ox, oy));
            case 2 -> new Corner(radiusX, radiusY, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidEllipseArc(x - radiusX - strokeWidth / 2, y - radiusY - strokeWidth / 2, radiusX, radiusY, strokeWidth, 90, 0, (float) (radiusX + radiusY) / 2f, rot, ox, oy));
            case 3 -> new Corner(radiusX, radiusY, (ctx, x, y, strokeWidth, rot, ox, oy) -> ctx.voidEllipseArc(x - radiusX - strokeWidth / 2, y + radiusY + strokeWidth / 2, radiusX, radiusY, strokeWidth, 90, 270, (float) (radiusX + radiusY) / 2f, rot, ox, oy));
        };
    }
}
