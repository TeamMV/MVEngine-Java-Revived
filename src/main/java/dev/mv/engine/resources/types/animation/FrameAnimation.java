package dev.mv.engine.resources.types.animation;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.types.SpriteCollection;

public class FrameAnimation extends Animation {
    private SpriteCollection sprites;
    private TextureRegion current;

    private FrameAnimation(SpriteCollection sprites) {
        this.sprites = sprites;
        this.current = sprites.getSprite(0);
    }

    @Override
    protected boolean apply(float time, int iteration) {
        if (iteration >= sprites.getAmount()) return true;
        current = sprites.getSprite(iteration);
        return false;
    }

    @Override
    public void draw(DrawContext ctx) {
        ctx.color(Color.BLACK);
        ctx.image(x, y, width, height, current, rotation, originX, originY);
    }

    public static FrameAnimation fromSprites(SpriteCollection sprites) {
        return new FrameAnimation(sprites);
    }

    public void computeDelay() {
        int amt = sprites.getAmount();
        this.delay = getDuration() / (float) amt;
    }
}
