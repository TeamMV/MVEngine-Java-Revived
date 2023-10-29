package dev.mv.engine.resources.types;

import dev.mv.engine.parsing.Parser;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.engine.render.shared.texture.TextureRegion;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpriteCollection {
    private Map<String, TextureRegion> sprites;
    private int width, height, count;
    private Point start, end;

    public SpriteCollection(Texture texture, Point start, int width, int height, int count) {
        sprites = new LinkedHashMap<>();
        this.start = start;
        this.count = count;
        this.width = width;
        this.height = height;

        int x = start.x;
        int y = start.y;
        int idx = 0;

        for (int i = 0; i < count; i++) {
            TextureRegion region = texture.cutRegion(x, y, width, height);
            sprites.put(String.valueOf(idx++), region);
            if (x + width >= texture.getWidth()) {
                x = 0;
                y += height;
            }
            x += width;
        }

        end = new Point(x, y);
    }

    public SpriteCollection(Texture texture, Point start, int width, int height, Parser parser) {
        sprites = new LinkedHashMap<>();
        this.start = start;
        count = parser.count();
        this.width = width;
        this.height = height;

        int x = start.x;
        int y = start.y;

        do {
            String name = parser.requireAttrib("name");
            TextureRegion region = texture.cutRegion(x, y, width, height);
            sprites.put(name, region);
            if (x + width >= texture.getWidth()) {
                x = 0;
                y += height;
            }
        } while (parser.advance());

        end = new Point(x, y);
    }

    public Point getStartCoords() {
        return start;
    }

    public Point getEndCoords() {
        return end;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCount() {
        return count;
    }

    public TextureRegion getSprite(String name) {
        return sprites.get(name);
    }

    public TextureRegion getSprite(int idx) {
        return sprites.values().toArray(TextureRegion[]::new)[idx];
    }

    public int getAmount() {
        return sprites.size();
    }
}
