package dev.mv.engine.gui.style;

import dev.mv.engine.TimingManager;
import dev.mv.engine.gui.Origin;
import dev.mv.engine.gui.style.value.*;
import dev.mv.engine.logic.Interpolator;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.render.shared.graphics.Point;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.utils.Default;
import dev.mv.engine.utils.Utils;

public class GuiStyle implements Default<GuiStyle> {
    private int timingId;

    public GuiValue<Integer> x, y, width, height;
    public GuiValue<Color> backgroundColor;
    public GuiValue<Drawable> background;
    public Sides margin = new Sides();
    public Sides padding = new Sides();
    public GuiValue<ViewState> viewState;
    public GuiValue<Origin> origin, rotationCenter;
    public GuiValue<Point> customRotationCenter, customOrigin;
    public GuiValue<Positioning> positioning;
    public GuiValue<Cursor> cursor;
    public GuiBorderStyle border = new GuiBorderStyle();
    public GuiTextStyle text = new GuiTextStyle();
    public GuiTransformationStyle transform = new GuiTransformationStyle();

    @Override
    public GuiStyle setDefault() {
        x = new GuiValueNone<>();
        y = new GuiValueNone<>();
        width = new GuiValueAuto<>();
        height = new GuiValueAuto<>();
        backgroundColor = new GuiValueJust<>(Color.WHITE);
        background = new GuiValueNone<>();
        margin = new Sides().setDefault();
        padding = new Sides().setDefault();
        viewState = new GuiValueJust<>(ViewState.THERE);
        origin = new GuiValueJust<>(Origin.BOTTOM_LEFT);
        rotationCenter = new GuiValueJust<>(Origin.CENTER);
        customOrigin = new GuiValueNone<>();
        customRotationCenter = new GuiValueNone<>();
        positioning = new GuiValueJust<>(Positioning.RELATIVE);
        cursor = new GuiValueJust<>(Cursor.ARROW);
        border.setDefault();
        text.setDefault();
        transform.setDefault();
        return this;
    }

    public void overlay(GuiStyle other) {
        if (other.x != null) x = other.x;
        if (other.y != null) y = other.y;
        if (other.width != null) width = other.width;
        if (other.height != null) height = other.height;
        if (other.backgroundColor != null) backgroundColor = other.backgroundColor;
        if (other.background != null) background = other.background;
        if (other.margin != null) {
            if (other.margin.top != null) margin.top = other.margin.top;
            if (other.margin.bottom != null) margin.bottom = other.margin.bottom;
            if (other.margin.left != null) margin.left = other.margin.left;
            if (other.margin.right != null) margin.right = other.margin.right;
        }
        if (other.padding != null) {
            if (other.padding.top != null) padding.top = other.padding.top;
            if (other.padding.bottom != null) padding.bottom = other.padding.bottom;
            if (other.padding.left != null) padding.left = other.padding.left;
            if (other.padding.right != null) padding.right = other.padding.right;
        }
        if (other.viewState != null) viewState = other.viewState;
        if (other.origin != null) origin = other.origin;
        if (other.rotationCenter != null) rotationCenter = other.rotationCenter;
        if (other.customRotationCenter != null) customRotationCenter = other.customRotationCenter;
        if (other.customOrigin != null) customOrigin = other.customOrigin;
        if (other.positioning != null) positioning = other.positioning;
        if (other.cursor != null) cursor = other.cursor;
        if (other.border != null) border.overlay(other.border);
        if (other.text != null) text.overlay(other.text);
        if (other.transform != null) transform.overlay(other.transform);
    }

    public void transition(GuiStyle other, float duration, Window window, ResolveContext context) {
        if (duration == 0) {
            overlay(other);
            return;
        }
        timingId = TimingManager.queue(i -> {
            float timePassed = ((float) i * 1000f) / window.getFPS();
            float percentage = Utils.getPercent((int) timePassed, (int) duration);
            if (percentage >= 100f) {
                applyTime(other, 100, context);
                return false;
            }
            applyTime(other, percentage, context);
            return true;
        });
    }

    private void applyTime(GuiStyle other, float percentage, ResolveContext context) {
        x = applySingle(x, other.x, percentage, context);
        y = applySingle(y, other.y, percentage, context);
        width = applySingle(width, other.width, percentage, context);
        height = applySingle(height, other.height, percentage, context);

        backgroundColor = applySingle(backgroundColor, other.backgroundColor, percentage, context);
        background = applySingle(background, other.background, percentage, context);

        margin.top = applySingle(margin.top, other.margin.top, percentage, context);
        margin.bottom = applySingle(margin.bottom, other.margin.bottom, percentage, context);
        margin.left = applySingle(margin.left, other.margin.left, percentage, context);
        margin.right = applySingle(margin.right, other.margin.right, percentage, context);

        padding.top = applySingle(padding.top, other.padding.top, percentage, context);
        padding.bottom = applySingle(padding.bottom, other.padding.bottom, percentage, context);
        padding.left = applySingle(padding.left, other.padding.left, percentage, context);
        padding.right = applySingle(padding.right, other.padding.right, percentage, context);

        viewState = applySingle(viewState, other.viewState, percentage, context);
        origin = applySingle(origin, other.origin, percentage, context);
        rotationCenter = applySingle(rotationCenter, other.rotationCenter, percentage, context);
        customRotationCenter = applySingle(customRotationCenter, other.customRotationCenter, percentage, context);
        customOrigin = applySingle(customOrigin, other.customOrigin, percentage, context);
        positioning = applySingle(positioning, other.positioning, percentage, context);
        cursor = applySingle(cursor, other.cursor, percentage, context);

        border.width = applySingle(border.width, other.border.width, percentage, context);
        border.radius = applySingle(border.radius, other.border.radius, percentage, context);
        border.style = applySingle(border.style, other.border.style, percentage, context);
        border.color = applySingle(border.color, other.border.color, percentage, context);
        border.custom = applySingle(border.custom, other.border.custom, percentage, context);

        text.size = applySingle(text.size, other.text.size, percentage, context);
        text.font = applySingle(text.font, other.text.font, percentage, context);
        text.color = applySingle(text.color, other.text.color, percentage, context);
        text.chroma = applySingle(text.chroma, other.text.chroma, percentage, context);
        text.chromaCompress = applySingle(text.chromaCompress, other.text.chromaTilt, percentage, context);
        text.chromaTilt = applySingle(text.chromaTilt, other.text.chromaTilt, percentage, context);

        transform.rotation = applySingle(transform.rotation, other.transform.rotation, percentage, context);
        transform.translation = applySingle(transform.translation, other.transform.translation, percentage, context);
    }

    private <T> GuiValue<T> applySingle(GuiValue<T> self, GuiValue<T> other, float percentage, ResolveContext context) {
        if (other == null) return self;
        T start = self.resolve(context);
        T end = other.resolve(context);

        if (start instanceof Number s && end instanceof Number e) {
            if (percentage >= 100) {
                return other;
            }
            int intEnd = e.intValue();
            int intStart = s.intValue();
            float value = Utils.getValue(percentage, intEnd - intStart) + intEnd;
            return new GuiValueJust<>((T) (Object) value);
        } else if (start instanceof Interpolator<?>) {
            Interpolator<T> inter = (Interpolator<T>) start;
            if (percentage >= 100) {
                return other;
            }
            return new GuiValueJust<>(inter.interpolate(start, end, percentage));
        } else {
            return percentage > 50f ? self : other;
        }
    }
}
