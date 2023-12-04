package dev.mv.engine.utils.buffer;

import dev.mv.engine.utils.ArrayUtils;
import dev.mv.engine.utils.ByteUtils;
import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.collection.Vec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Function;


public class DynamicByteBuffer implements Cloneable {

    private byte[] buffer;
    private boolean flipped, readonly;
    private Charset defaultCharset = StandardCharsets.US_ASCII;
    private int size, index;

    private DynamicByteBuffer() {
        buffer = new byte[0];
    }

    public DynamicByteBuffer(int startingCapacity) {
        buffer = new byte[startingCapacity];
    }

    public DynamicByteBuffer(byte... buffer) {
        this.buffer = buffer;
        this.size = buffer.length;
    }

    public DynamicByteBuffer(Byte[] buffer) {
        this.buffer = Utils.toPrimitive(buffer);
        this.size = buffer.length;
    }

    private void grow(int min) {
        if (buffer.length >= size + min) {
            return;
        }
        int newLen = Math.round(buffer.length * ArrayUtils.phi);
        if (newLen >= size + min) {
            buffer = Arrays.copyOf(buffer, newLen);
        }
        else {
            buffer = Arrays.copyOf(buffer, size + min);
        }
    }

    private void shrink() {
        if (buffer.length <= size) {
            return;
        }
        if (Math.round(buffer.length * ArrayUtils.inversePhi) >= size) {
            buffer = Arrays.copyOf(buffer, size);
        }
    }

    public void allocate(int extra) {
        buffer = Arrays.copyOf(buffer, size + extra);
    }

    public void shrinkToSize() {
        if (buffer.length != size) {
            buffer = Arrays.copyOf(buffer, size);
        }
    }

    public void fill(byte b) {
        Arrays.fill(buffer, size, buffer.length, b);
        size = buffer.length;
    }

    public DynamicByteBuffer push(byte b) {
        if (readonly) throw new IllegalStateException("Buffer is readonly!");
        grow(1);
        if (flipped) {
            System.arraycopy(buffer, 0, buffer, 1, size++);
            buffer[0] = b;
        }
        else {
            buffer[size++] = b;
        }
        return this;
    }

    public DynamicByteBuffer push(byte... bytes) {
        if (readonly) throw new IllegalStateException("Buffer is readonly!");
        grow(bytes.length);
        if (flipped) {
            System.arraycopy(buffer, 0, buffer, bytes.length, size);
            size += bytes.length;
            System.arraycopy(bytes, 0, buffer, 0, bytes.length);
        }
        else {
            System.arraycopy(bytes, 0, buffer, size, bytes.length);
            size += bytes.length;
        }
        return this;
    }

    public DynamicByteBuffer push(int i) {
        push(ByteUtils.toBytes(i));
        return this;
    }

    public DynamicByteBuffer push(int[] i) {
        push(ByteUtils.toBytes(i));
        return this;
    }

    public DynamicByteBuffer push(long l) {
        push(ByteUtils.toBytes(l));
        return this;
    }

    public DynamicByteBuffer push(long[] l) {
        push(ByteUtils.toBytes(l));
        return this;
    }

    public DynamicByteBuffer push(float f) {
        push(ByteUtils.toBytes(f));
        return this;
    }

    public DynamicByteBuffer push(float[] f) {
        push(ByteUtils.toBytes(f));
        return this;
    }

    public DynamicByteBuffer push(double d) {
        push(ByteUtils.toBytes(d));
        return this;
    }

    public DynamicByteBuffer push(double[] d) {
        push(ByteUtils.toBytes(d));
        return this;
    }

    public DynamicByteBuffer push(short s) {
        push(ByteUtils.toBytes(s));
        return this;
    }

    public DynamicByteBuffer push(short[] s) {
        push(ByteUtils.toBytes(s));
        return this;
    }

    public DynamicByteBuffer push(boolean[] b) {
        push(ByteUtils.packBooleans(b));
        return this;
    }

    public DynamicByteBuffer push(String s) {
        push(ByteUtils.escapeBytes(s, defaultCharset));
        return this;
    }

    public DynamicByteBuffer push(String s, Charset charset) {
        push(ByteUtils.escapeBytes(s, charset));
        return this;
    }

    public DynamicByteBuffer push(String[] s) {
        for (int i = 0; i < s.length; i++) {
            push(s[i]);
        }
        return this;
    }

    public DynamicByteBuffer push(String[] s, Charset charset) {
        for (int i = 0; i < s.length; i++) {
            push(s[i], charset);
        }
        return this;
    }

    public <T extends Number> DynamicByteBuffer push(T t) {
        if (t instanceof Byte) {
            push((Byte) t);
        } else if (t instanceof Integer) {
            push((Integer) t);
        } else if (t instanceof Long) {
            push((Long) t);
        } else if (t instanceof Float) {
            push((Float) t);
        } else if (t instanceof Double) {
            push((Double) t);
        } else if (t instanceof Short) {
            push((Short) t);
        }
        return this;
    }

