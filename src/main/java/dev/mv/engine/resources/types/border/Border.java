package dev.mv.engine.resources.types.border;

import dev.mv.engine.parsing.Parser;
import dev.mv.engine.parsing.XMLParser;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.resources.types.drawable.StaticDrawable;

import java.io.InputStream;

public abstract class Border extends StaticDrawable {//1--2
    protected Corner[] corners = new Corner[4];   //   |  |
    protected int strokeWidth;//                       0--3
    protected Color color = Color.WHITE;

    public Border(Corner[] corners, int strokeWidth, Color color) {
        super(100, 100);
        this.corners = corners;
        this.strokeWidth = strokeWidth;
        this.color = color;
    }

    public Border() {
        super(100, 100);
        for (int i = 0; i < 4; i++) {
            corners[i] = createCorner(i);
        }
    }

    public abstract Corner createCorner(int index);

    @Override
    public void draw(DrawContext ctx, int x, int y, int width, int height, float rot, int ox, int oy) {
        ctx.color(color);
        corners[0].drawFunc().draw(ctx, x, y, corners[0].radius(), strokeWidth, rot, ox, oy);
        corners[1].drawFunc().draw(ctx, x, y + height, corners[1].radius(), strokeWidth, rot, ox, oy);
        corners[2].drawFunc().draw(ctx, x + width, y + height, corners[2].radius(), strokeWidth, rot, ox, oy);
        corners[3].drawFunc().draw(ctx, x + width, y, corners[3].radius(), strokeWidth, rot, ox, oy);

        ctx.rectangle(x + corners[0].radius(), y, width - corners[0].radius() - corners[3].radius(), strokeWidth, rot, ox, oy);
        ctx.rectangle(x + corners[1].radius(), y + height - strokeWidth, width - corners[1].radius() - corners[2].radius(), strokeWidth, rot, ox, oy);
        ctx.rectangle(x, y + corners[0].radius(), strokeWidth, height - corners[0].radius() - corners[1].radius(), rot, ox, oy);
        ctx.rectangle(x + width - strokeWidth, y + corners[3].radius(), strokeWidth, height - corners[3].radius() - corners[2].radius(), rot, ox, oy);
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Border parse(Parser parser) {
        parser = parser.requireRoot("border").inner();
        int strokeWidth = parser.intAttrib("strokeWidth", 3);
        Color color = Color.parse(parser.attrib("color", "255, 255, 255"));
        return null;
    }

    private Corner parseCorner(Parser parser) {
        return null;
    }
}
