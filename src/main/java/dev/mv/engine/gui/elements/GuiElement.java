package dev.mv.engine.gui.elements;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.gui.Origin;
import dev.mv.engine.gui.style.BorderStyle;
import dev.mv.engine.gui.style.GuiStyle;
import dev.mv.engine.gui.style.Positioning;
import dev.mv.engine.gui.style.value.ResolveContext;
import dev.mv.engine.render.shared.graphics.Point;
import dev.mv.engine.render.shared.graphics.Scale;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.resources.types.border.*;
import dev.mv.engine.resources.types.drawable.Drawable;

public abstract class GuiElement {
    protected int x, y;
    protected int contentX, contentY;//with padding
    private int relX, relY;
    protected int width, height; //with all
    protected int contentWidth, contentHeight; //without all
    protected int boundingWidth, boundingHeight; //with just padding

    public GuiStyle style;
    private Border border;

    protected int rotationCenterX, rotationCenterY;
    protected float rotation;

    protected GuiElement parent = null;
    protected ResolveContext resolveContext;

    public GuiElement() {
        style = new GuiStyle();
        style.setDefault();
        resolveContext = new ResolveContext();
    }

    protected void calculateValues(DrawContext ctx) {
        resolveContext.parent = parent;
        resolveContext.dpi = ctx.getWindow().dpi();

        int[] paddings = new int[] {
            style.padding.top.resolve(resolveContext),
            style.padding.bottom.resolve(resolveContext),
            style.padding.left.resolve(resolveContext),
            style.padding.right.resolve(resolveContext)
        };
        int[] margins = new int[] {
            style.margin.top.resolve(resolveContext),
            style.margin.bottom.resolve(resolveContext),
            style.margin.left.resolve(resolveContext),
            style.margin.right.resolve(resolveContext)
        };

        if (contentWidth < style.border.radius.resolve(resolveContext).x)

        if (!style.width.isAuto()) {
            contentWidth = style.width.resolve(resolveContext);
        }

        if (!style.height.isAuto()) {
            contentHeight = style.height.resolve(resolveContext);
        }

        boundingWidth = contentWidth + paddings[2] + paddings[3];
        width = boundingWidth + margins[2] + margins[3];
        boundingHeight = contentHeight + paddings[0] + paddings[1];
        height = boundingHeight + margins[0] + margins[1];

        Origin origin = style.origin.resolve(resolveContext);
        Positioning positioning = style.positioning.resolve(resolveContext);
        if (positioning == Positioning.ABSOLUTE) {
            x = style.x.resolve(resolveContext);
            y = style.y.resolve(resolveContext);
        } else {
            x = relX;
            y = relY;
        }

        Point customOrigin = style.customOrigin.resolve(resolveContext);
        if (customOrigin != null) {
            this.x = x + customOrigin.x;
            this.y = y + customOrigin.y;
        } else {
            if (origin != Origin.CENTER) {
                this.x = origin.isRight() ? x - width : x;
                this.y = origin.isTop() ? y - height : y;
            } else {
                this.x = x + width / 2;
                this.y = y + height / 2;
            }
        }

        contentX = x + paddings[2];
        contentY = y + paddings[1];

        Point customRotationCenter = style.customRotationCenter.resolve(resolveContext);
        if (customRotationCenter != null) {
            this.rotationCenterX = customRotationCenter.x;
            this.rotationCenterY = customRotationCenter.y;
        } else {
            Origin rotationCenter = style.rotationCenter.resolve(resolveContext);
            if (rotationCenter != Origin.CENTER) {
                this.rotationCenterX = rotationCenter.isRight() ? x - width : x;
                this.rotationCenterY = rotationCenter.isTop() ? y - height : y;
            } else {
                this.rotationCenterX = x + width / 2;
                this.rotationCenterY = y + height / 2;
            }
        }
    }

    public void moveTo(int x, int y) {
        relX = x;
        relY = y;
    }

    private void calcBorder() {
        int width = style.border.width.resolve(resolveContext);
        if (width <= 0) return;
        Color color = style.border.color.resolve(resolveContext);
        Scale radius = style.border.radius.resolve(resolveContext);

        BorderStyle borderStyle = style.border.style.resolve(resolveContext);
        if (borderStyle != BorderStyle.CUSTOM) {
            if (borderStyle == BorderStyle.ROUND && !(border instanceof RoundRectangleBorder)) {
                border = new RoundRectangleBorder(width, color, radius.x, radius.y);
            }
            if (borderStyle == BorderStyle.SQUARE && !(border instanceof RectangleBorder)) {
                border = new RectangleBorder(width, color);
            }
            if (borderStyle == BorderStyle.TRIANGLE && !(border instanceof TriangleRectangleBorder)) {
                border = new TriangleRectangleBorder(width, color, radius.x, radius.y);
            }
        } else {
            if (style.border.custom.isNone()) {
                Exceptions.send("GUI_VALUE_NONE", "style.border.custom", "when setting style.border.style to CUSTOM");
                border = null;
            } else {
                border = style.border.custom.resolve(resolveContext);
            }
        }

        if (border != null) {
            border.setColor(color);
            border.setStrokeWidth(width);
            if (border instanceof RadiusBorder radiusBorder) {
                if (radiusBorder.getRadiusX() != radius.x || radiusBorder.getRadiusY() != radius.y) {
                    radiusBorder.setRadius(radius.x, radius.y);
                }
            }
        }
    }

    private void drawBorder(DrawContext ctx) {
        border.draw(ctx, x, y, boundingWidth, boundingHeight, 0f, rotationCenterX, rotationCenterY);
    }

    protected void drawElementBody(DrawContext ctx) {
        calcBorder();

        //ctx.beginClip();
        //border.drawClip(ctx, x, y, boundingWidth, boundingHeight, 0f, rotationCenterX, rotationCenterY);
        //ctx.endClip();

        if (!style.background.isNone()) {
            Drawable bg = style.background.resolve(resolveContext);
            bg.setCnvsW(boundingWidth);
            bg.setCnvsH(boundingHeight);
            bg.draw(ctx, x, y, rotation, rotationCenterX, rotationCenterY);
        } else {
            ctx.color(style.backgroundColor.resolve(resolveContext));
            ctx.rectangle(x, y, boundingWidth, boundingHeight, rotation, rotationCenterX, rotationCenterY);
        }
        drawBorder(ctx);
    }

    public abstract void draw(DrawContext ctx);

    protected void setParent(GuiElement parent) {
        this.parent = parent;
        resolveContext.parent = parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getContentX() {
        return contentX;
    }

    public int getContentY() {
        return contentY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    public int getBoundingWidth() {
        return boundingWidth;
    }

    public int getBoundingHeight() {
        return boundingHeight;
    }

    public GuiElement getParent() {
        return parent;
    }
}
