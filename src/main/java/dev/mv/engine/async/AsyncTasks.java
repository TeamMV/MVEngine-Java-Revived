package dev.mv.engine.async;

import dev.mv.engine.utils.generic.pair.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class AsyncTasks {

    private static AtomicInteger currentTask = new AtomicInteger(0);
    static Map<Integer, AsyncTask> tasks = new HashMap<>();

    public static int getNextId() {
        return currentTask.incrementAndGet();
    }

    public static Pair<Runnable, AsyncEvent> createAsynchronousEqualSidedCuboid(IntConsumer taskWithIndex) {
        int i = getNextId();
        return new Pair<>(() -> taskWithIndex.accept(i), AsyncEvent.waitingFor(i));
    }

    public static void addTask(AsyncTask task) {
        if (tasks.containsKey(task.id)) {
            throw new IllegalStateException("Task with id " + task.id + " already exists!");
        }
        tasks.put(task.id, task);
    }

    public static void registerSignal(int id, Signal signal) {
        var task = tasks.get(id);
        if (task != null) {
            task.registerSignal(signal);
        }
    }

    public static boolean taskAlive(int id) {
        return tasks.containsKey(id);
    }

}
