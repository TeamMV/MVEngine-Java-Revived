package dev.mv.engine.physics.colliders;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.physics.Collider;
import dev.mv.engine.physics.Physics;
import dev.mv.engine.physics.shapes.Oval;
import dev.mv.engine.physics.shapes.Shape;

public class PolygonCollider implements Collider {

    private static final String name = PolygonCollider.class.getSimpleName();

    private Physics physics;

    public PolygonCollider(Physics physics) {
        this.physics = physics;
    }

    @Override
    public boolean checkCollision(Shape a, Shape b) {
        checkType(a, b);

        return false;
    }

    private void checkType(Shape a, Shape b) {
        if ((a instanceof Oval || b instanceof Oval)) {
            Exceptions.send("BAD_COLLIDER", name, "shapes without a finite amount of vertices");
        }
    }
}
