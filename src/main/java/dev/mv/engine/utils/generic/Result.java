package dev.mv.engine.utils.generic;

import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.nullHandler.NullHandler;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.mv.engine.utils.generic.Option.None;

public class Result<T, E> {

    private final T value;
    private final E error;

    private Result(T value, E ignore) {
        this.value = value;
        this.error = null;
    }

    private Result(E error) {
        this.value = null;
        this.error = error;
    }

    public static <T, E> Result<T, E> Ok(T data) {
        return new Result<T, E>(data, null);
    }

    public static <E> Result<Null, E> Ok() {
        return new Result<>(Null.INSTANCE, null);
    }

    public static <T, E> Result<T, E> Err(E error) {
        return new Result<T, E>(error);
    }

    public static <T> Result<T, Null> Err() {
        return new Result<>(Null.INSTANCE);
    }

    public boolean isOk() {
        return value != null;
    }

    public boolean isErr() {
        return error != null;
    }

    public Option<T> ok() {
        if (isErr()) return None();
        return Option.Some(value);
    }

    public Option<E> err() {
        if (isOk()) return None();
        return Option.Some(error);
    }

    public T unwrap() {
        if (isErr()) {
            throw new IllegalStateException("Called Error.unwrap() on 'Err' value!");
        }
        return value;
    }

    public <E extends Throwable> T unwrapOrThrow() throws E {
        if (isOk()) return value;
        throwIfErr();
        return null;
    }

    public T unwrapOrElse(T value) {
        if (isErr()) {
            return value;
        }
        return this.value;
    }

    public T unwrapOrElse(Supplier<T> supplier) {
        if (isOk()) {
            return supplier.get();
        }
        return value;
    }

    public T unwrapOrElse(Consumer<E> onErr) {
        if (isErr()) {
            onErr.accept(error);
            return null;
        }
        return value;
    }

    public T unwrapChecked() {
        return value;
    }

    public E unwrapErr() {
        if (isOk()) {
            throw new IllegalStateException("Called Error.unwrapErr() on 'Ok' value!");
        }
        return error;
    }

    public E unwrapErrOrElse(E error) {
        if (isOk()) {
            return error;
        }
        return this.error;
    }

    public E unwrapErrOrElse(Supplier<E> supplier) {
        if (isOk()) {
            return supplier.get();
        }
        return error;
    }

    public E unwrapErrOrElse(Consumer<T> onErr) {
        if (isOk()) {
            onErr.accept(value);
            return null;
        }
        return error;
    }

    public E unwrapErrChecked() {
        return error;
    }

    public <U> Result<U, E> map(Function<T, U> mapper) {
        if (isErr()) return Err(error);
        return new Result<U, E>(mapper.apply(value), error);
    }

    public <U> U mapOrElse(Function<T, U> mapper, U defaultValue) {
        if (isErr()) {
            return defaultValue;
        }
        return mapper.apply(value);
    }

    public <U> U mapOrElse(Function<T, U> mapper, Supplier<U> supplier) {
        if (isErr()) {
            return supplier.get();
        }
        return mapper.apply(value);
    }

    public <U> Result<T, U> mapErr(Function<E, U> mapper) {
        if (isOk()) return Ok(value);
        return new Result<T, U>(value, mapper.apply(error));
    }

    public <U> U mapErrOrElse(Function<E, U> mapper, U defaultValue) {
        if (isErr()) {
            return defaultValue;
        }
        return mapper.apply(error);
    }

    public <U> U mapErrOrElse(Function<E, U> mapper, Supplier<U> supplier) {
        if (isErr()) {
            return supplier.get();
        }
        return mapper.apply(error);
    }

    public <U extends Throwable> Result<T, E> throwIfErr() throws U {
        if (isOk()) return this;
        if (error instanceof Throwable) {
            throw (U) error;
        }
        throw new IllegalStateException("Result returned error: " + error);
    }

    public NullHandler<T> asNullHandler() {
        return isErr() ? Utils.ifNotNull(null) : Utils.ifNotNull(value);
    }

}
