package dev.mv.engine.resources.types.custom;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.resources.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public abstract class CustomResource implements Resource {

    public static InputStream resourceStream(InputStream inputStream, Class<? extends CustomResource> clazz) {
        return new SequenceInputStream(new ByteArrayInputStream((clazz.getName() + "\n").getBytes(StandardCharsets.UTF_8)), inputStream);
    }
}
