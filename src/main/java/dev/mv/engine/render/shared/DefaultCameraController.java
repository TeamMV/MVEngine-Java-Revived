package dev.mv.engine.render.shared;

import dev.mv.engine.input.Input;

public class DefaultCameraController {
    private Camera camera;

    public DefaultCameraController(Camera camera) {
        this.camera = camera;
    }

    public void update() {
        if (Input.keys[Input.KEY_W]) camera.move(0, 0, -1);
        if (Input.keys[Input.KEY_A]) camera.move(-1, 0, 0);
        if (Input.keys[Input.KEY_S]) camera.move(0, 0, 1);
        if (Input.keys[Input.KEY_D]) camera.move(1, 0, 0);
        if (Input.keys[Input.KEY_SPACE]) camera.move(0, 1, 0);
        if (Input.keys[Input.KEY_LEFT_SHIFT]) camera.move(0, -1, 0);
    }
}
