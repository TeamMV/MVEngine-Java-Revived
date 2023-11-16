package dev.mv.engine.resources.types.drawable;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.exceptions.InvalidGuiFileException;
import dev.mv.engine.parsing.Parser;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.resources.R;
import dev.mv.engine.resources.types.border.Border;
import dev.mv.engine.resources.types.border.Corner;
import dev.mv.engine.utils.BinaryFunction;
import dev.mv.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

public class StaticDrawable extends Drawable {
    private Consumer<DrawContext> drawFunc;
    private int x, y;

    private Color currentColor = Color.WHITE;
    private final Transformations transformations;
    private final DrawOptions drawOptions;

    public StaticDrawable(int canvasWidth, int canvasHeight) {
        super(canvasWidth, canvasHeight);
        transformations = new Transformations();
        drawOptions = new DrawOptions();
    }

    protected Transformations getTransformations() {
        return transformations;
    }

    protected DrawOptions getDrawOptions() {
        return drawOptions;
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, float rot, int ox, int oy) {
        ctx.translate(x, y);
        ctx.origin(ox, oy);
        ctx.rotate(rot);

        drawOptions.setBorder(null);
        drawOptions.setFilled(false);
        drawOptions.setStrokeWidth((w, h) -> 2);
        transformations.setOriginX(null);
        transformations.setOriginY(null);
        transformations.setScaleX((w, h) -> 1f);
        transformations.setScaleY((w, h) -> 1f);
        transformations.setTransX((w, h) -> 0);
        transformations.setTransY((w, h) -> 0);
        transformations.setRotation((w, h) -> 0);

        drawFunc.accept(ctx);
        ctx.resetTransformations();
    }

    @Override
    public void parse(Parser parser) {
        resId = parser.requireAttrib("resId");
        parser = parser.inner();
        if (!parser.current().equals("canvas")) {
            Exceptions.send(new InvalidGuiFileException("<drawable> MUST have <canvas> as the first tag!"));
            return;
        }
        cnvsW = parser.intAttrib("width", 100);
        cnvsH = parser.intAttrib("height", 100);
        parser = parser.inner();

        List<Consumer<DrawContext>> actions = new ArrayList<>();

        do {
            Consumer<DrawContext> action = parseTag(parser);
            if (action != null) {
                actions.add(action);
            }
        } while (parser.advance());

        this.drawFunc = ctx -> actions.forEach(c -> c.accept(ctx));
    }
    
    private Consumer<DrawContext> parseTag(Parser parser) {
        Parser originalParser = parser.copy();

        return switch (parser.current()) {
            case "color": { yield ctx -> currentColor = Color.parse(originalParser.text()); }
            case "rotate": { yield ctx -> transformations.setRotation(parseRotate(originalParser)); }
            case "scale": { yield parseScale(originalParser, transformations); }
            case "translate": { yield parseTranslate(originalParser, transformations); }
            case "origin": { yield parseOrigin(originalParser, transformations); }
            case "fill": { drawOptions.setFilled(true); yield null; }
            case "noFill": { drawOptions.setFilled(false); yield null; }
            case "strokeWidth": { yield parseStroke(originalParser, drawOptions); }
            case "border": { yield parseBorder(originalParser, drawOptions); }
            default: yield parseShape(parser, transformations, drawOptions);
        };
    }
    
    private BinaryOperator<Integer> parseRotate(Parser parser) {
        if (parser.hasAttrib("deg")) {
            return parseValue(parser.attrib("deg"), false);
        }
        return (w, h) -> (int) Math.toDegrees(parseValue(parser.attrib("rad"), false).apply(w, h));
    }

    private Consumer<DrawContext> parseScale(Parser parser, Transformations transformations) {
        BinaryFunction<Integer, Float> x;
        BinaryFunction<Integer, Float> y;

        if (parser.hasAttrib("x")) {
            x = parseValueF(parser.attrib("x"), true);
        } else {
            x = null;
        }
        if (parser.hasAttrib("y")) {
            y = parseValueF(parser.attrib("y"), false);
        } else {
            y = null;
        }

        return ctx -> {
            transformations.setScaleX(x);
            transformations.setScaleX(y);
        };
    }

