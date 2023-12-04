package dev.mv.engine.utils.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

public class ImmutableList<E> extends ImmutableCollection<E> implements List<E> {

    private static final ImmutableList EMPTY_LIST = new ImmutableList();

    private final Object[] elements;

    private ImmutableList() {
        this.elements = new Object[0];
    }

    private ImmutableList(Object[] elements) {
        this.elements = elements;
    }

    public static <E> List<E> of() {
        return EMPTY_LIST;
    }

    public static <E> List<E> of(E e1) {
        return new ImmutableList<>(new Object[]{e1});
    }

    public static <E> List<E> of(E e1, E e2) {
        return new ImmutableList<>(new Object[]{e1, e2});
    }

    public static <E> List<E> of(E e1, E e2, E e3) {
        return new ImmutableList<>(new Object[]{e1, e2, e3});
    }

    public static <E> List<E> of(E e1, E e2, E e3, E e4) {
        return new ImmutableList<>(new Object[]{e1, e2, e3, e4});
    }

    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
        return new ImmutableList<>(new Object[]{e1, e2, e3, e4, e5});
    }

    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return new ImmutableList<>(new Object[]{e1, e2, e3, e4, e5, e6});
    }

    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return new ImmutableList<>(new Object[]{e1, e2, e3, e4, e5, e6, e7});
    }

    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return new ImmutableList<>(new Object[]{e1, e2, e3, e4, e5, e6, e7, e8});
    }

    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return new ImmutableList<>(new Object[]{e1, e2, e3, e4, e5, e6, e7, e8, e9});
    }

    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return new ImmutableList<>(new Object[]{e1, e2, e3, e4, e5, e6, e7, e8, e9, e10});
    }

    public static <E> List<E> of(E... e) {
        return new ImmutableList<>(e);
    }

    public static <E> List<E> copyOf(Collection<? extends E> coll) {
        return new ImmutableList<>(coll.toArray());
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= elements.length) throw new IndexOutOfBoundsException();
        return (E) elements[index];
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].equals(o)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = elements.length - 1; i >= 0; i--) {
            if (elements[i].equals(o)) return i;
        }
        return -1;
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return new ImmutableListIterator<E>(elements);
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return new ImmutableListIterator<E>(elements, index);
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        Object[] copy = Arrays.copyOfRange(elements, fromIndex, toIndex);
        return new ImmutableList<E>(copy);
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Object element : elements) {
            if (element.equals(o)) return true;
        }
        return false;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new ImmutableListIterator<E>(elements);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return elements;
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        if (a.length != elements.length) {
            T[] ret = Arrays.copyOf(a, elements.length);
            System.arraycopy(elements, 0, ret, 0, elements.length);
            return ret;
        } else {
            System.arraycopy(elements, 0, a, 0, elements.length);
            return a;
        }
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        T[] ret = generator.apply(elements.length);
        System.arraycopy(elements, 0, ret, 0, elements.length);
        return ret;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (int i = 0; i < elements.length; i++) {
            E element = (E) elements[i];
            action.accept(element);
        }
    }

    @Override
    public final boolean addAll(int index, @NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }

    public static class Builder<E> {

        Object[] elements;
        boolean limited = false;
        int index = 0;

        public Builder() {
            elements = new Object[0];
        }

        public Builder(int limit) {
            elements = new Object[limit];
        }

        public Builder<E> add(Object element) {
            if (!limited) {
                elements = Arrays.copyOf(elements, elements.length + 1);
                elements[elements.length - 1] = element;
            } else {
                index++;
                if (index >= elements.length) {
                    throw new IndexOutOfBoundsException();
                }
                elements[index] = element;
            }
            return this;
        }

        public Builder<E> addAll(Object... elements) {
            if (elements == null || elements.length == 0) return this;
            if (!limited) {
                this.elements = Arrays.copyOf(this.elements, this.elements.length + elements.length);
                System.arraycopy(elements, 0, this.elements, this.elements.length - elements.length, elements.length);
            } else {
                if (index + elements.length >= this.elements.length) {
                    throw new IndexOutOfBoundsException();
                }
                System.arraycopy(elements, 0, this.elements, index, elements.length);
            }
            return this;
        }

        public Builder<E> addAll(Collection elements) {
            return addAll(elements.toArray());
        }

        public ImmutableList<E> build() {
            return new ImmutableList<E>(elements);
        }
    }
}
