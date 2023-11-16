package dev.mv.engine.async;

public class AsyncTask {
    protected final int id;
    private Signal signal;

    public AsyncTask(int id) {
        this.id = id;
    }

    protected void ended() {
        if (signal != null) {
            signal.wake();
        }
        AsyncTasks.tasks.remove(id);
    }

    public void registerSignal(Signal signal) {
        this.signal = signal;
    }
}
