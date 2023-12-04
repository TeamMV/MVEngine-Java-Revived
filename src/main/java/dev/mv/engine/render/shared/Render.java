package dev.mv.engine.render.shared;


import dev.mv.engine.render.shared.shader.Shader;
import dev.mv.engine.render.shared.texture.Texture;

public interface Render {

    void retrieveVertexData(Texture[] textures, int[] texIds, int[] indices, float[] vertices, int vboId, int iboId, Shader shader, int renderMode, boolean isStencil);

    int genBuffers();

    void clearStencil();
}
