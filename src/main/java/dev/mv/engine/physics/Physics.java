package dev.mv.engine.physics;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.physics.colliders.*;
import dev.mv.engine.physics.shapes.Circle;
import dev.mv.engine.physics.shapes.Oval;
import dev.mv.engine.physics.shapes.Rectangle;
import dev.mv.engine.physics.shapes.Shape;

public class Physics {

    private static Physics instance = null;

    private final Collider aabb;
    private final Collider rect;
    private final Collider simpleCircle;
    private final Collider circle;
    private final Collider simpleOval;
    private final Collider oval;
    private final Collider polygon;

    private Physics() {
        aabb = new SimpleRectangleCollider(this);
        rect = new RectangleCollider(this);
        simpleCircle = new SimpleCircleCollider(this);
        circle = new CircleCollider(this);
        simpleOval = new SimpleOvalCollider(this);
        oval = new OvalCollider(this);
        polygon = new PolygonCollider(this);
    }

    public static Physics init() {
        if (instance != null) Exceptions.send(new IllegalStateException("Physics2D already initialized"));
        instance = new Physics();
        return instance;
    }

    public void terminate() {
        instance = null;
    }

    public Collider getCollider(Shape a, Shape b) {
        if (a.equalsType(b)) {
            if (a instanceof Rectangle) {
                if (a.getRotation() % 90 == 0 && a.getRotation() % 90 == 0) {
                    return aabb;
                } else {
                    return rect;
                }
            } else if (a instanceof Circle) {
                return simpleCircle;
            } else if (a instanceof Oval) {
                return simpleOval;
            }
        }
        if (a instanceof Circle || b instanceof Circle) {
            return circle;
        }
        if (a instanceof Oval || b instanceof Oval) {
            return oval;
        }
        return null;
    }

}
