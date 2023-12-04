package dev.mv.engine.utils.check;

import java.util.function.Consumer;

public class Match<T> {

    private T value;
    private boolean happened = false;

    public Match(T value) {
        this.value = value;
    }

    public Match<T> occasion(T value, Runnable function) {
        if (this.value.equals(value)) {
            happened = true;
            function.run();
        }
        return this;
    }

    public Match<T> occasion(T value, Consumer<T> function) {
        if (this.value.equals(value)) {
            happened = true;
            function.accept(this.value);
        }
        return this;
    }

    public Match<T> __(Runnable function) {
        if (!happened) {
            function.run();
        }
        return this;
    }

    public Match<T> __(Consumer<T> function) {
        if (!happened) {
            function.accept(value);
        }
        return this;
    }

    public Match<T> noOccasion(Runnable function) {
        if (!happened) {
            function.run();
        }
        return this;
    }

    public Match<T> noOccasion(Consumer<T> function) {
        if (!happened) {
            function.accept(value);
        }
        return this;
    }

}
