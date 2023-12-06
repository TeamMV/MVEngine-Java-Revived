package dev.mv.engine.render.shared.font;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.engine.resources.Resource;
import dev.mv.engine.utils.CompositeInputStream;
import dev.mv.engine.utils.Utils;

import java.io.IOException;
import java.io.InputStream;

public interface Font extends Resource {

    int getSpacing();

    int getMaxHeight();

    int getMaxHeight(int height);

    int getHeight(char c);

    int getHeight(char c, int height);

    int getWidth(String s, int height);

    int possibleAmountOfChars(String s, int limitWidth, int height);

    int getMaxXOffset();

    int getMaxXOffset(int height);

    int getMaxYOffset();

    int getMaxYOffset(int height);

    Glyph getGlyph(char c);

    Glyph[] getGlyphs(String s);

    boolean contains(char c);

    Texture getTexture();
}
