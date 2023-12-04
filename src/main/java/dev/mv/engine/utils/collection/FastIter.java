package dev.mv.engine.utils.collection;

import dev.mv.engine.utils.ArrayUtils;
import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.function.*;
import dev.mv.engine.utils.generic.Indexed;
import dev.mv.engine.utils.generic.Null;
import dev.mv.engine.utils.generic.Option;
import dev.mv.engine.utils.generic.Result;
import dev.mv.engine.utils.generic.pair.UnaryPair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.*;

import static dev.mv.engine.utils.generic.Option.None;
import static dev.mv.engine.utils.generic.Option.Some;
import static dev.mv.engine.utils.generic.Result.Err;
import static dev.mv.engine.utils.generic.Result.Ok;

public class FastIter<E> implements Cloneable {

    private final UnsafeFastIter<E> unsafe = new UnsafeFastIter<>(this);

    private E[] data;

    @SafeVarargs
    public FastIter(E... data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    @SafeVarargs
    public FastIter(Collection<E> data, E... array) {
        this.data = data.toArray(array);
    }

    public int size() {
        return data.length;
    }

    public int len() {
        return data.length;
    }

    public E first() {
        if (data.length == 0) return null;
        return data[0];
    }

    public E last() {
        if (data.length == 0) return null;
        return data[data.length - 1];
    }

    public FastIter<E> intersperse(E e) {
        Vec<E> vec = new Vec<>();
        for (int i = 0; i < data.length; i++) {
            vec.push(data[i]);
            vec.push(e);
        }
        vec.pop();
        return vec.fastIter();
    }

    public FastIter<E> intersperse(Supplier<E> supplier) {
        Vec<E> vec = new Vec<>();
        for (int i = 0; i < data.length; i++) {
            vec.push(data[i]);
            vec.push(supplier.get());
        }
        vec.pop();
        return vec.fastIter();
    }

    public <U> FastIter<U> map(Function<E, U> mapper) {
        Vec<U> vec = new Vec<>();
        for (int i = 0; i < data.length; i++) {
            vec.push(mapper.apply(data[i]));
        }
        return vec.fastIter();
    }

    public <U> FastIter<U> multiMap(Function<Vec<E>, U> mapper, int amount) {
        Vec<U> vec = new Vec<>();
        Vec<E> buffer = new Vec<>();
        for (int i = 0; i + amount <= data.length; i += amount) {
            buffer.append(Arrays.copyOfRange(data, i, i + amount));
            vec.push(mapper.apply(buffer));
            buffer.clear();
        }
        return vec.fastIter();
    }

    public FastIter<E> forEach(Consumer<E> consumer) {
        for (int i = 0; i < data.length; i++) {
            consumer.accept(data[i]);
        }
        return this;
    }

    public FastIter<E> indexedForEach(IndexedConsumer<E> consumer) {
        enumerate().forEach(indexed -> consumer.accept(indexed.item, indexed.index));
        return this;
    }

    public FastIter<E> filter(Predicate<E> predicate) {
        int len = 0;
        E[] newData = Arrays.copyOf(data, data.length);
        for (int i = 0; i < data.length; i++) {
            if (predicate.test(data[i])) {
                newData[len++] = data[i];
            }
        }
        data = Arrays.copyOf(newData, len);
        return this;
    }

    public <U> FastIter<U> filterMap(Function<E, Option<U>> mapper) {
        Vec<U> vec = new Vec<>();
        for (int i = 0; i < data.length; i++) {
            mapper.apply(data[i]).asNullHandler().then(vec::push);
        }
        return vec.fastIter();
    }

    public FastIter<Indexed<E>> enumerate() {
        Vec<Indexed<E>> vec = new Vec<>();
        for (int i = 0; i < data.length; i++) {
            vec.push(new Indexed<>(i, data[i]));
        }
        return vec.fastIter();
    }

    public <U> FastIter<U> scan(U seed, BiFunction<U, E, Option<U>> f) {
        Vec<U> vec = new Vec<>(seed);
        U previous = seed;
        for (int i = 0; i < data.length; i++) {
            Option<U> option = f.apply(previous, data[i]);
            if (option.isSome()) {
                vec.push(option.unwrap());
                previous = option.unwrap();
            } else {
                return vec.fastIter();
            }
        }
        return vec.fastIter();
    }

    public <U> FastIter<U> flatMap(Function<E, Vec<U>> mapper) {
        Vec<U> vec = new Vec<>();
        for (E datum : data) {
            vec.append(mapper.apply(datum));
        }
        return vec.fastIter();
    }

    public Vec<E> collect() {
        return collect(Vec::collect);
    }

    public <R> R collect(FastIterCollector<E, R> collector) {
        return collector.collect(this);
    }

    public E[] toArray() {
        return Arrays.copyOf(data, data.length);
    }

    public E[] toArray(Class<E> type) {
        return Utils.cast(data, type);
    }

    public UnaryPair<FastIter<E>> partition(Predicate<E> predicate) {
        return collectPartition(predicate).map(Vec::fastIter);
    }

    public UnaryPair<Vec<E>> collectPartition(Predicate<E> predicate) {
        Vec<E> a = new Vec<>();
        Vec<E> b = new Vec<>();
        for (int i = 0; i < data.length; i++) {
            if (predicate.test(data[i])) {
                a.push(data[i]);
            } else {
                b.push(data[i]);
            }
        }
        return new UnaryPair<>(a, b);
    }

    public boolean isPartitioned(Predicate<E> predicate) {
        if (data.length <= 1) return true;
        boolean state = predicate.test(data[0]);
        for (int i = 1; i < data.length; i++) {
            if (predicate.test(data[i]) != state) {
                return false;
            }
        }
        return true;
    }

    public <R> Option<R> tryFold(R seed, BiFunction<R, E, Option<R>> f) {
        for (int i = 0; i < data.length; i++)  {
            Option<R> option = f.apply(seed, data[i]);
            if (option.isSome()) {
                seed = option.unwrap();
            } else {
                return None();
            }
        }
        return Some(seed);
    }

    public <Err> Result<FastIter<E>, Err> tryForEach(Function<E, Result<Null, Err>> f) {
        for (int i = 0; i < data.length; i++) {
            Result<Null, Err> result = f.apply(data[i]);
            if (result.isErr()) {
                return Err(result.unwrapErr());
            }
        }
        return Ok(this);
    }

    public <Err> Result<FastIter<E>, Err> tryIndexedForEach(IndexedFunction<E, Result<Null, Err>> consumer) {
        Result<FastIter<Indexed<E>>, Err> result = enumerate().tryForEach(indexed -> consumer.apply(indexed.item, indexed.index));
        if (result.isOk()) {
            return Ok(this);
        }
        return Err(result.unwrapErr());
    }

    public <Err> Result<FastIter<E>, Err> tryFilter(Function<E, Result<Boolean, Err>> f) {
        int len = 0;
        E[] newData = Arrays.copyOf(data, data.length);
        for (int i = 0; i < data.length; i++) {
            Result<Boolean, Err> result = f.apply(data[i]);
            if (result.isErr()) {
                return Err(result.unwrapErr());
            }
            if (result.unwrap()) {
                newData[len++] = data[i];
            }
        }
        data = Arrays.copyOf(newData, len);
        return Ok(this);
    }

    public <R> R fold(R seed, BiFunction<R, E, R> f) {
        for (int i = 0; i < data.length; i++) {
            seed = f.apply(seed, data[i]);
        }
        return seed;
    }

    public E reduce(BinaryOperator<E> f) {
        if (data.length == 0) return null;
        E previous = data[0];
        for (int i = 1; i < data.length; i++) {
            previous = f.apply(previous, data[i]);
        }
        return previous;
    }

    public E reduce(E seed, BinaryOperator<E> f) {
        if (data.length == 0) return seed;
        E previous = seed;
        for (int i = 0; i < data.length; i++) {
            previous = f.apply(previous, data[i]);
        }
        return previous;
    }

    public Option<E> tryReduce(BiFunction<E, E, Option<E>> f) {
        if (data.length == 0) return None();
        E previous = data[0];
        for (int i = 1; i < data.length; i++) {
            Option<E> option = f.apply(previous, data[i]);
            if (option.isSome()) {
                previous = option.unwrap();
            } else {
                return None();
            }
        }
        return Some(previous);
    }

    public Option<E> tryReduce(E seed, BiFunction<E, E, Option<E>> f) {
        if (data.length == 0) return Some(seed);
        E previous = seed;
        for (int i = 0; i < data.length; i++) {
            Option<E> option = f.apply(previous, data[i]);
            if (option.isSome()) {
                previous = option.unwrap();
            } else {
                return None();
            }
        }
        return Some(previous);
    }

    public boolean all(Predicate<E> predicate) {
        for (E e : data) {
            if (!predicate.test(e)) return false;
        }
        return true;
    }

    public boolean any(Predicate<E> predicate) {
        for (E e : data) {
            if (predicate.test(e)) return true;
        }
        return false;
    }

    public E find(Predicate<E> predicate) {
        for (E e : data) {
            if (predicate.test(e)) return e;
        }
        return null;
    }

    public <U> U findMap(Function<E, Option<U>> mapper) {
        for (E e : data) {
            Option<U> option = mapper.apply(e);
            if (option.isSome()) return option.unwrap();
        }
        return null;
    }

    public <Err> Result<E, Err> tryFind(Function<E, Result<Boolean, Err>> f) {
        for (E e : data) {
            Result<Boolean, Err> result = f.apply(e);
            if (result.isErr()) return Err(result.unwrapErr());
            if (result.isOk() && result.unwrapChecked()) return Ok(e);
        }
        return Err(null);
    }

    public int position(Predicate<E> predicate) {
        for (int i = 0; i < data.length; i++) {
            if (predicate.test(data[i])) return i;
        }
        return -1;
    }

    public int rPosition(Predicate<E> predicate) {
        int pos = position(predicate);
        if (pos < 0) {
            return -1;
        }
        return data.length - pos - 1;
    }

    public E max(Comparator<E> comparator) {
        if (data.length == 0) return null;
        E max = data[0];
        for (int i = 0; i < data.length; i++) {
            if (comparator.compare(data[i], data[i + 1]) >= 0) max = data[i + 1];
        }
        return max;
    }

    public E min(Comparator<E> comparator) {
        if (data.length == 0) return null;
        E min = data[0];
        for (int i = 0; i < data.length; i++) {
            if (comparator.compare(data[i], data[i + 1]) <= 0) min = data[i + 1];
        }
        return min;
    }

    public <U> E max(Function<E, U> mapper, Comparator<U> comparator) {
        if (data.length == 0) return null;
        E max = data[0];
        for (int i = 0; i < data.length; i++) {
            if (comparator.compare(mapper.apply(data[i]), mapper.apply(data[i + 1])) >= 0) max = data[i + 1];
        }
        return max;
    }

    public <U> E min(Function<E, U> mapper, Comparator<U> comparator) {
        if (data.length == 0) return null;
        E max = data[0];
        for (int i = 0; i < data.length; i++) {
            if (comparator.compare(mapper.apply(data[i]), mapper.apply(data[i + 1])) <= 0) max = data[i + 1];
        }
        return max;
    }

    public FastIter<E> reverse() {
        ArrayUtils.flip(data);
        return this;
    }

    public FastIter<Vec<E>> chunks(int size) {
        Vec<Vec<E>> chunks = new Vec<Vec<E>>((int) Math.ceil((float) data.length / size)).fill(Vec<E>::new);
        int index = 0;
        for (Vec<E> chunk : chunks) {
            for (int i = 0; i < size; i++) {
                if (index < data.length) {
                    chunk.push(data[index++]);
                } else {
                    break;
                }
            }
        }
        return chunks.fastIter();
    }

    public boolean eq(FastIter<E> other, UnaryBiPredicate<E> predicate) {
        if (other.size() != data.length) return false;
        for (int i = 0; i < data.length; i++) {
            if (!predicate.compare(data[i], other.data[i])) return false;
        }
        return true;
    }

    public boolean isSorted(Comparator<E> comparator) {
        for (int i = 1; i < data.length; i++) {
            if (comparator.compare(data[i - 1], data[i]) > 0) return false;
        }
        return true;
    }

    public FastIter<E> sort(Comparator<E> comparator) {
        Arrays.sort(data, comparator);
        return this;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof FastIter<?> fastIter) {
            if (!fastIter.data.getClass().getComponentType().equals(data.getClass().getComponentType())) return false;
            return Arrays.deepEquals(data, fastIter.data);
        }
        return false;
    }

    @Override
    public FastIter<E> clone() {
        return new FastIter<>(Arrays.copyOf(data, data.length));
    }

    public UnsafeFastIter<E> unsafe() {
        return unsafe;
    }
}
