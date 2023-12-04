package dev.mv.engine.utils.function;

@FunctionalInterface
public interface FaultyRunnable<E extends Throwable> {

    void run() throws E;

}
