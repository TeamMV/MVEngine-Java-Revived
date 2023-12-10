package dev.mv.engine.utils.collection;

import dev.mv.engine.utils.ArrayUtils;
import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.function.TriFunction;
import dev.mv.engine.utils.function.UnaryBiPredicate;
import dev.mv.engine.utils.generic.pair.Pair;
import dev.mv.engine.utils.generic.pair.UnaryPair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;

public class Vec<E> implements Iterable<E>, RandomAccess, Cloneable {

    private E[] data;
    private int size;

    @SafeVarargs
    public Vec(E... data) {
        this.data = Arrays.copyOf(data, data.length);
        this.size = data.length;
    }

    @SafeVarargs
    public Vec(int size, E... array) {
        data = (E[]) Array.newInstance(array.getClass().getComponentType(), size);
    }

    public Vec(int size, IntFunction<E[]> array) {
        this.data = array.apply(size);
    }

    @SafeVarargs
    public Vec(Collection<E> data, E... array) {
        this.data = data.toArray(array);
        this.size = this.data.length;
    }

    public Vec(Vec<E> vec) {
        this.data = Arrays.copyOf(vec.data, vec.data.length);
        this.size = data.length;
    }

    public int size() {
        return size;
    }

    public int len() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    public int freeCapacity() {
        return data.length - size;
    }

    private void grow(int min) {
        if (data.length >= size + min) {
            return;
        }
        int newLen = Math.round(data.length * ArrayUtils.phi);
        if (newLen >= size + min) {
            data = Arrays.copyOf(data, newLen);
        }
        else {
            data = Arrays.copyOf(data, size + min);
        }
    }

    private void shrink() {
        if (data.length <= size) {
            return;
        }
        if (Math.round(data.length * ArrayUtils.inversePhi) >= size) {
            data = Arrays.copyOf(data, size);
        }
    }

    public void allocate(int extra) {
        data = Arrays.copyOf(data, size + extra);
    }

    public void shrinkToSize() {
        if (data.length != size) {
            data = Arrays.copyOf(data, size);
        }
    }

    public int indexOf(E element) {
        return indexOfRange(element, 0, size);
    }

    public int indexOfRange(E element, int from, int to) {
        if (from < 0 || to > size) throw new IndexOutOfBoundsException();
        for (int i = from; i < to; i++) {
            E e = data[i];
            if (e == null && element == null) return i;
            if (e != null && e.equals(element)) return i;
        }
        return -1;
    }

    public void insert(int index, E element) {
        if (index < 0) throw new IndexOutOfBoundsException();
        if (index >= data.length) {
            grow(data.length - index);
            size = index + 1;
        }
        if (index >= size) {
            size = index + 1;
        }
        data[index] = element;
    }

