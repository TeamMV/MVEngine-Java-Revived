package dev.mv.engine;

import dev.mv.engine.render.shared.Window;
import dev.mv.engine.utils.generic.pair.Pair;
import dev.mv.engine.utils.generic.triplet.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TimingManager implements Looper {
    //                          Delay  current        Action              iteration result
    private static List<Triplet<Float, Float, Triplet<Predicate<Integer>, Integer, Boolean>>> queue;

    private TimingManager() {
        queue = new ArrayList<>();
    }

    static {
        MVEngine.instance().registerLooper(new TimingManager());
    }

    public static int queue(Predicate<Integer> action) {
        return queue(0f, action);
    }

    public static int queue(float delay, Predicate<Integer> action) {
        return queue(delay, 0f, 0, action);
    }

    public static int queue(float delay, float currentTime, int iteration, Predicate<Integer> action) {
        queue.add(0, new Triplet<>(delay, currentTime, new Triplet<>(action, iteration, false)));
        return queue.size() - 1;
    }

    public static void unqueue(int timingId) {
        if (queue.size() > timingId) queue.remove(timingId);
    }

    private static void frame(long currentFrame, float dt) {
        List<Integer> remove = new ArrayList<>();
        for (int i = 0; i < queue.size(); i++) {
            var item = queue.get(i);
            item.b += dt / 60f;
            if (item.b >= item.a) {
                item.b = 0.0f;
                if (item.c.c) {
                    remove.add(i);
                }
                item.c.c = item.c.a.test(item.c.b++);
            }
        }

        for (int idx : remove) {
            if (queue.size() > idx) queue.remove(idx);
        }
    }

    @Override
    public void loop(Window window) {
        frame(window.getCurrentFrame(), (float) window.getDeltaTime());
    }
}
