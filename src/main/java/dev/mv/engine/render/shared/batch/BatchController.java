package dev.mv.engine.render.shared.batch;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.render.shared.create.RenderBuilder;
import dev.mv.engine.render.shared.shader.Shader;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.utils.collection.Vec;

public class BatchController {

    protected final Vec<Batch> batches = new Vec<>();
    private final String VERTEX_PATH = "/assets/mvengine/shaders/2d/default.vert";
    private final String FRAGMENT_PATH = "/assets/mvengine/shaders/2d/default.frag";
    protected Window win;
    protected int maxBatchSize;
    protected Shader defaultShader, prebuildDefaultShader;
    protected int currentBatch;

    public BatchController(Window window, int batchLimit) {
        if (batchLimit < 14) {
            Exceptions.send(new IllegalArgumentException("Batch limit of " + batchLimit + " is too small, at least 14 is required!"));
        }

        defaultShader = RenderBuilder.newShader(VERTEX_PATH, FRAGMENT_PATH);
        defaultShader.make(win);
        defaultShader.bind();
        prebuildDefaultShader = defaultShader;
        defaultShader.use();

        win = window;
        maxBatchSize = batchLimit;
        currentBatch = 0;
    }

    public void start() {
        batches.push(new RegularBatch(maxBatchSize, win, defaultShader, false));
    }

    protected void nextBatch(boolean strip, boolean clip) {
        currentBatch++;
        try {
            if (batches.get(currentBatch).isStrip() != strip) {
                //TODO: check performance, might be slow if drastic changes between batch types...
                //Might only be an issue when changing what is rendered though, not every frame
                batches.insert(currentBatch, gen(strip, clip));
                //Backup solution, doesn't remake batches but might cause issues if it has to skip a lot of batches and then waste RAM
                //nextBatch(strip);
                //int i = findBatch(strip);
                //Batch tmp = batches.get(currentBatch);
                //batches.insert(currentBatch, batches.get(currentBatch + i));
                //batches.insert(currentBatch + i, tmp);
            }
        } catch (IndexOutOfBoundsException e) {
            batches.push(gen(strip, clip));
        }
    }

    //private int findBatch(boolean strip) {
    //    int i = 1;
    //    while (true) {
    //        try {
    //            if (batches.get(currentBatch + i).isStrip() == strip) {
    //                return i;
    //            }
    //        }  catch (IndexOutOfBoundsException e) {
    //            batches.push(gen(strip));
    //            return i;
    //        }
    //        i++;
    //    }
    //}

    private Batch gen(boolean strip, boolean clip) {
        return strip ? new ChainedBatch(maxBatchSize, win, defaultShader, clip) : new RegularBatch(maxBatchSize, win, defaultShader, clip);
    }

    public void addVertices(VertexGroup vertexData, boolean useCamera, float tR, int tTx, int tTy, int tOx, int tOy, boolean clip) {
        addVertices(vertexData, useCamera, false, tR, tTx, tTy, tOx, tOy, clip, false);
    }

    public void addVertices(VertexGroup vertexData, boolean useCamera, boolean strip, float tR, int tTx, int tTy, int tOx, int tOy, boolean clip, boolean isFont) {
        if (batches.get(currentBatch).isStrip() != strip) {
            nextBatch(strip, clip);
        }
        if (batches.get(currentBatch).isStencil() != clip) {
            nextBatch(strip, clip);
        }
        if (batches.get(currentBatch).isFull(vertexData.length() * Batch.VERTEX_SIZE_FLOATS)) {
            nextBatch(strip, clip);
        }

        batches.get(currentBatch).addVertices(vertexData, useCamera, tR, tTx, tTy, tOx, tOy, isFont);
    }

    public int addTexture(Texture tex) {
        return addTexture(tex, false, false);
    }

    public int addTexture(Texture tex, boolean strip, boolean clip) {
        if (batches.get(currentBatch).isStrip() != strip) {
            nextBatch(strip, clip);
        }
        if (batches.get(currentBatch).isFullOfTextures() || batches.get(currentBatch).isFull(Batch.VERTEX_SIZE_FLOATS * 4)) {
            nextBatch(strip, clip);
        }

        int texID = batches.get(currentBatch).addTexture(tex);

        if (texID == -1) {
            nextBatch(strip, clip);
            texID = batches.get(currentBatch).addTexture(tex);
        }

        return texID;
    }

    public int addTexture(Texture tex, int vertices) {
        return addTexture(tex, vertices, false, false);
    }

    public int addTexture(Texture tex, int vertices, boolean strip, boolean clip) {
        if (batches.get(currentBatch).isStrip() != strip) {
            nextBatch(strip, clip);
        }
        if (batches.get(currentBatch).isFullOfTextures() || batches.get(currentBatch).isFull(vertices)) {
            nextBatch(strip, clip);
        }

        int texID = batches.get(currentBatch).addTexture(tex);

        if (texID == -1) {
            nextBatch(strip, clip);
            texID = batches.get(currentBatch).addTexture(tex);
        }

        return texID;
    }

    public int getNumberOfBatches() {
        return batches.size();
    }

    public void finish() {
        for (int i = 0; i <= currentBatch; i++) {
            batches.get(i).finish();
        }
        currentBatch = 0;
    }

    public void render() {
        defaultShader.use();
        for (int i = 0; i <= currentBatch; i++) {
            batches.get(i).render();
        }
        currentBatch = 0;
    }

    public void finishAndRender() {
        defaultShader.use();
        for (int i = 0; i <= currentBatch; i++) {
            batches.get(i).finish();
            batches.get(i).render();
        }
        currentBatch = 0;
    }

    public void rebuildShader() {
        if (prebuildDefaultShader != null) {
            defaultShader = prebuildDefaultShader;
            defaultShader.use();
            return;
        }
        defaultShader = RenderBuilder.newShader(VERTEX_PATH, FRAGMENT_PATH);
        defaultShader.make(win);
        prebuildDefaultShader = defaultShader;
        defaultShader.use();
        batches.forEach(batch -> batch.setShader(defaultShader));
    }

    public void rebuildShader(String vertexShader, String fragmentShader) {
        defaultShader = RenderBuilder.newShader(vertexShader, fragmentShader);
        defaultShader.make(win);
        defaultShader.use();
        batches.forEach(batch -> batch.setShader(defaultShader));
    }

    public void rebuildShader(Shader shader) {
        defaultShader = shader;
        batches.forEach(batch -> batch.setShader(defaultShader));
    }
}
