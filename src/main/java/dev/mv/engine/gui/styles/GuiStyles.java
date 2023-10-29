package dev.mv.engine.gui.styles;

import dev.mv.engine.gui.Origin;
import dev.mv.engine.gui.styles.value.GuiValue;
import dev.mv.engine.render.shared.Gradient;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.resources.types.border.Border;
import org.joml.Vector2i;

public class GuiStyles {
    public GuiValue<Integer> x, y, width, height;
    public GuiValue<Gradient> backgroundColor, foregroundColor, textColor, borderColor;
    public GuiValue<Integer> textSize;
    public GuiValue<Boolean> textChroma;
    public GuiValue<Float> textChromaTilt, textChromaCompress;
    public GuiValue<BitmapFont> font;
    public GuiValue<BorderStyle> borderStyle;
    public GuiValue<Border> customBorder;
    public GuiValue<Integer> borderWidth, borderRadius;
    public GuiValue<Integer> marginTop, marginBottom, marginLeft, marginRight;
    public GuiValue<Integer> paddingTop, paddingBottom, paddingLeft, paddingRight;
    public GuiValue<ViewState> viewState;
    public GuiValue<Origin> origin, rotationCenter;
    public GuiValue<Vector2i> customRotationCenter, customOrigin;
    public GuiValue<Positioning> positioning;
}
