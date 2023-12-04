package dev.mv.engine.utils.check;

import java.util.function.Function;
import java.util.function.Supplier;

public class MatchReturn<T, R> {

    private T value;
    private R ret;
    private boolean happened;

    public MatchReturn(T value) {
        this.value = value;
    }

    public MatchReturn<T, R> occasion(T value, R ret) {
        if (this.value.equals(value)) {
            happened = true;
            this.ret = ret;
        }
        return this;
    }

    public MatchReturn<T, R> occasion(T value, Supplier<R> function) {
        if (this.value.equals(value)) {
            happened = true;
            ret = function.get();
        }
        return this;
    }

    public MatchReturn<T, R> occasion(T value, Function<T, R> function) {
        if (this.value.equals(value)) {
            happened = true;
            ret = function.apply(this.value);
        }
        return this;
    }

    public MatchReturn<T, R> __(R ret) {
        if (!happened) {
            this.ret = ret;
        }
        return this;
    }

    public MatchReturn<T, R> __(Supplier<R> function) {
        if (!happened) {
            this.ret = function.get();
        }
        return this;
    }

    public MatchReturn<T, R> __(Function<T, R> function) {
        if (!happened) {
            this.ret = function.apply(this.value);
        }
        return this;
    }

    public MatchReturn<T, R> noOccasion(R ret) {
        if (!happened) {
            this.ret = ret;
        }
        return this;
    }

    public MatchReturn<T, R> noOccasion(Supplier<R> function) {
        if (!happened) {
            this.ret = function.get();
        }
        return this;
    }

    public MatchReturn<T, R> noOccasion(Function<T, R> function) {
        if (!happened) {
            this.ret = function.apply(this.value);
        }
        return this;
    }

    public R value() {
        return ret;
    }

}