    public <T extends Number> DynamicByteBuffer push(T[] t) {
        if (t instanceof Byte[]) {
            push(Utils.toPrimitive((Byte[]) t));
        } else if (t instanceof Integer[]) {
            push(Utils.toPrimitive((Integer[]) t));
        } else if (t instanceof Long[]) {
            push(Utils.toPrimitive((Long[]) t));
        } else if (t instanceof Float[]) {
            push(Utils.toPrimitive((Float[]) t));
        } else if (t instanceof Double[]) {
            push(Utils.toPrimitive((Double[]) t));
        } else if (t instanceof Short[]) {
            push(Utils.toPrimitive((Short[]) t));
        }
        return this;
    }

    public DynamicByteBuffer pushRaw(String s) {
        push(ByteUtils.toBytes(s, defaultCharset));
        return this;
    }

    public DynamicByteBuffer pushRaw(String s, Charset charset) {
        push(ByteUtils.toBytes(s, charset));
        return this;
    }

    public DynamicByteBuffer pushRaw(String[] s) {
        for (int i = 0; i < s.length; i++) {
            pushRaw(s[i]);
        }
        return this;
    }

    public DynamicByteBuffer pushRaw(String[] s, Charset charset) {
        for (int i = 0; i < s.length; i++) {
            pushRaw(s[i], charset);
        }
        return this;
    }

    public byte pop() {
        if (readonly) {
            return flipped ? buffer[index++] : buffer[size - (++index)];
        }
        else {
            byte b;
            if (flipped) {
                b = buffer[0];
                System.arraycopy(buffer, 1, buffer, 0, --size);
            }
            else {
                b = buffer[--size];
            }
            shrink();
            return b;
        }
    }

    public byte[] pop(int n) {
        byte[] bytes = new byte[n];
        if (readonly) {
            if (flipped) {
                System.arraycopy(buffer, index, bytes, 0, n);
            }
            else {
                System.arraycopy(buffer, size - index - n - 1, bytes, 0, n);
            }
            index += n;
        }
        else {
            if (flipped) {
                System.arraycopy(buffer, 0, bytes, 0, n);
                System.arraycopy(buffer, n, buffer, 0, size - n);
            }
            else {
                System.arraycopy(buffer, size - n, bytes, 0, n);
            }
            size -= n;
            shrink();
        }
        return bytes;
    }

    public byte peek() {
        return flipped ? buffer[index] : buffer[size - 1];
    }

    public byte[] peek(int n) {
        byte[] bytes = new byte[n];
        if (flipped) {
            System.arraycopy(buffer, index, bytes, 0, n);
        }
        else {
            System.arraycopy(buffer, size - index - n - 1, bytes, 0, n);
        }
        return bytes;
    }

    public int popInt() {
        return ByteUtils.intFromBytes(pop(4));
    }

    public long popLong() {
        return ByteUtils.longFromBytes(pop(8));
    }

    public float popFloat() {
        return ByteUtils.floatFromBytes(pop(4));
    }

    public double popDouble() {
        return ByteUtils.doubleFromBytes(pop(8));
    }

    public short popShort() {
        return ByteUtils.shortFromBytes(pop(2));
    }

    public boolean[] popBooleans() {
        return ByteUtils.unpackBooleans(pop());
    }

    public int[] popInts(int n) {
        if (!flipped) {
            return ByteUtils.intArrayFromBytes(pop(n * 4));
        }
        int[] ints = new int[n];
        for (int i = 0; i < n; i++) {
            ints[i] = popInt();
        }
        return ints;
    }

    public long[] popLongs(int n) {
        if (!flipped) {
            return ByteUtils.longArrayFromBytes(pop(n * 8));
        }
        long[] longs = new long[n];
        for (int i = 0; i < n; i++) {
            longs[i] = popLong();
        }
        return longs;
    }

    public float[] popFloats(int n) {
        if (!flipped) {
            return ByteUtils.floatArrayFromBytes(pop(n * 4));
        }
        float[] floats = new float[n];
        for (int i = 0; i < n; i++) {
            floats[i] = popFloat();
        }
        return floats;
    }

    public double[] popDoubles(int n) {
        if (!flipped) {
            return ByteUtils.doubleArrayFromBytes(pop(n * 8));
        }
        double[] doubles = new double[n];
        for (int i = 0; i < n; i++) {
            doubles[i] = popDouble();
        }
        return doubles;
    }

    public short[] popShorts(int n) {
        if (!flipped) {
            return ByteUtils.shortArrayFromBytes(pop(n * 2));
        }
        short[] shorts = new short[n];
        for (int i = 0; i < n; i++) {
            shorts[i] = popShort();
        }
        return shorts;
    }

