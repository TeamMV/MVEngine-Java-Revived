package dev.mv.engine.gui.style;

import dev.mv.engine.gui.Origin;
import dev.mv.engine.gui.style.value.GuiValue;
import dev.mv.engine.gui.style.value.GuiValueAuto;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.gui.style.value.GuiValueNone;
import dev.mv.engine.render.shared.graphics.Point;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.utils.Default;

public class GuiStyle implements Default<GuiStyle> {
    public GuiValue<Integer> x, y, width, height;
    public GuiValue<Color> backgroundColor;
    public GuiValue<Drawable> background;
    public Sides margin;
    public Sides padding;
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
        if (other.margin != null) margin = other.margin;
        if (other.padding != null) padding = other.padding;
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
}
