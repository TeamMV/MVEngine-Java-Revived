package dev.mv.engine.render.shared.texture;

import dev.mv.engine.render.shared.create.RenderBuilder;
import dev.mv.engine.resources.CompositeResource;
import dev.mv.engine.resources.Resource;
import dev.mv.engine.utils.generic.pair.Pair;

import java.io.IOException;
import java.io.InputStream;

public class TextureRegion implements CompositeResource {
    Texture tex;
    private int id;
    private int width;
    private int height;
    private float[] uv;

    public TextureRegion(Texture tex, int x, int y, int width, int height) {
        this.width = tex.getWidth();
        this.height = tex.getHeight();
        this.id = tex.getId();
        this.tex = tex;

        this.uv = createUV(x, y, width, height);
    }

    private float[] createUV(int x, int y, int width, int height) {
        return new float[]{
            (float) x / (float) this.width,
            (float) (x + width) / (float) this.width,
            (float) (y + height) / (float) this.height,
            (float) (y) / (float) this.height
        };
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float[] getUVCoordinates() {
        return uv;
    }

    public Texture getParentTexture() {
        return tex;
    }

    @Override
    public Pair<Resource.Type, String>[] getDependencies() {
        return new Pair[] {new Pair<>(Resource.Type.TEXTURE, tex.getResId())};
    }
}
