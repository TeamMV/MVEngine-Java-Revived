package dev.mv.engine.utils.function;

import dev.mv.engine.utils.collection.FastIter;

public interface FastIterCollector<T, R> {

    R collect(FastIter<T> enumerator);

}
