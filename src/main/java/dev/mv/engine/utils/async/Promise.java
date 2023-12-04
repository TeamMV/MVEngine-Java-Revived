package dev.mv.engine.utils.async;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Non-null async function return type. Return this from a function to run the code inside the Promise asynchronously.
 * Returns a value, unlike {@link PromiseNull}.
 *
 * @author Maxim Savenkov
 */
public class Promise<T> {

    private volatile T ret;
    private volatile boolean done = false;
    private volatile Thread thread;

    /**
     * Make a new {@link Promise}, the function will be run automatically in a new thread. Return this from a function.
     *
     * @param function the function to run, takes in a {@link Resolver} and {@link Rejector}.
     */
    public Promise(@NotNull BiConsumer<Resolver<T>, Rejector> function) {
        thread = new Thread(() -> {
            function.accept(val -> {
                thread.interrupt();
                ret = val;
                done = true;
            }, new Rejector() {
                @Override
                public void reject(Throwable t) {
                    thread.interrupt();
                    done = true;
                    throwError(t);
                }

                @Override
                public void reject(String s) {
                    thread.interrupt();
                    done = true;
                    throwError(s);
                }

                @Override
                public void reject(String s, Throwable t) {
                    thread.interrupt();
                    done = true;
                    throwError(s, t);
                }
            });
            if (!done) {
                done = true;
                ret = null;
            }
        });
        thread.start();
    }

    /**
     * Make a new {@link Promise}, the function will be run automatically in a new thread. Return this from a function.
     *
     * @param function the function to run, takes in a {@link Resolver}.
     */
    public Promise(@NotNull Consumer<Resolver<T>> function) {
        thread = new Thread(() -> {
            function.accept(val -> {
                thread.interrupt();
                ret = val;
                done = true;
            });
            if (!done) {
                done = true;
                ret = null;
            }
        });
        thread.start();
    }

    /**
     * Make a new {@link Promise}, the function will be run automatically in a new thread. Return this from a function.
     *
     * @param function the function to run, return to resolve, throw to reject.
     */
    public Promise(@NotNull Supplier<T> function) {
        thread = new Thread(() -> {
            try {
                ret = function.get();
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
     * @param consumer the function to run, takes in the output of the current async function as a parameter.
     * @return a new {@link PromiseNull}.
     */
    public PromiseNull then(@NotNull Consumer<T> consumer) {
        return new PromiseNull((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        consumer.accept(ret);
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
     * @param function the function to run, takes in the output of the current async function as a parameter, return to resolve, throw to reject.
     * @return a new {@link Promise}.
     */
    public <R> Promise<R> then(@NotNull Function<T, R> function) {
        return new Promise<R>((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        res.resolve(function.apply(ret));
                    } catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    /**
     * Run a consumer function after the current async function resolves, but on the main thread.
     *
     * @param consumer the function to run, takes in the output of the current async function as a parameter.
     */
    public void thenSync(@NotNull Consumer<T> consumer) {
        while (true) {
            if (done) {
                consumer.accept(ret);
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
