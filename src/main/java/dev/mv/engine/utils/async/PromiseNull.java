package dev.mv.engine.utils.async;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Null async function return type. Return this from a function to run the code inside the Promise asynchronously.
 * Does not return a value, unlike the {@link Promise}.
 *
 * @author Maxim Savenkov
 */
public class PromiseNull {

    private volatile boolean done = false;

    private volatile Thread thread;

    /**
     * Make a new {@link PromiseNull}, the function will be run automatically in a new thread. Return this from a function.
     *
     * @param function the function to run, takes in a {@link ResolverNull} and {@link Rejector}.
     */
    public PromiseNull(@NotNull BiConsumer<ResolverNull, Rejector> function) {
        thread = new Thread(() -> {
            function.accept(() -> {
                thread.interrupt();
                done = true;
            }, new Rejector() {
                @Override
                public void reject(Throwable t) {
                    done = true;
                    throwError(t);
                }

                @Override
                public void reject(String s) {
                    done = true;
                    throwError(s);
                }

                @Override
                public void reject(String s, Throwable t) {
                    done = true;
                    throwError(s, t);
                }
            });
            done = true;
        });
        thread.start();
    }

    /**
     * Make a new {@link PromiseNull}, the function will be run automatically in a new thread. Return this from a function.
     *
     * @param function the function to run, takes in a {@link ResolverNull}.
     */
    public PromiseNull(@NotNull Consumer<ResolverNull> function) {
        thread = new Thread(() -> function.accept(() -> {
            thread.interrupt();
            done = true;
        }));
        thread.start();
    }

    /**
     * Make a new {@link PromiseNull}, the function will be run automatically in a new thread. Return this from a function.
     *
     * @param function the function to run, return or implied return to resolve, throw to reject.
     */
    public PromiseNull(@NotNull Runnable function) {
        thread = new Thread(() -> {
            try {
                function.run();
                done = true;
            } catch (Throwable t) {
                done = true;
                throwError(t);
            }
        });
        thread.start();
    }

    /**
     * Make a new {@link PromiseNull} that will run after the current async function resolves.
     *
     * @param function the function to run.
     * @return a new {@link PromiseNull}.
     */
    public PromiseNull then(@NotNull Runnable function) {
        return new PromiseNull((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        function.run();
                        res.resolve();
                    } catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    /**
     * Make a new {@link Promise} that will run after the current async function resolves.
     *
     * @param function the function to run, that returns a value.
     * @return a new {@link Promise}.
     */
    public <R> Promise<R> then(@NotNull Supplier<R> function) {
        return new Promise<R>((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        res.resolve(function.get());
                    } catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    /**
     * Run a function after the current async function resolves, but on the main thread.
     *
     * @param function the function to run.
     */
    public void thenSync(@NotNull Runnable function) {
        while (true) {
            if (done) {
                function.run();
                break;
            }
        }
    }

    private void throwError(Throwable cause) {
        throw new RuntimeException(new PromiseRejectedException(cause));
    }

    private void throwError(String message) {
        throw new RuntimeException(new PromiseRejectedException(message));
    }

    private void throwError(String message, Throwable cause) {
        throw new RuntimeException(new PromiseRejectedException(message, cause));
    }
}
