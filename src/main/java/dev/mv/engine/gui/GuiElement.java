package dev.mv.engine.gui;

import dev.mv.engine.gui.styles.GuiStyles;
import dev.mv.engine.gui.styles.Positioning;
import dev.mv.engine.gui.styles.value.ResolveContext;
import dev.mv.engine.render.shared.DrawContext;
import org.joml.Vector2i;

public class GuiElement {
    private int x, y;
    private int width, height;
    private int contentWidth, contentHeight;
    private int boundingWidth, boundingHeight;

    public GuiStyles style;

    private int rotationCenterX, rotationCenterY;

    private GuiElement parent = null;
    private ResolveContext resolveContext;

    public GuiElement() {
        style = new GuiStyles();
        resolveContext = new ResolveContext();
    }

    private void calculateValues() {
        int[] paddings = new int[] {
            style.paddingTop.resolve(resolveContext),
            style.paddingBottom.resolve(resolveContext),
            style.paddingLeft.resolve(resolveContext),
            style.paddingRight.resolve(resolveContext)
        };
        int[] margins = new int[] {
            style.marginTop.resolve(resolveContext),
            style.marginBottom.resolve(resolveContext),
            style.marginLeft.resolve(resolveContext),
            style.marginRight.resolve(resolveContext)
        };

        boundingWidth = contentWidth + paddings[2] + paddings[3];
        width = boundingWidth + margins[2] + margins[3];
        boundingHeight = contentHeight + paddings[0] + paddings[1];
        height = boundingHeight + margins[0] + margins[1];

        Origin origin = style.origin.resolve(resolveContext);
        Positioning positioning = style.positioning.resolve(resolveContext);
        if (positioning == Positioning.ABSOLUTE) {
            int x = style.x.resolve(resolveContext);
            int y = style.y.resolve(resolveContext);
            Vector2i customOrigin = style.customOrigin.resolve(resolveContext);
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
        }

        Vector2i customRotationCenter = style.customRotationCenter.resolve(resolveContext);
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

    protected void drawElementBody(DrawContext context) {
        int border_radius = style.borderRadius.resolve(resolveContext);
    }

    public void setParent(GuiElement parent) {
        this.parent = parent;
        resolveContext.parent = parent;
    }
}
