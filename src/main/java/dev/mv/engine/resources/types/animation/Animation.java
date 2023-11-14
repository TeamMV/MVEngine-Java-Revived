package dev.mv.engine.resources.types.animation;

import dev.mv.engine.TimingManager;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.resources.Resource;

import java.util.function.Predicate;

public abstract class Animation implements Resource {
    private boolean isRunning, infinite;
    private float duration, currentTime;
    protected float delay = 0f;
    private int timingId;
    private int iteration;

    //draw-related
    protected int x, y, width, height;
    protected float rotation;
    protected int originX, originY;

    String resId;

    private final Predicate<Integer> timingAction = i -> {
        iteration = i;
        boolean finished = apply(currentTime, i);
        if (finished) {
            stop();
            if (infinite) {
                start();
            }
        }
        return finished;
    };

    protected abstract boolean apply(float time, int iteration);

    public abstract void draw(DrawContext ctx);

    public void start() {
        isRunning = true;
        currentTime = 0;
        timingId = TimingManager.queue(delay, timingAction);
    }

    public void pause() {
        isRunning = false;
        TimingManager.unqueue(timingId);
    }

    public void resume() {
        isRunning = true;
        timingId = TimingManager.queue(delay, currentTime, iteration, timingAction);
    }

    public void stop() {
        isRunning = false;
        currentTime = 0;
        iteration = 0;
        TimingManager.unqueue(timingId);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    @Override
    public String resId() {
        return resId;
    }

    @Override
    public Type type() {
        return Type.ANIMATION;
    }
}
