package dev.mv.engine.utils.collection;

import java.util.ListIterator;

public class ImmutableListIterator<E> extends ImmutableIterator<E> implements ListIterator<E> {


    public ImmutableListIterator(Object[] elements) {
        super(elements);
    }

    public ImmutableListIterator(Object[] elements, int index) {
        super(elements, index);
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public E previous() {
        return (E) elements[--index];
    }

    @Override
    public int nextIndex() {
        return index++;
    }

    @Override
    public int previousIndex() {
        return --index;
    }

    @Override
    public void set(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(E e) {
        throw new UnsupportedOperationException();
    }
}
