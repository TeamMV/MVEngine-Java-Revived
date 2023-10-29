package dev.mv.engine.resources.types.border;

public class RoundRectangleBorder extends Border{
    private int radius = 10;

    @Override
    public Corner createCorner(int index) {
        return switch (index) {
            default -> null;
            case 0 -> new Corner(radius, (ctx, x, y, radius, strokeWidth, rot, ox, oy) -> ctx.voidArc(x + radius, y + radius, radius - strokeWidth / 2, strokeWidth, 90, 180, radius, rot, ox, oy));
            case 1 -> new Corner(radius, (ctx, x, y, radius, strokeWidth, rot, ox, oy) -> ctx.voidArc(x + radius, y - radius, radius - strokeWidth / 2, strokeWidth, 90, 90, radius, rot, ox, oy));
            case 2 -> new Corner(radius, (ctx, x, y, radius, strokeWidth, rot, ox, oy) -> ctx.voidArc(x - radius, y - radius, radius - strokeWidth / 2, strokeWidth, 90, 0, radius, rot, ox, oy));
            case 3 -> new Corner(radius, (ctx, x, y, radius, strokeWidth, rot, ox, oy) -> ctx.voidArc(x - radius, y + radius, radius - strokeWidth / 2, strokeWidth, 90, 270, radius, rot, ox, oy));
        };
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        for (int i = 0; i < 4; i++) {
            corners[i] = createCorner(i);
        }
    }
}
