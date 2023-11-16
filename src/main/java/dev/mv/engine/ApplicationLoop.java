package dev.mv.engine;

import dev.mv.engine.render.shared.Window;
import dev.mv.engine.resources.ProgressAction;

public interface ApplicationLoop {
    ProgressAction preload(MVEngine engine, Window window) throws Exception;

    void start(MVEngine engine, Window window) throws Exception;

    void update(MVEngine engine, Window window) throws Exception;

    void draw(MVEngine engine, Window window) throws Exception;

    void exit(MVEngine engine, Window window) throws Exception;
}