    private Consumer<DrawContext> parseTranslate(Parser parser, Transformations transformations) {
        BinaryOperator<Integer> x, y;

        if (parser.hasAttrib("x")) {
            x = parseValue(parser.attrib("x"), true);
        } else {
            x = null;
        }
        if (parser.hasAttrib("y")) {
            y = parseValue(parser.attrib("y"), false);
        } else {
            y = null;
        }

        return ctx -> {
            transformations.setTransX(x);
            transformations.setTransX(y);
        };
    }

    private Consumer<DrawContext> parseOrigin(Parser parser, Transformations transformations) {
        if (parser.hasAttrib("center")) {
            String center = parser.attrib("center");
            if (center.equals("canvas")) {
                return ctx -> {
                    transformations.setOriginX((w, h) -> w / 2);
                    transformations.setOriginY((w, h) -> h / 2);
                };
            } else if (center.equals("shape")) {
                return ctx -> {
                    transformations.setOriginX(null);
                    transformations.setOriginY(null);
                };
            }
        }

        BinaryOperator<Integer> x, y;

        if (parser.hasAttrib("x")) {
            x = parseValue(parser.attrib("x"), true);
        } else {
            x = null;
        }
        if (parser.hasAttrib("y")) {
            y = parseValue(parser.attrib("y"), false);
        } else {
            y = null;
        }

        return ctx -> {
            transformations.setOriginX(x);
            transformations.setOriginX(y);
        };
    }

    private Consumer<DrawContext> parseStroke(Parser parser, DrawOptions drawOptions) {
        var value = parseValue(parser.text(), true);
        return ctx -> drawOptions.setStrokeWidth(value);
    }

    private Consumer<DrawContext> parseBorder(Parser parser, DrawOptions drawOptions) {
        if (parser.hasAttrib("res")) {
            drawOptions.setBorder(R.drawable.get(parser.attrib("res")));
        }
        var border = new Border() {
            @Override
            public void draw(DrawContext ctx, int x, int y, float rot, int ox, int oy) {}

            @Override
            public Corner createCorner(int index) {
                return null;
            } //gets overridden in the next line
        };
        border.parse(parser);

        return ctx -> drawOptions.setBorder(border);
    }

    private Consumer<DrawContext> parseShape(Parser parser, Transformations transformations, DrawOptions drawOptions) {
        return switch (parser.current()) {
            case "triangle": yield parseTriangle(parser, transformations, drawOptions);
            case "rect": yield parseRect(parser, transformations, drawOptions);
            case "circle": yield parseCircle(parser, transformations, drawOptions);
            case "ellipse": yield parseEllipse(parser, transformations, drawOptions);
            case "arc": yield parseArc(parser, transformations, drawOptions);
            default: yield c -> {};
        };
    }

    private BinaryOperator<Integer> parseValue(String str, boolean preferWidth) {
        if (str.endsWith("%")) {
            char mod = str.charAt(str.length() - 2);
            if (!Character.isDigit(mod)) {
                int v = Integer.parseInt(str.substring(0, str.length() - 2));
                if (mod == 'w') {
                    return (w, h) -> Utils.getValue(v, w);
                }
                if (mod == 'h') {
                    return (w, h) -> Utils.getValue(v, h);
                }
            }
            int v = Integer.parseInt(str.substring(0, str.length() - 1));
            if (preferWidth) {
                return (w, h) -> Utils.getValue(v, w);
            }
            return (w, h) -> Utils.getValue(v, h);
        }

        final int v = Integer.parseInt(str);
        return (w, h) -> v;
    }

    private BinaryFunction<Integer, Float> parseValueF(String str, boolean preferWidth) {
        if (str.endsWith("%")) {
            char mod = str.charAt(str.length() - 2);
            if (!Character.isDigit(mod)) {
                float v = Float.parseFloat(str.substring(0, str.length() - 2));
                if (mod == 'w') {
                    return (w, h) -> Utils.getValue(v, w);
                }
                if (mod == 'h') {
                    return (w, h) -> Utils.getValue(v, h);
                }
            }
            float v = Float.parseFloat(str.substring(0, str.length() - 1));
            if (preferWidth) {
                return (w, h) -> Utils.getValue(v, w);
            }
            return (w, h) -> Utils.getValue(v, h);
        }

        final float v = Float.parseFloat(str);
        return (w, h) -> v;
    }

