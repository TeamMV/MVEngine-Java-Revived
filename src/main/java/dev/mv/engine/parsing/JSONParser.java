package dev.mv.engine.parsing;

import java.io.InputStream;

public class JSONParser implements Parser{


    public JSONParser(InputStream inputStream) {

    }

    @Override
    public void load(InputStream stream) {

    }

    @Override
    public String root() {
        return null;
    }

    @Override
    public boolean advance() {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public Parser inner() {
        return null;
    }

    @Override
    public boolean hasInner() {
        return false;
    }

    @Override
    public String text() {
        return null;
    }

    @Override
    public String current() {
        return null;
    }

    @Override
    public String attrib(String name) {
        return null;
    }

    @Override
    public Parser copy() {
        return null;
    }
}
