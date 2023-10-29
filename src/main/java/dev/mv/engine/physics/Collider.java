package dev.mv.engine.physics;

import dev.mv.engine.physics.shapes.Shape;

public interface Collider {

    boolean checkCollision(Shape a, Shape b);

}