    private Consumer<DrawContext> parseTriangle(Parser parser, Transformations transformations, DrawOptions drawOptions) {
        var x1 = parseValue(parser.attrib("x1", "0"), true);
        var y1 = parseValue(parser.attrib("y1", "0"), false);
        var x2 = parseValue(parser.attrib("x2", "10"), true);
        var y2 = parseValue(parser.attrib("y2", "10"), false);
        var x3 = parseValue(parser.attrib("x3", "20"), true);
        var y3 = parseValue(parser.attrib("y3", "0"), false);

        if (drawOptions.filled()) {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> (x1.apply(getCnvsW(), getCnvsH()) + x2.apply(getCnvsW(), getCnvsH()) + x3.apply(getCnvsW(), getCnvsH())) / 3,
                        (wi, he) -> (y1.apply(getCnvsW(), getCnvsH()) + y2.apply(getCnvsW(), getCnvsH()) + y3.apply(getCnvsW(), getCnvsH())) / 3
                );

                int tx1 = x1.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty1 = y1.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH());
                int tx2 = (int) (x2.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH())) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty2 = (int) (y2.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH())) + transformations.transY().apply(getCnvsW(), getCnvsH());
                int tx3 = (int) (x3.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH())) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty3 = (int) (y3.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH())) + transformations.transY().apply(getCnvsW(), getCnvsH());

                c.color(currentColor);
                c.triangle(tx1, ty1, tx2, ty2, tx3, ty3, transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));
                //drawOptions.border().draw(c, tx, ty, tw, th);

                transformations.restoreOriginsToNull();
            };
        } else {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> (x1.apply(getCnvsW(), getCnvsH()) + x2.apply(getCnvsW(), getCnvsH()) + x3.apply(getCnvsW(), getCnvsH())) / 3,
                        (wi, he) -> (y1.apply(getCnvsW(), getCnvsH()) + y2.apply(getCnvsW(), getCnvsH()) + y3.apply(getCnvsW(), getCnvsH())) / 3
                );

                int tx1 = x1.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty1 = y1.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH());
                int tx2 = (int) (x2.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH())) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty2 = (int) (y2.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH())) + transformations.transY().apply(getCnvsW(), getCnvsH());
                int tx3 = (int) (x3.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH())) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty3 = (int) (y3.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH())) + transformations.transY().apply(getCnvsW(), getCnvsH());

                c.color(currentColor);
                c.voidTriangle(tx1, ty1, tx2, ty2, tx3, ty3, drawOptions.strokeWidth().apply(getCnvsW(), getCnvsH()), transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                //drawOptions.border().draw(c, tx, ty, tw, th);

                transformations.restoreOriginsToNull();
            };
        }
    }

    private Consumer<DrawContext> parseRect(Parser parser, Transformations transformations, DrawOptions drawOptions) {
        var x = parseValue(parser.attrib("x", "0"), true);
        var y = parseValue(parser.attrib("y", "0"), false);
        var w = parseValue(parser.attrib("width", "10"), true);
        var h = parseValue(parser.attrib("height", "10"), false);

        if (drawOptions.filled()) {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()) + w.apply(getCnvsW(), getCnvsH()) / 2,
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH()) + h.apply(getCnvsW(), getCnvsH()) / 2
                );

                int tx = x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty = y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH());
                int tw = (int) (w.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int th = (int) (h.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH()));

                c.color(currentColor);
                c.rectangle(tx, ty, tw, th, transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx, ty, tw, th, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        } else {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()) + w.apply(getCnvsW(), getCnvsH()) / 2,
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH()) + h.apply(getCnvsW(), getCnvsH()) / 2
                );

                int tx = x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH());
                int ty = y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH());
                int tw = (int) (w.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int th = (int) (h.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH()));

                c.color(currentColor);
                c.voidRectangle(tx, ty, tw, th, drawOptions.strokeWidth().apply(getCnvsW(), getCnvsH()), transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx, ty, tw, th, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        }
    }

    private Consumer<DrawContext> parseCircle(Parser parser, Transformations transformations, DrawOptions drawOptions) {
        var x = parseValue(parser.attrib("x", "0"), true);
        var y = parseValue(parser.attrib("y", "0"), false);
        var r = parseValue(parser.attrib("radius", "10"), true);

        if (drawOptions.filled()) {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()),
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH())
                );

                int tx = (int) (x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int ty = (int) (y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int tr = (int) (r.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));

                c.color(currentColor);
                c.circle(tx, ty, tr, tr, transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx - tr, ty - tr, tr, tr, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        } else {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()),
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH())
                );

                int tx = (int) (x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int ty = (int) (y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int tr = (int) (r.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));

                c.color(currentColor);
                c.voidCircle(tx, ty, tr, tr, drawOptions.strokeWidth().apply(getCnvsW(), getCnvsH()), transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx - tr, ty - tr, tr, tr, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        }
    }

    private Consumer<DrawContext> parseEllipse(Parser parser, Transformations transformations, DrawOptions drawOptions) {
        var x = parseValue(parser.attrib("x", "0"), true);
        var y = parseValue(parser.attrib("y", "0"), false);
        var rx = parseValue(parser.attrib("radiusX", "10"), true);
        var ry = parseValue(parser.attrib("radiusY", "20"), false);

        if (drawOptions.filled()) {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()),
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH())
                );

                int tx = (int) (x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int ty = (int) (y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int trax = (int) (rx.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int tray = (int) (ry.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH()));

                c.color(currentColor);
                c.ellipse(tx, ty, trax, tray, Math.max(trax, tray), transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx - trax, ty - tray, trax, tray, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        } else {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()),
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH())
                );

                int tx = (int) (x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int ty = (int) (y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int trax = (int) (rx.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int tray = (int) (ry.apply(getCnvsW(), getCnvsH()) * transformations.scaleY().apply(getCnvsW(), getCnvsH()));

                c.color(currentColor);
                c.voidEllipse(tx, ty, trax, tray, drawOptions.strokeWidth().apply(getCnvsW(), getCnvsH()), Math.max(trax, tray), transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx - trax, ty - tray, trax, tray, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        }
    }

    private Consumer<DrawContext> parseArc(Parser parser, Transformations transformations, DrawOptions drawOptions) {
        var x = parseValue(parser.attrib("x", "0"), true);
        var y = parseValue(parser.attrib("y", "0"), false);
        var r = parseValue(parser.attrib("radius", "10"), true);
        var rn = parseValue(parser.attrib("range", "90"), true);
        var s = parseValue(parser.attrib("start", "0"), true);

        if (drawOptions.filled()) {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()),
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH())
                );

                int tx = (int) (x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int ty = (int) (y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int tr = (int) (r.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int trn = rn.apply(getCnvsW(), getCnvsH());
                int ts = s.apply(getCnvsW(), getCnvsH());

                c.color(currentColor);
                c.arc(tx, ty, tr, trn, ts, tr / (360f / trn), transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx - tr, ty - tr, tr, tr, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        } else {
            return c -> {
                transformations.setOriginsIfNull(
                        (wi, he) -> x.apply(getCnvsW(), getCnvsH()),
                        (wi, he) -> y.apply(getCnvsW(), getCnvsH())
                );

                int tx = (int) (x.apply(getCnvsW(), getCnvsH()) + transformations.transX().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int ty = (int) (y.apply(getCnvsW(), getCnvsH()) + transformations.transY().apply(getCnvsW(), getCnvsH()) - transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int tr = (int) (r.apply(getCnvsW(), getCnvsH()) * transformations.scaleX().apply(getCnvsW(), getCnvsH()));
                int trn = rn.apply(getCnvsW(), getCnvsH());
                int ts = s.apply(getCnvsW(), getCnvsH());

                c.color(currentColor);
                c.voidArc(tx, ty, tr, drawOptions.strokeWidth().apply(getCnvsW(), getCnvsH()), trn, ts, tr / (360f / trn), transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                if (drawOptions.border() == null) return;
                drawOptions.border().draw(c, tx - tr, ty - tr, tr, tr, (float) transformations.rotation().apply(getCnvsW(), getCnvsH()), transformations.originX().apply(getCnvsW(), getCnvsH()), transformations.originY().apply(getCnvsW(), getCnvsH()));

                transformations.restoreOriginsToNull();
            };
        }
    }
}
