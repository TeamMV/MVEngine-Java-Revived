package dev.mv.engine.game;

import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.MVEngine;
import dev.mv.engine.render.shared.*;

class WindowManager implements ApplicationLoop {

    private Game game;
    private DrawContext ctx;
    private Camera camera;
    private DefaultCameraController cameraController;

    WindowManager(Game game) {
        this.game = game;
    }

    @Override
    public void start(MVEngine engine, Window window) {
        ctx = new DrawContext(window);
        camera = window.getCamera();
        camera.setSpeed(0.2f);
        cameraController = new DefaultCameraController(camera);
    }

    @Override
    public void update(MVEngine engine, Window window) {

    }

    @Override
    public void draw(MVEngine engine, Window window) {

    }

    @Override
    public void exit(MVEngine engine, Window window) {

    }
}
