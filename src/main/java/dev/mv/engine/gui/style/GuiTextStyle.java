package dev.mv.engine.gui.style;

import dev.mv.engine.gui.style.value.GuiValue;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.gui.style.value.GuiValueMeasurement;
import dev.mv.engine.logic.unit.Unit;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.render.shared.font.Font;
import dev.mv.engine.resources.R;
import dev.mv.engine.utils.Default;

public class GuiTextStyle implements Default<GuiTextStyle> {
    public GuiValue<Color> color;
    public GuiValue<Integer> size;
    public GuiValue<Boolean> chroma;
    public GuiValue<Float> chromaTilt, chromaCompress;
    public GuiValue<Font> font;

    @Override
    public GuiTextStyle setDefault() {
        color = new GuiValueJust<>(Color.BLACK);
        size = new GuiValueMeasurement<>(1, Unit.CM);
        chroma = new GuiValueJust<>(false);
        chromaTilt = new GuiValueJust<>(0f);
        chromaCompress = new GuiValueJust<>(0f);
        font = new GuiValueJust<>(R.font.get("mvengine.default"));
        return this;
    }

    public void overlay(GuiTextStyle other) {
        if (other.color != null) color = other.color;
        if (other.size != null) size = other.size;
        if (other.chroma != null) chroma = other.chroma;
        if (other.chromaTilt != null) chromaTilt = other.chromaTilt;
        if (other.chromaCompress != null) chromaCompress = other.chromaCompress;
        if (other.font != null) font = other.font;
    }
}
