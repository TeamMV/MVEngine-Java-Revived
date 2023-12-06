package dev.mv.engine.render.shared.texture;

import dev.mv.engine.resources.Resource;

public interface Texture extends Resource {
    void bind(int index);

    void unbind();

    int getWidth();

    int getHeight();

    int getId();

    TextureRegion cutRegion(int x, int y, int width, int height);

    TextureRegion convertToRegion();
}
