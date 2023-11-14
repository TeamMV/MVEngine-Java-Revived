package dev.mv.engine.resources.types.border;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;

public class EllispeBorder extends Border{
    private int radiusX, radiusY;

    public EllispeBorder(int strokeWidth, Color color) {
        super(strokeWidth, color);
        createCorners();
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, int width, int height, float rot, int ox, int oy) {
        radiusX = width;
        radiusY = height;
        createCorners();
        super.draw(ctx, x, y, width, height, rot, ox, oy);
    }

    @Override
    public Corner createCorner(int index) {
        if (index == 0) {
            return new Corner(radiusX / 2, radiusY / 2, (ctx, x, y, strokeWidth, rotation, ox, oy) -> {
                ctx.voidEllipse(x + radiusX, y + radiusY, this.radiusX, this.radiusY, strokeWidth, (float) (radiusX + radiusY) / 2, rotation, ox, oy);
            });
        }
        return new Corner(radiusX / 2, radiusY / 2, (ctx, x, y, strokeWidth, rotation, ox, oy) -> {});
    }

    public int getRadiusX() {
        return radiusX;
    }

    public void setRadiusX(int radiusX) {
        this.radiusX = radiusX;
    }

    public int getRadiusY() {
        return radiusY;
    }

    public void setRadiusY(int radiusY) {
        this.radiusY = radiusY;
    }
}
