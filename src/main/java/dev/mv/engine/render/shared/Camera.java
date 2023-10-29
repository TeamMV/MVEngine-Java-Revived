package dev.mv.engine.render.shared;

import org.joml.Vector2f;

public class Camera {
    private Vector2f location;
    private float rotation;
    private float speed;

    public Camera() {
        location = new Vector2f(0, 0);
        rotation = 0f;
    }

    public Camera(Vector2f location, float rotation) {
        this.location = location;
        this.rotation = rotation;
    }

    public void move(float x, float y, float z) {
        x *= speed;
        y *= speed;
        z *= speed;
        location.x += x;
        location.y += y;
    }

    public void moveTo(float x, float y, float z) {
        location.x = x;
        location.y = y;
    }

    public void rotate(float rotation) {
        this.rotation += rotation;
    }

    public void rotateTo(float rotation) {
        this.rotation = rotation;
    }

    public Vector2f getLocation() {
        return location;
    }

    public float getRotation() {
        return rotation;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }
}
