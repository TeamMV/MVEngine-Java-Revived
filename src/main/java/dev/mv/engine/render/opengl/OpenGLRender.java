package dev.mv.engine.render.opengl;

import dev.mv.engine.Env;
import dev.mv.engine.render.shared.Render;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.render.shared.batch.Batch;
import dev.mv.engine.render.shared.shader.Shader;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.engine.render.utils.RenderConstants;

import java.util.Arrays;

import static org.lwjgl.opengl.GL46.*;

public class OpenGLRender implements Render {
    private Window window;

    public OpenGLRender(Window window) {
        this.window = window;
    }

    @Override
    public void retrieveVertexData(Texture[] textures, int[] texIds, int[] indices, float[] vertices, int vboId, int iboId, Shader shader, int renderMode, boolean isStencil) {
        if (window == null) {
            throw new IllegalStateException("Window is not set!");
        }

        if (textures != null) {
            int i = 1;
            for (Texture texture : textures) {
                if (texture == null) continue;
                texture.bind(i++);
            }
        }

        if (isStencil) {
            glStencilFunc(GL_ALWAYS, 1, 0xFF);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glStencilMask(0xFF);
        } else {
            glStencilFunc(GL_EQUAL, 1, 0xFF);
            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
            glStencilMask(0x00);
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW);

        if (textures != null) {
            shader.uniform("TEX_SAMPLER", texIds);
        }
        shader.uniform("uResX", (float) window.getWidth());
        shader.uniform("uResY", (float) window.getHeight());

        shader.uniform("uSmoothing", RenderConstants.FONT_SMOOTHING);

        glVertexAttribPointer(0, Batch.POSITION_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.POSITION_OFFSET_BYTES);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, Batch.ROTATION_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.ROTATION_OFFSET_BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, Batch.ROTATION_ORIGIN_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.ROTATION_ORIGIN_OFFSET_BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, Batch.COLOR_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.COLOR_OFFSET_BYTES);
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(4, Batch.UV_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.UV_OFFSET_BYTES);
        glEnableVertexAttribArray(4);
        glVertexAttribPointer(5, Batch.TEX_ID_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.TEX_ID_OFFSET_BYTES);
        glEnableVertexAttribArray(5);
        glVertexAttribPointer(6, Batch.USE_CAMERA_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.USE_CAMERA_OFFSET_BYTES);
        glEnableVertexAttribArray(6);
        glVertexAttribPointer(7, Batch.TRANSFORM_ROTATION_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.TRANSFORM_ROTATION_OFFSET_BYTES);
        glEnableVertexAttribArray(7);
        glVertexAttribPointer(8, Batch.TRANSFORM_TRANSLATE_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.TRANSFORM_TRANSLATE_OFFSET_BYTES);
        glEnableVertexAttribArray(8);
        glVertexAttribPointer(9, Batch.TRANSFORM_ORIGIN_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.TRANSFORM_ORIGIN_OFFSET_BYTES);
        glEnableVertexAttribArray(9);
        glVertexAttribPointer(10, Batch.IS_FONT_SIZE, GL_FLOAT, false, Batch.VERTEX_SIZE_BYTES, Batch.IS_FONT_OFFSET_BYTES);
        glEnableVertexAttribArray(10);

        glDrawElements(renderMode, indices.length, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        if (textures != null) {
            for (Texture texture : textures) {
                if (texture == null) continue;
                texture.unbind();
            }
        }
    }

    @Override
    public int genBuffers() {
        return glGenBuffers();
    }

    @Override
    public void clearStencil() {
        glClear(GL_STENCIL_BUFFER_BIT);
    }
}
