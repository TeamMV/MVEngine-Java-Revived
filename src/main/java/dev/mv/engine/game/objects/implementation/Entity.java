package dev.mv.engine.game.objects.implementation;

import dev.mv.engine.game.objects.GameObject2D;
import dev.mv.engine.game.objects.Renderable2D;
import dev.mv.engine.game.objects.physics.RigidDynamic2D;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.render.shared.Drawable;

public abstract class Entity extends GameObject2D implements RigidDynamic2D, Renderable2D {
    private float x, y, w, h, r;

    public abstract Drawable getDrawable();

    @Override
    public void draw(DrawContext ctx2D) {
        getDrawable().draw(ctx2D, x, y, w, h, r);
    }
}
