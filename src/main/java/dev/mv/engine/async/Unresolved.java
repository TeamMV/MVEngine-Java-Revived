package dev.mv.engine.async;

import dev.mv.engine.utils.BinaryFunction;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class Unresolved<E> {
    private final AtomicBoolean done = new AtomicBoolean(false);
    private E result;
    private Throwable error;

    public Unresolved(BiConsumer<Resolve<E>, Cancel> code) {
        AsyncManager.postRunnable((AsyncRunnable.SingleTask) () -> {
            code.accept(e -> {
                result = e;
            }, e -> {
                error = e;
            });
            Unresolved.this.done.set(true);
            Unresolved.this.notifyAll();
        });
    }

    public E waitForChecked() {
        try {
            return waitFor();
        } catch (Throwable e) {
            return null;
        }
    }

    public E waitFor() throws Throwable {
        if (!done.get()) this.wait();
        if (error != null) throw error;
        return result;
    }

    public interface Resolve<E> {
        void apply(E e);
    }

    public interface Cancel {
        void cancel(Throwable e);
    }
}
