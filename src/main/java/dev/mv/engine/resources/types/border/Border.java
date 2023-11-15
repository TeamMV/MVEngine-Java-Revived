package dev.mv.engine.resources.types.border;

import dev.mv.engine.exceptions.Exceptions;
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

    public Border(int strokeWidth, Color color) {
        super(100, 100);
        this.strokeWidth = strokeWidth;
        this.color = color;
    }

    public Border() {
        super(100, 100);
    }

    protected void createCorners() {
        for (int i = 0; i < 4; i++) {
            corners[i] = createCorner(i);
        }
    }

    public abstract Corner createCorner(int index);

    @Override
    public void draw(DrawContext ctx, int x, int y, float rot, int ox, int oy) {
        draw(ctx, x, y, getCnvsW(), getCnvsH(), rot, ox, oy);
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, int width, int height, float rot, int ox, int oy) {
        ctx.color(color);
        corners[0].drawFunc().draw(ctx, x, y, strokeWidth, rot, ox, oy);
        corners[1].drawFunc().draw(ctx, x, y + height, strokeWidth, rot, ox, oy);
        corners[2].drawFunc().draw(ctx, x + width, y + height, strokeWidth, rot, ox, oy);
        corners[3].drawFunc().draw(ctx, x + width, y, strokeWidth, rot, ox, oy);

        ctx.rectangle(x + corners[0].radiusX(), y, width - corners[0].radiusX() - corners[3].radiusX(), strokeWidth, rot, ox, oy);
        ctx.rectangle(x + corners[1].radiusX(), y + height - strokeWidth, width - corners[1].radiusX() - corners[2].radiusX(), strokeWidth, rot, ox, oy);
        ctx.rectangle(x, y + corners[0].radiusY(), strokeWidth, height - corners[0].radiusY() - corners[1].radiusY(), rot, ox, oy);
        ctx.rectangle(x + width - strokeWidth, y + corners[3].radiusY(), strokeWidth, height - corners[3].radiusY() - corners[2].radiusY(), rot, ox, oy);
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
    public void parse(Parser parser) {
        parser.requireCurrent("border");
        int strokeWidth = parser.intAttrib("strokeWidth", 3);
        Color color = Color.parse(parser.attrib("color", "255, 255, 255"));
        String type = parser.attrib("type", "new");
         switch (type) {
            default -> copyFrom(parseCustom(parser.inner()));
            case "rect" -> copyFrom(new RectangleBorder(strokeWidth, color));
            case "roundRect" -> copyFrom(new RoundRectangleBorder(strokeWidth, color, parser.intAttrib("radius", 10)));
            case "circle" -> copyFrom(new CircleBorder(strokeWidth, color));
            case "ellipse" -> copyFrom(new EllispeBorder(strokeWidth, color));
            case "arc" -> copyFrom(new ArcBorder(strokeWidth, color, parser.intAttrib("range", 90), parser.intAttrib("start", 0)));
        };
    }

    private void copyFrom(Border other) {
        if (other == null) return;
        this.strokeWidth = other.getStrokeWidth();
        this.color = other.getColor();
        this.corners = other.corners;
        this.cnvsW = other.getCnvsW();
        this.cnvsH = other.getCnvsH();
    }

    private Border parseCustom(Parser parser) {
        return null;
    }

    private Corner parseCorner(Parser parser) {
        return null;
    }
}
