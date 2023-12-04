package dev.mv.engine.render.shared.batch;

import dev.mv.engine.render.shared.Window;
import dev.mv.engine.render.shared.shader.Shader;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL46.GL_TRIANGLES;

public class RegularBatch extends Batch {

    public RegularBatch(int maxSize, Window win, Shader shader, boolean stencil) {
        super(maxSize, win, shader, stencil);
    }

    @Override
    public int getRenderMode() {
        return GL_TRIANGLES;
    }

    @Override
    protected void genIndices(int vertAmount) {
        if (vertAmount == 4) {
            indices[objCount * 6 + 0] = 0 + objCount * 4;
            indices[objCount * 6 + 1] = 1 + objCount * 4;
            indices[objCount * 6 + 2] = 2 + objCount * 4;
            indices[objCount * 6 + 3] = 0 + objCount * 4;
            indices[objCount * 6 + 4] = 2 + objCount * 4;
            indices[objCount * 6 + 5] = 3 + objCount * 4;
        } else {
            indices[objCount * 6 + 0] = 0 + objCount * 4;
            indices[objCount * 6 + 1] = 1 + objCount * 4;
            indices[objCount * 6 + 2] = 2 + objCount * 4;
        }
    }

    @Override
    public boolean isStrip() {
        return false;
    }
}
