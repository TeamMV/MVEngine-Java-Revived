package dev.mv.engine.gui.style;

import dev.mv.engine.gui.style.value.GuiValue;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.render.shared.graphics.Scale;
import dev.mv.engine.utils.Default;

public class GuiTransformationStyle implements Default<GuiTransformationStyle> {
    public GuiValue<Float> rotation;
    public GuiValue<Scale> translation;

    @Override
    public GuiTransformationStyle setDefault() {
        rotation = new GuiValueJust<>(0f);
        translation = new GuiValueJust<>(Scale.equal(0));
        return this;
    }
}
