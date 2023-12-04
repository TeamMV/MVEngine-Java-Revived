package dev.mv.engine.physics.colliders;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.physics.Collider;
import dev.mv.engine.physics.Physics;
import dev.mv.engine.physics.shapes.Circle;
import dev.mv.engine.physics.shapes.Shape;
import dev.mv.engine.utils.Utils;

public class SimpleCircleCollider implements Collider {

    private static final String name = SimpleCircleCollider.class.getSimpleName();
    private Physics physics;

    public SimpleCircleCollider(Physics physics) {
        this.physics = physics;
    }

    @Override
    public boolean checkCollision(Shape a, Shape b) {
        checkType(a, b);
        float xDist = a.getX() - b.getX();
        float yDist = a.getY() - b.getY();
        return Utils.square(xDist) + Utils.square(yDist) <= Utils.square(((Circle) a).getRadius() + ((Circle) b).getRadius());
    }

    private void checkType(Shape a, Shape b) {
        if (!(a instanceof Circle && b instanceof Circle)) {
            Exceptions.send("BAD_COLLIDER", name, "non circle shapes");
        }
    }
}
