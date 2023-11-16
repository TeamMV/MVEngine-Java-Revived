package dev.mv.engine.resources.types.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TestCustomResource extends CustomResource{
    public String string;

    public TestCustomResource() {}

    @Override
    public void load(InputStream inputStream) throws IOException {
        string = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
    }
}
