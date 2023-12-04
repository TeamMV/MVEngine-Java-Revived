package dev.mv.engine.utils.collection;

import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.function.*;
import dev.mv.engine.utils.generic.Indexed;
import dev.mv.engine.utils.generic.Option;
import dev.mv.engine.utils.generic.Result;
import dev.mv.engine.utils.generic.pair.UnaryPair;

import java.util.Comparator;
import java.util.function.*;

import static dev.mv.engine.utils.generic.Result.Err;
import static dev.mv.engine.utils.generic.Result.Ok;

public class UnsafeFastIter<T> implements Cloneable {

    private final FastIter<T> fastIter;

    UnsafeFastIter(FastIter<T> fastIter) {
        this.fastIter = fastIter;
    }

    public int size() {
        return fastIter.size();
    }

    public int len() {
        return fastIter.len();
    }

    public T first() {
        return fastIter.first();
    }

    public T last() {
        return fastIter.last();
    }

    public UnsafeFastIter<T> intersperse(T t) {
        return fastIter.intersperse(t).unsafe();
    }

    public UnsafeFastIter<T> intersperse(Supplier<T> supplier) {
        return fastIter.intersperse(supplier).unsafe();
    }

    public <U> UnsafeFastIter<U> map(Function<T, U> mapper) {
        return fastIter.map(mapper).unsafe();
    }

    public <U> UnsafeFastIter<U> multiMap(Function<Vec<T>, U> mapper, int amount) {
        return fastIter.multiMap(mapper, amount).unsafe();
    }

    public UnsafeFastIter<T> forEach(Consumer<T> consumer) {
        return fastIter.forEach(consumer).unsafe();
    }

    public UnsafeFastIter<T> indexedForEach(IndexedConsumer<T> consumer) {
        return fastIter.indexedForEach(consumer).unsafe();
    }

    public UnsafeFastIter<T> filter(Predicate<T> predicate) {
        return fastIter.filter(predicate).unsafe();
    }

    public <U> UnsafeFastIter<U> filterMap(Function<T, Option<U>> mapper) {
        return fastIter.filterMap(mapper).unsafe();
    }

    public UnsafeFastIter<Indexed<T>> enumerate() {
        return fastIter.enumerate().unsafe();
    }

    public <U> UnsafeFastIter<U> scan(U seed, BiFunction<U, T, Option<U>> f) {
        return fastIter.scan(seed, f).unsafe();
    }

    public <U> UnsafeFastIter<U> flatMap(Function<T, Vec<U>> mapper) {
        return fastIter.flatMap(mapper).unsafe();
    }

    public Vec<T> collect() {
        return fastIter.collect();
    }

    public <R> R collect(FastIterCollector<T, R> collector) {
        return fastIter.collect(collector);
    }

    public T[] toArray() {
        return fastIter.toArray();
    }

    public T[] toArray(Class<T> type) {
        return fastIter.toArray(type);
    }

    public UnaryPair<UnsafeFastIter<T>> partition(Predicate<T> predicate) {
        UnaryPair<UnsafeFastIter<T>> pair = new UnaryPair<>();
        UnaryPair<FastIter<T>> partitioned = fastIter.partition(predicate);
        pair.a = partitioned.a.unsafe();
        pair.b = partitioned.b.unsafe();
        return pair;
    }

    public UnaryPair<Vec<T>> collectPartition(Predicate<T> predicate) {
        return fastIter.collectPartition(predicate);
    }

    public boolean isPartitioned(Predicate<T> predicate) {
        return fastIter.isPartitioned(predicate);
    }

    public <R> R tryFold(R seed, BiFunction<R, T, Option<R>> f) {
        return fastIter.tryFold(seed, f).unwrapChecked();
    }

    public <E extends Throwable> UnsafeFastIter<T> tryForEach(FaultyConsumer<T, E> consumer) throws E {
        Result<FastIter<T>, E> result = fastIter.tryForEach(e -> {
            try {
                consumer.accept(e);
                return Ok();
            } catch (Throwable ex) {
                return Err((E) ex);
            }
        });
        result.<E>throwIfErr();
        return result.unwrap().unsafe();
    }

    public <E extends Throwable> UnsafeFastIter<T> tryIndexedForEach(FaultyIndexedConsumer<T, E> consumer) throws E {
        Result<FastIter<T>, E> result = fastIter.tryIndexedForEach((t, i) -> {
            try {
                consumer.accept(t, i);
                return Ok();
            } catch (Throwable ex) {
                return Err((E) ex);
            }
        });
        result.<E>throwIfErr();
        return result.unwrap().unsafe();
    }

    public <E extends Throwable> UnsafeFastIter<T> tryFilter(FaultyPredicate<T, E> f) throws E {
        Result<FastIter<T>, E> result = fastIter.tryFilter(e -> {
            try {
                return Ok(f.test(e));
            } catch (Throwable t) {
                return Err((E) t);
            }
        });
        result.<E>throwIfErr();
        return result.unwrap().unsafe();
    }

    public <R> R fold(R seed, BiFunction<R, T, R> f) {
        return fastIter.fold(seed, f);
    }

    public T reduce(BinaryOperator<T> f) {
        return fastIter.reduce(f);
    }

    public T reduce(T seed, BinaryOperator<T> f) {
        return fastIter.reduce(seed, f);
    }

    public T tryReduce(BiFunction<T, T, Option<T>> f) {
        return fastIter.tryReduce(f).unwrapChecked();
    }

    public T tryReduce(T seed, BiFunction<T, T, Option<T>> f) {
        return fastIter.tryReduce(seed, f).unwrapChecked();
    }

    public boolean all(Predicate<T> predicate) {
        return fastIter.all(predicate);
    }

    public boolean any(Predicate<T> predicate) {
        return fastIter.any(predicate);
    }

    public T find(Predicate<T> predicate) {
        return fastIter.find(predicate);
    }

    public <U> U findMap(Function<T, Option<U>> mapper) {
        return fastIter.findMap(mapper);
    }

    public <E> T tryFind(Function<T, Result<Boolean, E>> f) {
        return fastIter.tryFind(f).unwrapChecked();
    }

    public int position(Predicate<T> predicate) {
        return fastIter.position(predicate);
    }

    public int rPosition(Predicate<T> predicate) {
        return fastIter.rPosition(predicate);
    }

    public T max(Comparator<T> comparator) {
        return fastIter.max(comparator);
    }

    public T min(Comparator<T> comparator) {
        return fastIter.min(comparator);
    }

    public <U> T max(Function<T, U> mapper, Comparator<U> comparator) {
        return fastIter.max(mapper, comparator);
    }

    public <U> T min(Function<T, U> mapper, Comparator<U> comparator) {
        return fastIter.min(mapper, comparator);
    }

    public UnsafeFastIter<T> reverse() {
        return fastIter.reverse().unsafe();
    }

    public UnsafeFastIter<Vec<T>> chunks(int size) {
        return fastIter.chunks(size).unsafe();
    }

    public boolean eq(UnsafeFastIter<T> other, UnaryBiPredicate<T> predicate) {
        return fastIter.eq(other.safe(), predicate);
    }

    public boolean isSorted(Comparator<T> comparator) {
        return fastIter.isSorted(comparator);
    }

    public UnsafeFastIter<T> sort(Comparator<T> comparator) {
        return fastIter.sort(comparator).unsafe();
    }

    public FastIter<T> safe() {
        return fastIter;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof UnsafeFastIter<?> i) {
            return fastIter.equals(i.safe());
        }
        return false;
    }

    public UnsafeFastIter<T> clone() {
        return fastIter.clone().unsafe();
    }
}
