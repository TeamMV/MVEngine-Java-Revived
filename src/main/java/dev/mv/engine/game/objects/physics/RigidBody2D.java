package dev.mv.engine.game.objects.physics;

import dev.mv.engine.physics.shapes.Shape;

public interface RigidBody2D extends PhysicsActor {

    Shape getHitbox();

    default Shape.BoundingBox getBoundingBox() {
        return getHitbox().getBoundingBox();
    }

}
