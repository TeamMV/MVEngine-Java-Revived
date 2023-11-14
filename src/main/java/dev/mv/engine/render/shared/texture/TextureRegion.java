package dev.mv.engine.render.shared.texture;

import dev.mv.engine.resources.Resource;

public class TextureRegion implements Resource {
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
        register();
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
        return Type.TEXTURE;
    }
}
