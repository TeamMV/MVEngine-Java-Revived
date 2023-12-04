package dev.mv.engine.resources;

import dev.mv.engine.utils.logger.Logger;

public interface ProgressAction {
    void loading(String resId);
    void failed(String resId);
    void loaded(int total, int current, float percentage, String resId);

    static ProgressAction simple() {
        return new ProgressAction() {
            @Override
            public void loading(String resId) {
                Logger.info("loading resource \""+resId+"\"...");
            }

            @Override
            public void failed(String resId) {
                Logger.warn("failed to load resource \""+resId+"\"!");
            }

            @Override
            public void loaded(int total, int current, float percentage, String resId) {
                Logger.info(String.format("[%f%% - %d/%d] loaded resource \"%s\".", percentage, current, total, resId));
            }
        };
    }

    static ProgressAction quiet() {
        return new ProgressAction() {
            @Override
            public void loading(String resId) {}

            @Override
            public void failed(String resId) {}

            @Override
            public void loaded(int total, int current, float percentage, String resId) {}
        };
    }
}
