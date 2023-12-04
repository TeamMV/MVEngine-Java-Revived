package dev.mv.engine.utils;

import java.lang.reflect.Array;

public class ArrayUtils {

    public static final float phi = 1.6180339887498948482f;
    public static final float inversePhi = 1f / phi;

    public static byte[] flipped(byte[] bytes) {
        byte[] flipped = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            flipped[i] = bytes[bytes.length - 1 - i];
        }
        return flipped;
    }

    public static byte[] flip(byte[] bytes) {
        byte temp;
        for (int i = 0; i < bytes.length / 2; i++) {
            temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        return bytes;
    }

    public static short[] flipped(short[] shorts) {
        short[] flipped = new short[shorts.length];
        for (int i = 0; i < shorts.length; i++) {
            flipped[i] = shorts[shorts.length - 1 - i];
        }
        return flipped;
    }

    public static short[] flip(short[] shorts) {
        short temp;
        for (int i = 0; i < shorts.length / 2; i++) {
            temp = shorts[i];
            shorts[i] = shorts[shorts.length - i - 1];
            shorts[shorts.length - i - 1] = temp;
        }
        return shorts;
    }

    public static int[] flipped(int[] ints) {
        int[] flipped = new int[ints.length];
        for (int i = 0; i < ints.length; i++) {
            flipped[i] = ints[ints.length - 1 - i];
        }
        return flipped;
    }

    public static int[] flip(int[] ints) {
        int temp;
        for (int i = 0; i < ints.length / 2; i++) {
            temp = ints[i];
            ints[i] = ints[ints.length - i - 1];
            ints[ints.length - i - 1] = temp;
        }
        return ints;
    }

    public static long[] flipped(long[] longs) {
        long[] flipped = new long[longs.length];
        for (int i = 0; i < longs.length; i++) {
            flipped[i] = longs[longs.length - 1 - i];
        }
        return flipped;
    }

    public static long[] flip(long[] longs) {
        long temp;
        for (int i = 0; i < longs.length / 2; i++) {
            temp = longs[i];
            longs[i] = longs[longs.length - i - 1];
            longs[longs.length - i - 1] = temp;
        }
        return longs;
    }

    public static float[] flipped(float[] floats) {
        float[] flipped = new float[floats.length];
        for (int i = 0; i < floats.length; i++) {
            flipped[i] = floats[floats.length - 1 - i];
        }
        return flipped;
    }

    public static float[] flip(float[] floats) {
        float temp;
        for (int i = 0; i < floats.length / 2; i++) {
            temp = floats[i];
            floats[i] = floats[floats.length - i - 1];
            floats[floats.length - i - 1] = temp;
        }
        return floats;
    }

    public static double[] flipped(double[] doubles) {
        double[] flipped = new double[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            flipped[i] = doubles[doubles.length - 1 - i];
        }
        return flipped;
    }

    public static double[] flip(double[] doubles) {
        double temp;
        for (int i = 0; i < doubles.length / 2; i++) {
            temp = doubles[i];
            doubles[i] = doubles[doubles.length - i - 1];
            doubles[doubles.length - i - 1] = temp;
        }
        return doubles;
    }

    public static boolean[] flipped(boolean[] booleans) {
        boolean[] flipped = new boolean[booleans.length];
        for (int i = 0; i < booleans.length; i++) {
            flipped[i] = booleans[booleans.length - 1 - i];
        }
        return flipped;
    }

    public static boolean[] flip(boolean[] booleans) {
        boolean temp;
        for (int i = 0; i < booleans.length / 2; i++) {
            temp = booleans[i];
            booleans[i] = booleans[booleans.length - i - 1];
            booleans[booleans.length - i - 1] = temp;
        }
        return booleans;
    }

    public static <T> T[] flipped(T[] data) {
        T[] flipped = (T[]) Array.newInstance(data.getClass().getComponentType(), data.length);
        for (int i = 0; i < data.length; i++) {
            flipped[i] = data[data.length - i - 1];
        }
        return flipped;
    }

    public static <T> T[] flip(T[] data) {
        T temp;
        for (int i = 0; i < data.length / 2; i++) {
            temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
        return data;
    }

}
