package dev.mv.engine.utils.buffer;

import java.util.Arrays;

public class DynamicCharBuffer {

    char[] chars;

    public DynamicCharBuffer() {
        chars = new char[0];
    }

    public DynamicCharBuffer(String str) {
        chars = str.toCharArray();
    }

    public DynamicCharBuffer(char[] chars) {
        this.chars = chars;
    }

    public DynamicCharBuffer push(char c) {
        chars = Arrays.copyOf(chars, chars.length + 1);
        chars[chars.length - 1] = c;
        return this;
    }

    public DynamicCharBuffer push(char... chars) {
        this.chars = Arrays.copyOf(this.chars, this.chars.length + chars.length);
        System.arraycopy(chars, 0, this.chars, this.chars.length - chars.length, chars.length);
        return this;
    }

    public DynamicCharBuffer push(String str) {
        push(str.toCharArray());
        return this;
    }

    public char pop() {
        char ret = chars[chars.length - 1];
        chars = Arrays.copyOf(chars, chars.length - 1);
        return ret;
    }

    public char[] pop(int n) {
        char[] ret = new char[n];
        System.arraycopy(chars, chars.length - n, ret, 0, n);
        chars = Arrays.copyOf(chars, chars.length - n);
        return flip(ret);
    }

    public char peek() {
        return chars[chars.length - 1];
    }

    public char[] peek(int n) {
        char[] ret = new char[n];
        System.arraycopy(chars, chars.length - n, ret, 0, n);
        return flip(ret);
    }

    public char[] array() {
        return chars.clone();
    }

    public int length() {
        return chars.length;
    }

    public int rawLength() {
        return chars.length;
    }

    public boolean isEmpty() {
        return chars.length == 0;
    }

    public boolean isRawEmpty() {
        return chars.length == 0;
    }

    public int size() {
        return 0;
    }

    public DynamicCharBuffer flip() {
        return this;
    }

    public DynamicCharBuffer reverse() {
        chars = flip(chars);
        return this;
    }

    public DynamicCharBuffer clear() {
        chars = new char[0];
        return this;
    }

    private char[] flip(char[] bytes) {
        char[] flipped = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            flipped[i] = bytes[bytes.length - 1 - i];
        }
        return flipped;
    }

}
