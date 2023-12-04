package dev.mv.engine.gui.style;

import dev.mv.engine.gui.style.value.GuiValue;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.utils.Default;

public class Sides implements Default<Sides> {
    public GuiValue<Integer> top, bottom, left, right;

    public Sides() {
    }

    public Sides(int top, int bottom, int left, int right) {
        this.top = new GuiValueJust<>(top);
        this.bottom = new GuiValueJust<>(bottom);
        this.left = new GuiValueJust<>(left);
        this.right = new GuiValueJust<>(right);
    }

    public void setInts(int top, int bottom, int left, int right) {
        this.top = new GuiValueJust<>(top);
        this.bottom = new GuiValueJust<>(bottom);
        this.left = new GuiValueJust<>(left);
        this.right = new GuiValueJust<>(right);
    }

    public void setInts(int value) {
        setInts(value, value, value, value);
    }

    @Override
    public Sides setDefault() {
        top = new GuiValueJust<>(5);
        bottom = new GuiValueJust<>(5);
        left = new GuiValueJust<>(5);
        right = new GuiValueJust<>(5);
        return this;
    }
}
