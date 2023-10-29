package dev.mv.engine.physics.colliders;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.physics.Collider;
import dev.mv.engine.physics.Physics;
import dev.mv.engine.physics.shapes.Rectangle;
import dev.mv.engine.physics.shapes.Shape;

public class RectangleCollider implements Collider {

    private static final String name = RectangleCollider.class.getSimpleName();
    private Physics physics;

    public RectangleCollider(Physics physics) {
        this.physics = physics;
    }

    @Override
    public boolean checkCollision(Shape a, Shape b) {
        checkType(a, b);
        return false;
    }

    private void checkType(Shape a, Shape b) {
        if (!(a instanceof Rectangle && b instanceof Rectangle)) {
            Exceptions.send("BAD_COLLIDER", name, "non rectangular shapes");
        }
    }
}
