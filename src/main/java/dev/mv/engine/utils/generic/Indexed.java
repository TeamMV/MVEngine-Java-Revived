package dev.mv.engine.utils.generic;

import dev.mv.engine.utils.Utils;

public class Indexed<T> {

    public T item;
    public int index;

    public Indexed(int index, T value) {
        this.item = value;
        this.index = index;
    }

    public static <T> Indexed<T> Index(int index, T item) {
        return new Indexed<T>(index, item);
    }

    public static <T> Indexed<T> Index(String use, T item) {
        return new Indexed<T>(Utils.nextId(use), item);
    }

    public static <T> Indexed<T> Index(T item) {
        return new Indexed<T>(Utils.nextId("dev.mv.engine.utils.generic.Indexed<" + item.getClass().getTypeName() + ">"), item);
    }

}
