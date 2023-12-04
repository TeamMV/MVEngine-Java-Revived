package dev.mv.engine.parsing;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.exceptions.ParserInvalidAttribException;
import dev.mv.engine.utils.Utils;

import java.io.InputStream;

public interface Parser extends Copyable<Parser> {
    void load(InputStream stream);

    String root();

    default Parser requireRoot(String expected) {
        if (!root().equals(expected)) {
            Exceptions.send("INVALID_TAG", "root", expected, root());
        }

        return this;
    }

    default void requireCurrent(String expected) {
        if (!current().equals(expected)) {
            Exceptions.send("INVALID_TAG", "tag", expected, current());
        }
    }

    boolean advance();

    int count();

    Parser inner();

    boolean hasInner();

    String text();

    String current();

    String attrib(String name);

    default String requireAttrib(String name) {
        if (!hasAttrib(name)) {
            Exceptions.send(new IllegalStateException("<" + current() + "> must have the \"" + name + "\" attribute!"));
        }
        return attrib(name);
    }

    default String attrib(String name, String defaultValue) {
        return Utils.ifNotNull(attrib(name)).thenReturn().otherwiseReturn(defaultValue).getGenericReturnValue().value();
    }

    default boolean hasAttrib(String name) {
        return attrib(name) != null;
    }

    default int intAttrib(String name) {
        try {
            return Integer.parseInt(attrib(name, "bre"));
        } catch (Exception ignore) {
            Exceptions.send(new ParserInvalidAttribException("Failed to get int from attribute \"" + name + "\", returning 0."));
            return 0;
        }
    }

    default int intAttrib(String name, int defaultValue) {
        try {
            return Integer.parseInt(attrib(name, "bre"));
        } catch (Exception ignore) {
            return defaultValue;
        }
    }

    default float floatAttrib(String name) {
        try {
            return Float.parseFloat(attrib(name, "bre"));
        } catch (Exception ignore) {
            Exceptions.send(new ParserInvalidAttribException("Failed to get float from attribute \"" + name + "\", returning 0."));
            return 0;
        }
    }

    default float floatAttrib(String name, float defaultValue) {
        try {
            return Float.parseFloat(attrib(name, "bre"));
        } catch (Exception ignore) {
            return defaultValue;
        }
    }
}
