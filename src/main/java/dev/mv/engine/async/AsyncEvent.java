package dev.mv.engine.async;

public interface AsyncEvent {

    void register(Signal signal);

    boolean done();

    static AsyncEvent empty() {
        return new AsyncEvent() {
            @Override
            public void register(Signal signal) {
                signal.wake();
            }

            @Override
            public boolean done() {
                return true;
            }
        };
    }

    static AsyncEvent waitingFor(int i) {
        return new AsyncEvent() {
            @Override
            public void register(Signal signal) {
                AsyncTasks.registerSignal(i, signal);
            }

            @Override
            public boolean done() {
                return !AsyncTasks.taskAlive(i);
            }
        };
    }

}
