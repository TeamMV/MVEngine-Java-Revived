package dev.mv.engine.gui.style;

import dev.mv.engine.gui.style.value.GuiValue;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.gui.style.value.GuiValueMeasurement;
import dev.mv.engine.gui.style.value.GuiValueNone;
import dev.mv.engine.logic.unit.Unit;
import dev.mv.engine.render.shared.graphics.Scale;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.resources.types.border.Border;
import dev.mv.engine.resources.types.border.RectangleBorder;
import dev.mv.engine.utils.Default;

public class GuiBorderStyle implements Default<GuiBorderStyle> {
    public GuiValue<Color> color;
    public GuiValue<BorderStyle> style;
    public GuiValue<Border> custom;
    public GuiValue<Integer> width;
    public GuiValue<Scale> radius;

    @Override
    public GuiBorderStyle setDefault() {
        color = new GuiValueJust<>(Color.BLACK);
        style = new GuiValueJust<>(BorderStyle.SQUARE);
        custom = new GuiValueNone<>();
        width = new GuiValueMeasurement<>(3, Unit.PX);
        radius = new GuiValueJust<>(Scale.equal(0));
        return this;
    }

    public void overlay(GuiBorderStyle other) {
        if (other.color != null) color = other.color;
        if (other.style != null) style = other.style;
        if (other.custom != null) custom = other.custom;
        if (other.width != null) width = other.width;
        if (other.radius != null) radius = other.radius;
    }
}
