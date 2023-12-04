package dev.mv.engine.resources.types.border;

import dev.mv.engine.exceptions.UnimplementedException;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;

public class ArcBorder extends RadiusBorder {
    private int range, start;

    public ArcBorder(int strokeWidth, Color color, int range, int start, int radiusX, int radiusY) {
        super(strokeWidth, color, radiusX, radiusY);
        this.range = range;
        this.start = start;
        createCorners();
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, int width, int height, float rot, int ox, int oy) {
        createCorners();
        super.draw(ctx, x, y, width, height, rot, ox, oy);
    }

    @Override
    public Corner createCorner(int index) {
        throw new UnimplementedException();
    }
}