    public boolean[] popBooleans(int n) {
        if (!flipped) {
            return ByteUtils.unpackBooleans(pop(n));
        }
        boolean[] booleans = new boolean[n * 8];
        for (int i = 0; i < n; i++) {
            boolean[] popped = popBooleans();
            booleans[i * 8] = popped[0];
            booleans[i * 8 + 1] = popped[1];
            booleans[i * 8 + 2] = popped[2];
            booleans[i * 8 + 3] = popped[3];
            booleans[i * 8 + 4] = popped[4];
            booleans[i * 8 + 5] = popped[5];
            booleans[i * 8 + 6] = popped[6];
            booleans[i * 8 + 7] = popped[7];
        }
        return booleans;
    }

    public String popString(int len) {
        String ret = ByteUtils.stringFromBytes(defaultCharset, pop(len));
        pop();
        return ret;
    }

    public String popString(int len, Charset charset) {
        String ret = ByteUtils.stringFromBytes(charset, pop(len));
        pop();
        return ret;
    }

    public String popEscapedString() {
        int len = nextEscapedStringLength();
        return popString(len);
    }

    public String popEscapedString(Charset charset) {
        int len = nextEscapedStringLength();
        return popString(len, charset);
    }

    private int nextEscapedStringLength() {
        int len = 0;

        if (flipped) {
            while (buffer[index + len] != 0 && index + len < size) {
                len++;
            }
        }
        else {
            while (buffer[size - index - len - 1] != 0 && size - len - 1 >= index) {
                len++;
            }
        }

        return len;
    }

    public String popStringRaw(int len) {
        return ByteUtils.stringFromBytes(defaultCharset, pop(len));
    }

    public String popStringRaw(int len, Charset charset) {
        return ByteUtils.stringFromBytes(charset, pop(len));
    }

    public int peekInt() {
        return ByteUtils.intFromBytes(peek(4));
    }

    public long peekLong() {
        return ByteUtils.longFromBytes(peek(8));
    }

    public float peekFloat() {
        return ByteUtils.floatFromBytes(peek(4));
    }

    public double peekDouble() {
        return ByteUtils.doubleFromBytes(peek(8));
    }

    public short peekShort() {
        return ByteUtils.shortFromBytes(peek(2));
    }

    public boolean[] peekBooleans() {
        return ByteUtils.unpackBooleans(peek());
    }

    public String peekString(int len) {
        return ByteUtils.stringFromBytes(defaultCharset, peek(len));
    }

    public String peekString(int len, Charset charset) {
        return ByteUtils.stringFromBytes(charset, peek(len));
    }

    public int size() {
        return size;
    }

    public byte[] array() {
        return Arrays.copyOf(buffer, size);
    }

    public <R> R map(Function<DynamicByteBuffer, R> mapper) {
        return mapper.apply(this);
    }

    public DynamicByteBuffer flip() {
        flipped = !flipped;
        index = 0;
        return this;
    }

    public DynamicByteBuffer readonly() {
        readonly = true;
        index = 0;
        return this;
    }

    public DynamicByteBuffer readwrite() {
        readonly = false;
        index = 0;
        return this;
    }

    public DynamicByteBuffer reverse() {
        ArrayUtils.flip(buffer);
        return this;
    }

    public DynamicByteBuffer clear() {
        buffer = new byte[0];
        size = 0;
        return this;
    }

    public int length() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    public void setDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public DynamicByteBuffer[] splitInto(int amount) {
        if (amount <= 0) throw new IndexOutOfBoundsException("Parts must be greater than to 0!");
        if (amount == 1) return new DynamicByteBuffer[]{clone()};
        DynamicByteBuffer[] parts = new Vec<DynamicByteBuffer>(amount).fill(DynamicByteBuffer::new).asArray();
        if (amount >= buffer.length) {
            for (int i = 0; i < amount; i++) {
                if (i < buffer.length) {
                    parts[i].buffer = new byte[]{buffer[i]};
                }
            }
            return parts;
        }
        int index = 0;
        int splitDataLength = buffer.length / amount;
        int extra = buffer.length % amount;

        for (int i = 0; i < amount; i++) {
            int length = splitDataLength;
            if (extra > 0) {
                length++;
                extra--;
            }
            parts[i].buffer = Arrays.copyOfRange(buffer, index, index + length);
            index += length;
        }

        return parts;
    }

    public byte[][] splitIntoBytes(int amount) {
        DynamicByteBuffer[] parts = splitInto(amount);
        return new Vec<>(parts).fastIter().map(DynamicByteBuffer::array).toArray(byte[].class);
    }

    public DynamicByteBuffer merge(DynamicByteBuffer other) {
        if (other == null) return this;
        if (other.isEmpty()) return this;
        push(other.array());
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof DynamicByteBuffer buf) {
            if (!defaultCharset.equals(buf.defaultCharset)) return false;
            if (size != buf.size) return false;
            return Arrays.equals(array(), buf.array());
        }
        return false;
    }

    @Override
    public DynamicByteBuffer clone() {
        DynamicByteBuffer buf = new DynamicByteBuffer();
        buf.defaultCharset = defaultCharset;
        buf.buffer = Arrays.copyOf(buffer, size);
        buf.flipped = flipped;
        buf.readonly = readonly;
        buf.size = size;
        buf.index = index;
        return buf;
    }
}
