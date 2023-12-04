package dev.mv.engine.utils.collection;

import java.util.Iterator;
import java.util.function.Consumer;

public class ImmutableIterator<E> implements Iterator<E> {

    protected final Object[] elements;
    protected int index = 0;

    public ImmutableIterator(Object[] elements) {
        this.elements = elements;
    }

    public ImmutableIterator(Object[] elements, int index) {
        this.elements = elements;
        this.index = index;
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public E next() {
        if (index < 0 || index >= elements.length) return null;
        return (E) elements[index++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        for (int i = index; i < elements.length; i++) {
            action.accept((E) elements[i]);
        }
        index = elements.length;
    }
}
