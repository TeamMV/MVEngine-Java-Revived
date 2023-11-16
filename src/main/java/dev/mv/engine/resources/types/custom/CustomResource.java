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
    protected String resId;

    @Override
    public void load(InputStream inputStream, String resId) throws IOException {
        this.resId = resId;
        load(inputStream);
    }

    @Override
    public abstract void load(InputStream inputStream) throws IOException;

    @Override
    public Type type() {
        return Type.RESOURCE;
    }

    @Override
    public String resId() {
        return resId;
    }

    public static InputStream resourceStream(InputStream inputStream, Class<? extends CustomResource> clazz) {
        return new SequenceInputStream(new ByteArrayInputStream((clazz.getName() + "\n").getBytes(StandardCharsets.UTF_8)), inputStream);
    }
}
