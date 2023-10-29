package dev.mv.engine.resources.types.border;

import java.util.function.BinaryOperator;

public class CircleBorder extends Border{
    private BinaryOperator<Integer> radius;

    @Override
    public Corner createCorner(int index) {
        if (index == 0) {
            return new Corner(radius, (ctx, x, y, radius, strokeWidth, rotation, ox, oy) -> {
                ctx.voidCircle(x, y, this.radius, strokeWidth, radius, rotation, ox, oy);
            });
        }
        return new Corner(radius, (ctx, x, y, radius, strokeWidth, rotation, ox, oy) -> {});
    }

    public BinaryOperator<Integer> getRadius() {
        return radius;
    }

    public void setRadius(BinaryOperator<Integer> radius) {
        this.radius = radius;
    }
}
