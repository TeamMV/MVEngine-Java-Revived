package dev.mv.engine.utils;

import dev.mv.engine.exceptions.Exceptions;

import java.io.IOException;
import java.io.InputStream;

public class CompositeInputStream extends InputStream {
    private final InputStream[] streams;
    private int currentIdx = 0;

    public CompositeInputStream(InputStream... streams) {
        this.streams = streams;
    }

    @Override
    public int read() throws IOException {
        if (streams[currentIdx].available() <= 0) {
            currentIdx++;
            if (currentIdx >= streams.length) {
                throw new IOException("End of stream is reached!");
            }
        }
        return streams[currentIdx].read();
    }

    public int getCurrentIdx() {
        return currentIdx;
    }

    public InputStream get(int idx) {
        if (idx >= streams.length) {
            Exceptions.send(new ArrayIndexOutOfBoundsException("idx " + idx + " is too big for streams.length=" + streams.length));
            return null;
        }
        return streams[idx];
    }
}
