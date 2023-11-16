package dev.mv.engine.async;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.utils.collection.Vec;
import dev.mv.utils.generic.pair.Pair;

public class MonoThreadAsyncProcessor extends Thread {

    private final Signal signal = new Signal();
    private Vec<Pair<AsyncRunnable, Pair<Runnable, AsyncEvent>>> runnables = new Vec<>();
    private boolean instant = false;

    public int countTasks() {
        return runnables.len();
    }

    public boolean isBusy() {
        return runnables.len() > 0;
    }

    public void postRunnable(AsyncRunnable runnable) {
        var next = runnable.next();
        if (next == null) {
            return;
        }
        runnables.push(new Pair<>(runnable, next));
        signal.wake();
    }

    private synchronized void process() {
        while (true) {
            instant = false;
            if (runnables.isEmpty()) {
                waitForSignal();
            }

            Vec<Pair<AsyncRunnable, Pair<Runnable, AsyncEvent>>> newRunnables = new Vec<>();

            for (var next : runnables) {
                if (next.b.a != null) {
                    runTask(next, newRunnables);
                }
                else if (next.b.b != null && next.b.b.done()) {
                    var newNext = next.a.next();
                    if (newNext != null) {
                        next.b = newNext;
                        runTask(next, newRunnables);
                    }
                }
            }

            runnables = newRunnables;

            if (!instant) {
                waitForSignal();
            }
        }
    }

    private void runTask(Pair<AsyncRunnable, Pair<Runnable, AsyncEvent>> next, Vec<Pair<AsyncRunnable, Pair<Runnable, AsyncEvent>>> queue) {
        next.b.a.run();
        if (next.b.b != null && next.b.b.done()) {
            instant = true;
            var newNext = next.a.next();
            if (newNext != null) {
                next.b = newNext;
                queue.push(next);
            }
        }
        else {
            next.b.a = null;
            next.b.b.register(signal);
            queue.push(next);
        }
    }

    private synchronized void waitForSignal() {
        try {
            signal.wait();
        } catch (Exception e) {
            Exceptions.send(e);
        }
    }

    @Override
    public void run() {
        process();
    }
}