    public E get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return data[index];
    }

    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E element = data[index];
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        size--;
        shrink();
        return element;
    }

    public E removeFirst(Predicate<E> predicate) {
        for (int i = 0; i < size; i++) {
            E e = data[i];
            if (predicate.test(e)) {
                return remove(i);
            }
        }
        return null;
    }

    public E remove(E element) {
        return remove(indexOf(element));
    }

    public E[] asArray() {
        return Arrays.copyOf(data, size);
    }

    public E[] asArray(Class<E> type) {
        return Utils.cast(Arrays.copyOf(data, size), type);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void append(Vec<E> other) {
        if (other == null) return;
        if (other.data.length == 0) return;
        if (data.length == 0) {
            data = other.data;
            size = data.length;
            return;
        }
        grow(other.data.length);
        System.arraycopy(other.data, 0, data, size, other.data.length);
        size += other.data.length;
    }

    public void append(E[] other) {
        if (other == null) return;
        if (other.length == 0) return;
        if (data.length == 0) {
            data = other;
            size = data.length;
            return;
        }
        grow(other.length);
        System.arraycopy(other, 0, data, size, other.length);
        size += other.length;
    }

    public void append(Collection<E> other) {
        if (other == null) return;
        if (other.size() == 0) return;
        if (data.length == 0) {
            data = other.toArray(Arrays.copyOf(data, 0));
            size = data.length;
            return;
        }
        grow(other.size());
        System.arraycopy(other.toArray(Arrays.copyOf(data, 0)), 0, data, size, other.size());
        size += other.size();
    }

    public void resetData(Vec<E> other) {
        clear();
        append(other);
    }

    public void resetData(E[] other) {
        clear();
        append(other);
    }

    public void resetData(Collection<E> other) {
        clear();
        append(other);
    }

    public void clear() {
        data = Arrays.copyOf(data, 0);
        size = 0;
    }

    public void dedup() {
        dedup((a, b) -> {
            if (a == null && b == null) return true;
            if (a == null || b == null) return false;
            return a.equals(b);
        });
    }

    public void dedup(UnaryBiPredicate<E> comparator) {
        if (size <= 1) return;
        int len = 1;
        E[] newData = Arrays.copyOf(data, size);
        E previous = data[0];
        for (int i = 1; i < size; i++) {
            E element = data[i];
            if (!comparator.compare(previous, element)) {
                newData[len++] = element;
                previous = element;
            }
        }
        data = Arrays.copyOf(newData, len);
        size = len;
    }

    public <R> void dedupByKey(Function<E, R> key) {
        dedup((a, b) -> key.apply(a).equals(key.apply(b)));
    }

    public Vec<Pair<E, Integer>> dedupWithCount() {
        return dedupWithCount((a, b) -> {
            if (a == null && b == null) return true;
            if (a == null || b == null) return false;
            return a.equals(b);
        });
    }

    public Vec<Pair<E, Integer>> dedupWithCount(UnaryBiPredicate<E> comparator) {
        if (size <= 1) return new Vec<>();
        Vec<Pair<E, Integer>> vec = new Vec<>();
        Pair<E, Integer> current = new Pair<>(data[0], 1);
        int len = 1;
        E[] newData = Arrays.copyOf(data, size);
        E previous = data[0];
        for (int i = 1; i < size; i++) {
            E element = data[i];
            if (comparator.compare(previous, element)) {
                current.b++;
            } else {
                vec.push(current);
                current = new Pair<>(element, 1);
                newData[len++] = element;
                previous = element;
            }
        }
        vec.push(current);
        data = Arrays.copyOf(newData, len);
        size = len;
        return vec;
    }

    public <R> Vec<Pair<E, Integer>> dedupByKeyWithCount(Function<E, R> key) {
        return dedupWithCount((a, b) -> key.apply(a).equals(key.apply(b)));
    }

    public Vec<E> drain(int from) {
        return drain(from, data.length);
    }

    public Vec<E> drain(int from, int to) {
        if (from < 0 || from >= size || to < 0 || to > size || from > to || from == to) throw new IndexOutOfBoundsException();
        Vec<E> drained = new Vec<>(Arrays.copyOfRange(data, from, to));
        System.arraycopy(data, to, data, from, size - to);
        data = Arrays.copyOf(data, size - drained.len());
        size -= drained.len();
        shrink();
        return drained;
    }

    public void retain(Predicate<E> predicate) {
        if (data.length < 1) return;
        int len = 0;
        E[] newData = Arrays.copyOf(data, data.length);
        for (int i = 0; i < size; i++) {
            E element = data[i];
            if (predicate.test(element)) {
                newData[len++] = element;
            }
        }
        data = Arrays.copyOf(newData, len);
        size = len;
        shrink();
    }

    public FastIter<E> fastIter() {
        return new FastIter<>(Arrays.copyOf(data, size));
    }

    public FastIter<E> fastIter(int from) {
        return fastIter(from, data.length);
    }

    public FastIter<E> fastIter(int from, int to) {
        if (from < 0 || from >= data.length || to < 0 || to > data.length || from > to || from == to)
            throw new IndexOutOfBoundsException();
        return new FastIter<>(Arrays.copyOfRange(data, from, to));
    }

    public Iterator<E> iterator() {
        ArrayList<E> arr = new ArrayList<>(size);
        arr.addAll(Arrays.asList(data).subList(0, size));
        return arr.iterator();
    }

    public void extendFromWithin(int from, int to) {
        if (from < 0 || from >= size || to < 0 || to > size || from > to) throw new IndexOutOfBoundsException();
        if (from == to) return;
        append(Arrays.copyOfRange(data, from, to));
    }

    public void push(E element) {
        grow(1);
        data[size++] = element;
    }

    public E pop() {
        if (size == 0) return null;
        E element = data[--size];
        shrink();
        return element;
    }

    public E popFirst() {
        return remove(0);
    }

    public boolean contains(E element) {
        return indexOf(element) >= 0;
    }

    public boolean containsAll(Collection<E> elements) {
        for (E element : elements) {
            if (!contains(element)) return false;
        }
        return true;
    }

    public boolean containsAll(Vec<E> elements) {
        for (E element : elements) {
            if (!contains(element)) return false;
        }
        return true;
    }

    public boolean containsAll(E[] elements) {
        for (E element : elements) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @SafeVarargs
    public final boolean startsWith(E... elements) {
        E[] array = Arrays.copyOf(data, elements.length);
        return Arrays.deepEquals(array, elements);
    }

    @SafeVarargs
    public final boolean endsWith(E... elements) {
        E[] array = Arrays.copyOfRange(data, data.length - elements.length, data.length);
        return Arrays.deepEquals(array, elements);
    }

    public E first() {
        if (size == 0) return null;
        return data[0];
    }

    public E last() {
        if (size == 0) return null;
        return data[size - 1];
    }

    public E find(Predicate<E> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(data[i])) return data[i];
        }
        return null;
    }

    public Vec<E> repeat(int times) {
        Vec<E> vec = new Vec<>();
        for (int i = 0; i < times; i++) {
            vec.append(this);
        }
        return vec;
    }

    public void truncate(int newSize) {
        if (newSize < 0) throw new IndexOutOfBoundsException("Size must be greater than or equal to 0!");
        if (newSize >= size) return;
        if (newSize == 0) {
            data = Arrays.copyOf(data, 0);
            size = 0;
            return;
        }
        size = newSize;
        data = Arrays.copyOf(data, size);
    }

    public void reverse() {
        data = Arrays.copyOf(data, size);
        data = ArrayUtils.flip(data);
    }

    public Vec<E> fill(E element) {
        for (int i = size; i < data.length; i++) {
            data[i] = element;
        }
        return this;
    }

    public Vec<E> fill(Supplier<E> supplier) {
        for (int i = size; i < data.length; i++) {
            data[i] = supplier.get();
        }
        return this;
    }

    public Pair<E, Vec<E>> splitLast() {
        if (size == 0) return new Pair<>(null, cloneEmpty());
        E last = data[size - 1];
        Vec<E> vec = new Vec<>();
        vec.data = Arrays.copyOfRange(data, 0, size - 1);
        vec.size = size - 1;
        return new Pair<>(last, vec);
    }

    public Pair<E, Vec<E>> splitFirst() {
        if (size == 0) return new Pair<>(null, cloneEmpty());
        E first = data[0];
        Vec<E> vec = new Vec<>();
        vec.data = Arrays.copyOfRange(data, 1, size);
        vec.size = size - 1;
        return new Pair<>(first, vec);
    }

    public Pair<E, Vec<E>> splitOffLast() {
        if (size == 0) return new Pair<>(null, cloneEmpty());
        E last = pop();
        return new Pair<>(last, this);
    }

    public Pair<E, Vec<E>> splitOffFirst() {
        if (data.length == 0) return new Pair<>(null, cloneEmpty());
        E first = popFirst();
        return new Pair<>(first, this);
    }

    public UnaryPair<Vec<E>> splitAt(E element) {
        return splitAt(indexOf(element));
    }

    public UnaryPair<Vec<E>> splitAt(int index) {
        if (index < 0 || index >= data.length) throw new IndexOutOfBoundsException();
        Vec<E> start = new Vec<>(), end = new Vec<>();
        start.data = Arrays.copyOfRange(data, 0, index);
        end.data = Arrays.copyOfRange(data, index, data.length);
        return new UnaryPair<>(start, end);
    }

    public Vec<E> splitOffAt(E element) {
        return splitOffAt(indexOf(element));
    }

    public Vec<E> splitOffAt(int index) {
        if (index < 0 || index >= data.length) throw new IndexOutOfBoundsException();
        Vec<E> vec = new Vec<>();
        vec.data = Arrays.copyOfRange(data, index, size);
        vec.size = size - index;
        data = Arrays.copyOf(data, index);
        size = index;
        shrink();
        return vec;
    }

    public Vec<E>[] splitInto(int amount) {
        if (amount <= 0) throw new IndexOutOfBoundsException("Amount must be greater than 0!");
        if (amount == 1) return new Vec<Vec<E>>(clone()).asArray();
        Vec<E>[] parts = new Vec<Vec<E>>(amount).fill(this::cloneEmpty).asArray();
        if (amount >= size) {
            for (int i = 0; i < amount; i++) {
                if (i < size) {
                    parts[i].push(data[i]);
                }
            }
            return parts;
        }
        int index = 0;
        int splitDataLength = size / amount;
        int extra = size % amount;

        for (int i = 0; i < amount; i++) {
            int length = splitDataLength;
            if (extra > 0) {
                length++;
                extra--;
            }
            parts[i].data = Arrays.copyOfRange(data, index, index + length);
            index += length;
        }

        return parts;
    }

    public Vec<E> sort(Comparator<E> comparator) {
        data = Arrays.copyOf(data, size);
        Arrays.sort(data, comparator);
        return this;
    }

    public void shuffle() {
        shuffle(new Random());
    }

    public void shuffle(Random random) {
        for (int i = size; i > 1; i--) {
            swap(i - 1, random.nextInt(i));
        }
    }

    public void swap(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) throw new IndexOutOfBoundsException();
        if (i == j) return;
        E temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    public E join(BinaryOperator<E> joiner) {
        if (size == 0) return null;
        if (size == 1) return data[0];
        E joined = data[0];
        for (int i = 1; i < size; i++) {
            joined = joiner.apply(joined, data[i]);
        }
        return joined;
    }

    public E join(BinaryOperator<E> joiner, E separator) {
        if (size == 0) return null;
        if (size == 1) return data[0];
        E joined = data[0];
        for (int i = 1; i < size; i++) {
            joined = joiner.apply(joined, separator);
            joined = joiner.apply(joined, data[i]);
        }
        return joined;
    }

    public <R> R join(Function<E, R> mapper, BinaryOperator<R> joiner) {
        if (size == 0) return null;
        if (size == 1) return mapper.apply(data[0]);
        R joined = mapper.apply(data[0]);
        for (int i = 1; i < size; i++) {
            joined = joiner.apply(joined, mapper.apply(data[i]));
        }
        return joined;
    }

    public <T> E join(TriFunction<E, T, E, E> joiner, T separator) {
        if (size == 0) return null;
        if (size == 1) return data[0];
        E joined = data[0];
        for (int i = 1; i < size; i++) {
            joined = joiner.apply(joined, separator, data[i]);
        }
        return joined;
    }

    public <T> E join(TriFunction<E, T, E, E> joiner, Supplier<T> separator) {
        if (size == 0) return null;
        if (size == 1) return data[0];
        E joined = data[0];
        for (int i = 1; i < size; i++) {
            joined = joiner.apply(joined, separator.get(), data[i]);
        }
        return joined;
    }

    public <R> R join(Function<E, R> mapper, BinaryOperator<R> joiner, R separator) {
        if (size == 0) return null;
        if (size == 1) return mapper.apply(data[0]);
        R joined = mapper.apply(data[0]);
        for (int i = 1; i < size; i++) {
            joined = joiner.apply(joined, separator);
            joined = joiner.apply(joined, mapper.apply(data[i]));
        }
        return joined;
    }

    public <T, R> R join(Function<E, R> mapper, TriFunction<R, T, R, R> joiner, T separator) {
        if (size == 0) return null;
        if (size == 1) return mapper.apply(data[0]);
        R joined = mapper.apply((E) data[0]);
        for (int i = 1; i < size; i++) {
            joined = joiner.apply(joined, separator, mapper.apply(data[i]));
        }
        return joined;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Vec<?> vec) {
            if (!vec.data.getClass().getComponentType().equals(data.getClass().getComponentType())) return false;
            if (vec.size != size) return false;
            vec.shrinkToSize();
            shrinkToSize();
            return Arrays.deepEquals(data, vec.data);
        }
        return false;
    }

    @Override
    public Vec<E> clone() {
        Vec<E> vec = new Vec<>();
        vec.data = Arrays.copyOf(data, size);
        vec.size = size;
        return vec;
    }

    public Vec<E> cloneEmpty() {
        return new Vec<>();
    }

    public static <T> Vec<T> collect(FastIter<T> fastIter) {
        Vec<T> vec = new Vec<T>();
        vec.data = fastIter.toArray();
        vec.size = vec.data.length;
        return vec;
    }
}
