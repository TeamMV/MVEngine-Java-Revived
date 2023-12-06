package dev.mv.engine.render.shared.texture;

import dev.mv.engine.render.shared.create.RenderBuilder;
import dev.mv.engine.resources.Resource;

import java.io.IOException;
import java.io.InputStream;

public class TextureRegion {
    Texture tex;
    private int id;
    private int width;
    private int height;
    private float[] uv;
    private String resId;

    public TextureRegion(Texture tex, int x, int y, int width, int height) {
        this(tex, x, y, width, height, Resource.NO_R);
    }

    public TextureRegion(Texture tex, int x, int y, int width, int height, String resId) {
        this.width = tex.getWidth();
        this.height = tex.getHeight();
        this.id = tex.getId();
        this.tex = tex;

        this.uv = createUV(x, y, width, height);
        this.resId = resId;
        
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
    public String resId() {
        return resId;
    }

    @Override
    public Type type() {
        return Type.TEXTURE_REGION;
    }

    public TextureRegion() {}

    @Override
    public void load(InputStream inputStream, String resId) throws IOException {
        this.resId = resId;
        tex = RenderBuilder.newTexture(inputStream);
        width = tex.getWidth();
        height = tex.getHeight();
        id = tex.getId();
        this.uv = createUV(0, 0, width, height);
        
    }
}
