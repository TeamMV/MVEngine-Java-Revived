package dev.mv.engine.resources.types;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.exceptions.UnimplementedException;
import dev.mv.engine.parsing.Parser;
import dev.mv.engine.parsing.XMLParser;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.engine.resources.Resource;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpriteSheet implements Resource {
    private String resId;
    private Map<String, SpriteCollection> collections;

    public SpriteSheet(Texture texture, InputStream desc) {
        collections = new HashMap<>();
        Parser parser = new XMLParser(desc).requireRoot("sprites");
        resId = parser.requireAttrib("resId");
        parser = parser.inner();
        Point following = new Point(0, 0);

        do {
            if (parser.current().equals("collection")) {
                if ((!parser.hasAttrib("size") && !parser.hasAttrib("width")) || (!parser.hasAttrib("size") && !parser.hasAttrib("height"))) {
                    Exceptions.send(new IllegalStateException("<collection> must either have the attribute \"size\" or \"width\" & \"height\"!"));
                    continue;
                }
                if (!parser.hasAttrib("count") && !parser.hasInner()) {
                    Exceptions.send(new IllegalStateException("<collection> must either have the attribute \"count\" or <sprite> tags as the children!"));
                    continue;
                }

                int width, height;

                if (!parser.hasAttrib("size")) {
                    width = parser.intAttrib("width");
                    height = parser.intAttrib("height");
                } else {
                    width = height = parser.intAttrib("size");
                }

                String startAtt = parser.requireAttrib("start");
                Point start = new Point();
                if (startAtt.equals("origin")) {
                    start.setLocation(0, 0);
                } else if (startAtt.equals("following")) {
                    start.setLocation(following);
                } else {
                    int[] coords = Arrays.stream(startAtt.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
                    start.setLocation(coords[0], coords[1]);
                }

                SpriteCollection collection;
                if (parser.hasAttrib("count")) {
                    collection = new SpriteCollection(texture, start, width, height, parser.intAttrib("count"));
                } else {
                    collection = new SpriteCollection(texture, start, width, height, parser);
                }

                collections.put(parser.requireAttrib("name"), collection);
            }
        } while (parser.advance());
    }

    public SpriteCollection getCollection(String name) {
        return collections.get(name);
    }

    @Override
    public String resId() {
        return resId;
    }

    @Override
    public Type type() {
        return Type.SPRITE_SHEET;
    }

    public SpriteSheet() {}

    @Override
    public void load(InputStream inputStream, String resId) throws IOException {
        throw new UnimplementedException();
    }
}
