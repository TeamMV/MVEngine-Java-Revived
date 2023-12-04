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
        border.setDefault();
        text.setDefault();
        transform.setDefault();
        return this;
    }
}
