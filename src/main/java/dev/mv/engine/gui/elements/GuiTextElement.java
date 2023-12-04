package dev.mv.engine.gui.elements;

import dev.mv.engine.render.shared.font.BitmapFont;

public interface GuiTextElement {
    void setFont(BitmapFont font);

    BitmapFont getFont();

    void setText(String text);

    String getText();
}
