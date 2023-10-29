package dev.mv.engine.physics.colliders;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.physics.Collider;
import dev.mv.engine.physics.Physics;
import dev.mv.engine.physics.shapes.Circle;
import dev.mv.engine.physics.shapes.Shape;

public class CircleCollider implements Collider {

    private static final String name = CircleCollider.class.getSimpleName();

    private Physics physics;

    public CircleCollider(Physics physics) {
        this.physics = physics;
    }

    @Override
    public boolean checkCollision(Shape a, Shape b) {
        checkType(a, b);
        return false;
    }

    private void checkType(Shape a, Shape b) {
        if (!(a instanceof Circle || b instanceof Circle)) {
            Exceptions.send("BAD_COLLIDER", name, "non circle shapes");
        }
    }
}
