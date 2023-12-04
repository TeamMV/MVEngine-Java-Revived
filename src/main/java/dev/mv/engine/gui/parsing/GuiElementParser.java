package dev.mv.engine.gui.parsing;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.gui.elements.Attrib;
import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.elements.GuiParsable;
import dev.mv.engine.parsing.Parser;
import dev.mv.utils.Utils;
import dev.mv.utils.collection.Vec;
import dev.mv.utils.generic.pair.Pair;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiElementParser {
    private Parser parser;
    private Map<String, Pair<Class<? extends GuiElement>, Map<String, String>>> targets;

    public GuiElementParser() {
        targets = new HashMap<>();
    }

    public void prepare() {
        Vec<Class<?>> classes = Utils.getAllClasses(i -> !Utils.containsAny(i, "dev.mv.engine",
                "dev.mv.utils", "org.lwjgl", "de.fabmax.physxjni", "physx.",
                "org.joml", "com.codedisaster.steamworks", "javax.annotation",
                "org.jetbrains.annotations", "org.intellij", "com.intellij"));
        for (Class<?> clazz : classes) {
            if (GuiElement.class.isAssignableFrom(clazz)) {
                if (clazz.isAnnotationPresent(GuiParsable.class)) {
                    GuiParsable parsable = clazz.getAnnotation(GuiParsable.class);
                    String tagName = parsable.tagName();
                    Attrib[] attribs = parsable.attribs();
                    Map<String, String> attribMap = new HashMap<>();
                    for (Attrib attrib : attribs) {
                        attribMap.put(attrib.name(), attrib.field());
                    }
                    targets.put(tagName, new Pair<>((Class<? extends GuiElement>) clazz, attribMap));
                }
            }
        }
    }

    public void load(Parser parser) {

    }

    private GuiElement handleElementTag(Parser parser) {
        if (targets.containsKey(parser.current())) {
            try {
                var found = targets.get(parser.current());
                GuiElement instance = found.a.getDeclaredConstructor().newInstance();
                for (var entry : found.b.entrySet()) {
                    String name = entry.getKey();
                    String value;
                    if (parser.hasAttrib(name)) {
                        value = parser.attrib(name);
                    } else {
                        continue;
                    }
                    String fieldName = entry.getValue();
                    Field field = found.a.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(instance, value);
                }
                return instance;
            } catch (Exception e) {
                Exceptions.send(e);
            }
        }

        return null;
    }
}
