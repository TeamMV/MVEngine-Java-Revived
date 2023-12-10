package dev.mv.engine.gui.elements;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.gui.Gui;
import dev.mv.engine.gui.Origin;
import dev.mv.engine.gui.events.GuiEvents;
import dev.mv.engine.gui.events.listeners.GuiEnterListener;
import dev.mv.engine.gui.events.listeners.GuiLeaveListener;
import dev.mv.engine.gui.style.BorderStyle;
import dev.mv.engine.gui.style.GuiPseudos;
import dev.mv.engine.gui.style.GuiStyle;
import dev.mv.engine.gui.style.Positioning;
import dev.mv.engine.gui.style.value.ResolveContext;
import dev.mv.engine.input.InputProcessor;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.render.shared.font.Font;
import dev.mv.engine.render.shared.graphics.Point;
import dev.mv.engine.render.shared.graphics.Scale;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.resources.R;
import dev.mv.engine.resources.types.border.*;
import dev.mv.engine.resources.types.drawable.Drawable;

import java.util.Arrays;

public abstract class GuiElement implements InputProcessor {
    protected int x, y, bgX, bgY, conX, conY;
    private int relX, relY;
    protected int width, height; //with all
    protected int contentWidth, contentHeight; //without all
    protected int boundingWidth, boundingHeight; //with just padding
    protected boolean inputEnabled = true;
    protected int zIndex = -1;
    protected Gui gui;

    protected int[] margins, paddings;

    public GuiEvents event;
    public GuiStyle style;
    public GuiPseudos pseudo;

    private GuiStyle backupStyle;

    private Border border;

    protected int rotationCenterX, rotationCenterY;
    protected float rotation;

    protected GuiElement parent = null;
    protected ResolveContext resolveContext;

    protected Window window;

    public GuiElement() {
        style = new GuiStyle();
        backupStyle = new GuiStyle();
        style.setDefault();
        event = new GuiEvents(this);
        resolveContext = new ResolveContext();
        pseudo = new GuiPseudos();

        event.enter((caller, mx, my) -> {
            backupStyle.overlay(style);
            style.overlay(pseudo.hover);
            if (style.cursor != null && !style.cursor.isNone()) {
                window.setCursor(style.cursor.resolve(resolveContext));
            }
        });

        event.leave((caller, mx, my) -> {
            style.overlay(backupStyle);
            if (style.cursor != null && !style.cursor.isNone()) {
                window.setCursor(style.cursor.resolve(resolveContext));
            }
        });
    }

    protected void calculateValues(DrawContext ctx) {
        window = ctx.getWindow();
        resolveContext.parent = parent;
        resolveContext.dpi = ctx.getWindow().dpi();

        paddings = new int[] {
            style.padding.top.resolve(resolveContext),
            style.padding.bottom.resolve(resolveContext),
            style.padding.left.resolve(resolveContext),
            style.padding.right.resolve(resolveContext)
        };
        margins = new int[] {
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

        bgX = x + margins[2];
        bgY = y + margins[1];
        conX = bgX + paddings[2];
        conY = bgY + paddings[1];

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
        border.draw(ctx, bgX, bgY, boundingWidth, boundingHeight, 0f, rotationCenterX, rotationCenterY);
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
            bg.draw(ctx, bgX, bgY, rotation, rotationCenterX, rotationCenterY);
        } else {
            ctx.color(style.backgroundColor.resolve(resolveContext));
            ctx.rectangle(bgX, bgY, boundingWidth, boundingHeight, rotation, rotationCenterX, rotationCenterY);
        }
        drawBorder(ctx);
    }

    public void draw(DrawContext ctx, int zIndex) {
        if (this.zIndex != zIndex) return;
        draw(ctx);
    }

    public abstract void draw(DrawContext ctx);

    public void drawDebug(DrawContext ctx) {
        int s = 20;

        draw(ctx);
        //margin frame
        ctx.color(255, 0, 0, 100);
        ctx.rectangle(x, y, width, height);
        //content frame
        ctx.color(0, 0, 255, 100);
        ctx.rectangle(conX, conY, contentWidth, contentHeight);

        ctx.color(Color.WHITE);
        Font font = R.font.get("mvengine.default");
        ctx.font(font);
        //display margins
        ctx.text(false, x, y + height / 2, s, margins[2] + "");
        ctx.text(false, x + width - font.getWidth(margins[3] + "", s), y + height / 2, s, margins[3] + "");
        ctx.text(false, x + width / 2, y + height - s, s, margins[0] + "");
        ctx.text(false, x + width / 2, y, s, margins[1] + "");

        //display paddings
        ctx.text(false, bgX, bgY + boundingHeight / 2, s, paddings[2] + "");
        ctx.text(false, bgX + boundingWidth - font.getWidth(paddings[3] + "", s), bgY + boundingHeight / 2, s, paddings[3] + "");
        ctx.text(false, bgX + boundingWidth / 2, bgY + boundingHeight - s, s, paddings[0] + "");
        ctx.text(false, bgX + boundingWidth / 2, bgY, s, paddings[1] + "");

        //display sizes
        ctx.text(false, x, y, s, width + " x " + height);
        ctx.text(false, bgX, bgY, s, boundingWidth + " x " + boundingHeight);
        ctx.text(false, conX, conY, s, contentWidth + " x " + contentHeight);
    }

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

    public int getBgX() {
        return bgX;
    }

    public int getBgY() {
        return bgY;
    }

    public int getConX() {
        return conX;
    }

    public int getConY() {
        return conY;
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

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        zIndex = Math.max(0, zIndex);
        if (gui != null) {
            gui.setZ(zIndex);
        }
        this.zIndex = zIndex;
    }

    public void setZIndexIfAbsent(int zIndex) {
        if (this.zIndex == -1) {
            this.zIndex = zIndex;
            if (gui != null) {
                gui.setZ(zIndex);
            }
        }
    }

    public Gui getGui() {
        return gui;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public boolean inside(int x, int y) {
        if (gui != null) {
            for (GuiElement e : gui.elements()) {
                if (e.getZIndex() > zIndex) {
                    if (e.inside(x, y)) {
                        return false;
                    }
                }
            }
        }

        return x > this.x && x < this.x + this.width &&
                y > this.y && y < this.y + this.height;
    }

    @Override
    public void keyPress(int key) {
        if (!inputEnabled) return;
        event.keyPress(key);
    }

    @Override
    public void keyType(char key) {
        if (!inputEnabled) return;
        event.keyType(key);
    }

    @Override
    public void keyRelease(int key) {
        if (!inputEnabled) return;
        event.keyRelease(key);
    }

    @Override
    public void mousePress(int btn) {
        if (!inputEnabled) return;
        event.mousePress(btn);
    }

    @Override
    public void mouseRelease(int btn) {
        if (!inputEnabled) return;
        event.mouseRelease(btn);
    }

    @Override
    public void mouseMoveX(int x, int prev) {
        if (!inputEnabled) return;
        event.mouseMoveX(x, prev);
    }

    @Override
    public void mouseMoveY(int y, int prev) {
        if (!inputEnabled) return;
        event.mouseMoveY(y, prev);
    }

    @Override
    public void mouseScrollX(float value) {
        if (!inputEnabled) return;
        event.mouseScrollX(value);
    }

    @Override
    public void mouseScrollY(float value) {
        if (!inputEnabled) return;
        event.mouseScrollY(value);
    }

    @Override
    public void setEnabled(boolean enabled) {
        inputEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return inputEnabled;
    }
}
