package dev.mv.engine.render.shared.font;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.resources.ResourcePath;

import java.io.InputStream;
import java.util.Arrays;

public class Fonts {
    private static final byte[][] BMP = new byte[][]{new byte[]{(byte) 0x89, 0x50, 0x4e, 0x47}, new byte[]{0x69, 0x6e, 0x66, 0x6f}};

    public enum Type {
        UNSUPPORTED,
        BMP,
    }

    public static Type getTypeFromPaths(ResourcePath... paths) {
        byte[][] magics = new byte[paths.length][4];

        for (int i = 0; i < paths.length; i++) {
            try {
                InputStream stream = paths[i].getInputStream();
                stream.mark(4);
                magics[i] = stream.readNBytes(4);
                stream.reset();
            } catch (Exception e) {
                Exceptions.send(e);
            }
        }

        if (magics.length == 2) {
            if (Arrays.deepEquals(magics, BMP)) {
                return Type.BMP;
            }
        }

        return Type.UNSUPPORTED;
    }
}
