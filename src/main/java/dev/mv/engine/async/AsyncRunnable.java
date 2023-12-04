package dev.mv.engine.async;

import dev.mv.engine.utils.generic.pair.Pair;

import java.util.concurrent.atomic.AtomicBoolean;

public interface AsyncRunnable {

    Pair<Runnable, AsyncEvent> next();

    @FunctionalInterface
    interface SingleTask extends AsyncRunnable, Runnable {
        AtomicBoolean done = new AtomicBoolean(false);

        @Override
        default Pair<Runnable, AsyncEvent> next() {
            if (!done.get()) {
                done.set(true);
                return new Pair<>(this, null);
            }
            return null;
        }
    }

    @FunctionalInterface
    interface ChainedTask extends AsyncRunnable {
        Runnable[] getRunnables();

        @Override
        default Pair<Runnable, AsyncEvent> next() {
            int i = 0;
            Runnable[] runnables = ChainedTask.this.getRunnables();
            if (i < runnables.length) {
                i++;
                return new Pair<>(runnables[i], null);
            }
            return null;
        }
    }
}
