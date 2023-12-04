package dev.mv.engine.render.shared;

import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.input.GlfwClipboard;
import dev.mv.engine.render.shared.batch.BatchController;
import org.joml.Matrix4f;

import java.util.function.Consumer;

public interface Window {
    void run();

    void run(ApplicationLoop applicationLoop);

    void stop();

    int getWidth();

    int getHeight();

    int getFPS();

    int getUPS();

    int getFPSCap();

    void setFPSCap(int cap);

    int getUPSCap();

    void setUPSCap(int cap);

    float dpi();

    boolean isVsync();

    void setVsync(boolean vsync);

    long getCurrentFrame();

    long getGlfwId();

    void addResizeCallback(Consumer<Window> callback);

    boolean isFullscreen();

    void setFullscreen(boolean fullscreen);

    Matrix4f getProjectionMatrix();

    String getTitle();

    void setTitle(String title);

    RenderAdapter getAdapter();

    Render getRender();

    Camera getCamera();

    GlfwClipboard getClipboard();

    BatchController getBatchController();

    double getDeltaTime();
}
