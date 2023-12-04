package dev.mv.engine.gui.elements;

import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.resources.R;

@GuiParsable(tagName = "textLine", attribs = {
        @Attrib(name = "text", field = "text"),
        @Attrib(name = "font", field = "fontId"),
})
public class GuiTextLine extends GuiElement implements GuiTextElement {
    private String text;
    private String fontId;

    private BitmapFont font;

    public GuiTextLine() {
        font = R.font.get("mv.default");
    }

    @Override
    public void draw(DrawContext ctx) {
        int fontSize =  style.text.size.resolve(resolveContext);
        int width = font.getWidth(text, fontSize);
        contentWidth = width;
        contentHeight = fontSize;

        calculateValues(ctx);
        drawElementBody(ctx);

        ctx.color(style.text.color.resolve(resolveContext));
        ctx.chromaTilt(style.text.chromaTilt.resolve(resolveContext));
        ctx.chromaCompress(style.text.chromaCompress.resolve(resolveContext));
        boolean chroma = style.text.chroma.resolve(resolveContext);
        ctx.text(chroma, contentX, contentY, fontSize, text, font, rotation, rotationCenterX, rotationCenterY);
    }

    @Override
    public void setFont(BitmapFont font) {
        this.font = font;
    }

    @Override
    public BitmapFont getFont() {
        return font;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}