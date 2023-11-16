package dev.mv.engine.async;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.utils.Utils;
import dev.mv.utils.generic.pair.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AsyncManager {
    private List<MonoThreadAsyncProcessor> workers;

    public AsyncManager(int amountWorkers) {
        if (amountWorkers <= 0) {
            Exceptions.send(new IllegalArgumentException("Amount of workers must be greater than 0"));
        } else {
            this.workers = new ArrayList<>(amountWorkers);
            for (int i = 0; i < amountWorkers; i++) {
                MonoThreadAsyncProcessor p = new MonoThreadAsyncProcessor();
                p.start();
                workers.add(p);
            }
        }
    }

    public void postRunnable(AsyncRunnable runnable) {
        var p =  workers.stream().min(Comparator.comparingInt(MonoThreadAsyncProcessor::countTasks)).get();
        p.postRunnable(runnable);
    }
}
