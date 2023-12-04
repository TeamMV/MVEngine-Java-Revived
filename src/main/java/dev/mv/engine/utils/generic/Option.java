package dev.mv.engine.utils.generic;

import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.nullHandler.NullHandler;

import java.util.function.Function;
import java.util.function.Supplier;

import static dev.mv.engine.utils.generic.Result.Err;
import static dev.mv.engine.utils.generic.Result.Ok;

public class Option<T> {

    private T value = null;

    private Option() {
    }

    private Option(T value) {
        this.value = value;
    }

    public static <T> Option<T> Some(T t) {
        return new Option<>(t);
    }

    public static Option<Null> Some() {
        return new Option<>(Null.INSTANCE);
    }

    public static <T> Option<T> None() {
        return new Option<>();
    }

    public boolean isSome() {
        return value != null;
    }

    public boolean isNone() {
        return value == null;
    }

    public Option<T> ifNone(Runnable runnable) {
        if (isNone()) {
            runnable.run();
        }
        return this;
    }

    public <E extends Throwable> Option<T> ifNoneThrow(Supplier<E> supplier) throws E {
        if (isNone()) {
            throw supplier.get();
        }
        return this;
    }

    public T unwrap() {
        if (isNone()) {
            throw new IllegalStateException("Called Option.unwrap() on 'None' value!");
        }
        return value;
    }

    public T unwrapOrElse(T defaultValue) {
        if (isNone()) {
            return defaultValue;
        }
        return value;
    }

    public T unwrapOrElse(Supplier<T> supplier) {
        if (isNone()) {
            return supplier.get();
        }
        return value;
    }

    public T unwrapChecked() {
        return value;
    }

    public T expect(String errorMessage) {
        if (isNone()) {
            throw new IllegalStateException(errorMessage);
        }
        return value;
    }

    public <E extends Throwable> T expect(E throwable) throws E {
        if (isNone()) {
            throw throwable;
        }
        return value;
    }

    public <U> Option<U> map(Function<T, U> mapper) {
        if (isNone()) {
            return None();
        }
        return Some(mapper.apply(value));
    }

    public <U> U mapOrElse(Function<T, U> mapper, U defaultValue) {
        if (isNone()) {
            return defaultValue;
        }
        return mapper.apply(value);
    }

    public <U> U mapOrElse(Function<T, U> mapper, Supplier<U> supplier) {
        if (isNone()) {
            return supplier.get();
        }
        return mapper.apply(value);
    }

    public Option<T> orElse(T value) {
        if (isNone()) {
            this.value = value;
        }
        return this;
    }

    public Option<T> orElse(Supplier<T> supplier) {
        if (isNone()) {
            this.value = supplier.get();
        }
        return this;
    }

    public <E> Result<T, E> okOrElse(E error) {
        if (isNone()) {
            return Err(error);
        }
        return Ok(value);
    }

    public <E> Result<T, E> okOrElse(Supplier<E> supplier) {
        if (isNone()) {
            return Err(supplier.get());
        }
        return Ok(value);
    }

    public NullHandler<T> asNullHandler() {
        return Utils.ifNotNull(value);
    }
}
