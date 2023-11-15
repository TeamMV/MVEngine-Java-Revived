package dev.mv.engine.resources;

public interface ProgressAction {
    void loading(String resId);
    void failed(String resId);
    void loaded(int total, int current, float percentage, String resId);
}
