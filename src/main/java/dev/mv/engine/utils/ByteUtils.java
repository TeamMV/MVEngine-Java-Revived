package dev.mv.engine.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteUtils {

    private ByteUtils() {
    }

    public static byte[] toBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value & 0xff000000) >> 24);
        bytes[1] = (byte) ((value & 0x00ff0000) >> 16);
        bytes[2] = (byte) ((value & 0x0000ff00) >> 8);
        bytes[3] = (byte) ((value & 0x000000ff));
        return bytes;
    }

    public static byte[] toBytes(long value) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((value & 0xff00000000000000L) >> 54);
        bytes[1] = (byte) ((value & 0x00ff000000000000L) >> 48);
        bytes[2] = (byte) ((value & 0x0000ff0000000000L) >> 40);
        bytes[3] = (byte) ((value & 0x000000ff00000000L) >> 32);
        bytes[4] = (byte) ((value & 0x00000000ff000000L) >> 24);
        bytes[5] = (byte) ((value & 0x0000000000ff0000L) >> 16);
        bytes[6] = (byte) ((value & 0x000000000000ff00L) >> 8);
        bytes[7] = (byte) ((value & 0x00000000000000ffL));
        return bytes;
    }

    public static byte[] toBytes(float value) {
        return toBytes(Float.floatToIntBits(value));
    }

    public static byte[] toBytes(double value) {
        return toBytes(Double.doubleToLongBits(value));
    }

    public static byte[] toBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((value & 0xff00) >> 8);
        bytes[1] = (byte) ((value & 0x00ff));
        return bytes;
    }

    public static byte[] toBytes(int[] value) {
        byte[] bytes = new byte[value.length * 4];
        for (int i = 0; i < value.length; i++) {
            bytes[i * 4] = (byte) ((value[i] & 0xff000000) >> 24);
            bytes[i * 4 + 1] = (byte) ((value[i] & 0x00ff0000) >> 16);
            bytes[i * 4 + 2] = (byte) ((value[i] & 0x0000ff00) >> 8);
            bytes[i * 4 + 3] = (byte) ((value[i] & 0x000000ff));
        }
        return bytes;
    }

    public static byte[] toBytes(long[] value) {
        byte[] bytes = new byte[value.length * 8];
        for (int i = 0; i < value.length; i++) {
            bytes[i * 8] = (byte) ((value[i] & 0xff00000000000000L) >> 56);
            bytes[i * 8 + 1] = (byte) ((value[i] & 0x00ff000000000000L) >> 48);
            bytes[i * 8 + 2] = (byte) ((value[i] & 0x0000ff0000000000L) >> 40);
            bytes[i * 8 + 3] = (byte) ((value[i] & 0x000000ff00000000L) >> 32);
            bytes[i * 8 + 4] = (byte) ((value[i] & 0x00000000ff000000L) >> 24);
            bytes[i * 8 + 5] = (byte) ((value[i] & 0x0000000000ff0000L) >> 16);
            bytes[i * 8 + 6] = (byte) ((value[i] & 0x000000000000ff00L) >> 8);
            bytes[i * 8 + 7] = (byte) ((value[i] & 0x00000000000000ffL));
        }
        return bytes;
    }

    public static byte[] toBytes(float[] value) {
        int[] ints = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            ints[i] = Float.floatToIntBits(value[i]);
        }
        return toBytes(ints);
    }

    public static byte[] toBytes(double[] value) {
        long[] longs = new long[value.length];
        for (int i = 0; i < value.length; i++) {
            longs[i] = Double.doubleToLongBits(value[i]);
        }
        return toBytes(longs);
    }

    public static byte[] toBytes(short[] value) {
        byte[] bytes = new byte[value.length * 2];
        for (int i = 0; i < value.length; i++) {
            bytes[i * 4] = (byte) ((value[i] & 0xff00) >> 8);
            bytes[i * 4 + 1] = (byte) ((value[i] & 0x00ff));
        }
        return bytes;
    }

    public static byte[] toBytes(String value) {
        return value.getBytes(StandardCharsets.US_ASCII);
    }

    public static byte[] toBytes(String value, Charset charset) {
        return value.getBytes(charset);
    }

    public static byte[] escapeBytes(String value) {
        byte[] raw = value.getBytes(StandardCharsets.US_ASCII);
        return Arrays.copyOf(raw, raw.length + 1);
    }

    public static byte[] escapeBytes(String value, Charset charset) {
        byte[] raw = value.getBytes(charset);
        return Arrays.copyOf(raw, raw.length + 1);
    }

    public static byte[] packBooleans(boolean[] values) {
        byte[] bytes = new byte[(int) Math.ceil(values.length / 8f)];
        for (int i = 0; i < values.length; i++) {
            bytes[(int) Math.floor(i / 8f)] |= (values[i] ? (byte) 1 : (byte) 0) << i;
        }
        return bytes;
    }

    public static int intFromBytes(byte... bytes) {
        int[] unsigned = unsign(bytes);
        int ret = (unsigned[0] << 24)
            + (unsigned[1] << 16)
            + (unsigned[2] << 8)
            + unsigned[3];
        return ret;
    }

    public static long longFromBytes(byte... bytes) {
        int[] unsigned = unsign(bytes);
        return (long) (unsigned[0] << 56)
            + (unsigned[1] << 48)
            + (unsigned[2] << 40)
            + (unsigned[3] << 32)
            + (unsigned[4] << 24)
            + (unsigned[5] << 16)
            + (unsigned[6] << 8)
            + unsigned[7];
    }

    public static float floatFromBytes(byte... bytes) {
        return Float.intBitsToFloat(intFromBytes(bytes));
    }

    public static double doubleFromBytes(byte... bytes) {
        return Double.longBitsToDouble(longFromBytes(bytes));
    }

    public static short shortFromBytes(byte... bytes) {
        int[] unsigned = unsign(bytes);
        return (short) ((unsigned[0] << 8) + unsigned[1]);
    }

    public static String stringFromBytes(byte... bytes) {
        if (bytes.length == 0) return "";
        if (bytes[bytes.length - 1] == 0) {
            return new String(bytes, 0, bytes.length - 1, StandardCharsets.US_ASCII);
        }
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public static String stringFromBytes(Charset charset, byte... bytes) {
        if (bytes.length == 0) return "";
        if (bytes[bytes.length - 1] == 0) {
            return new String(bytes, 0, bytes.length - 1, charset);
        }
        return new String(bytes, charset);
    }

    public static boolean[] unpackBooleans(byte... bytes) {
        boolean[] booleans = new boolean[bytes.length * 8];
        for (int i = 0; i < booleans.length; i++) {
            booleans[i] = ((bytes[(int) Math.floor(i / 8f)] >> i) & 0x1) == 1;
        }
        return booleans;
    }

    public static int[] intArrayFromBytes(byte... bytes) {
        int[] ret = new int[bytes.length / 4];
        for (int i = 0; i < bytes.length / 4; i++) {
            ret[i] = intFromBytes(bytes[i * 4], bytes[i * 4 + 1], bytes[i * 4 + 2], bytes[i * 4 + 3]);
        }
        return ret;
    }

    public static long[] longArrayFromBytes(byte... bytes) {
        long[] ret = new long[bytes.length / 8];
        for (int i = 0; i < bytes.length / 8; i++) {
            ret[i] = longFromBytes(bytes[i * 8], bytes[i * 4 + 1], bytes[i * 8 + 2], bytes[i * 8 + 3], bytes[i * 8 + 4], bytes[i * 8 + 5], bytes[i * 8 + 6], bytes[i * 8 + 7]);
        }
        return ret;
    }

    public static float[] floatArrayFromBytes(byte... bytes) {
        int[] ints = intArrayFromBytes(bytes);
        float[] floats = new float[ints.length];
        for (int i = 0; i < ints.length; i++) {
            floats[i] = Float.intBitsToFloat(ints[i]);
        }
        return floats;
    }

    public static double[] doubleArrayFromBytes(byte... bytes) {
        long[] longs = longArrayFromBytes(bytes);
        double[] doubles = new double[longs.length];
        for (int i = 0; i < longs.length; i++) {
            doubles[i] = Double.longBitsToDouble(longs[i]);
        }
        return doubles;
    }

    public static short[] shortArrayFromBytes(byte... bytes) {
        short[] ret = new short[bytes.length / 2];
        for (int i = 0; i < bytes.length / 2; i++) {
            ret[i] = shortFromBytes(bytes[i * 2], bytes[i * 2 + 1]);
        }
        return ret;
    }

    public static String[] stringArrayFromBytes(byte... bytes) {
        byte[] buffer = new byte[bytes.length];
        int bufferBit = 0;
        String[] ret = new String[bytes.length / 2 + 1];
        int retBit = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0) {
                ret[retBit++] = new String(buffer, StandardCharsets.US_ASCII);
                buffer = new byte[bytes.length];
                bufferBit = 0;
            } else {
                buffer[bufferBit++] = bytes[i];
            }
        }
        ret = Arrays.copyOf(ret, retBit + 1);
        return ret;
    }

    public static String[] stringArrayFromBytes(Charset charset, byte... bytes) {
        byte[] buffer = new byte[bytes.length];
        int bufferBit = 0;
        String[] ret = new String[bytes.length / 2 + 1];
        int retBit = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0) {
                ret[retBit++] = new String(buffer, charset);
                buffer = new byte[bytes.length];
                bufferBit = 0;
            } else {
                buffer[bufferBit++] = bytes[i];
            }
        }
        ret = Arrays.copyOf(ret, retBit + 1);
        return ret;
    }

    public static int unsign(byte b) {
        return b < 0 ? 256 + b : b;
    }

    public static int[] unsign(byte[] bytes) {
        int[] ret = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = unsign(bytes[i]);
        }
        return ret;
    }
}
