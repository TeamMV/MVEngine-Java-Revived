package dev.mv.engine.resources;

import dev.mv.engine.utils.collection.Vec;

public class ResourceBundle {

    Vec<String> resources = new Vec<>();
    Vec<String> textures = new Vec<>();
    Vec<String> fonts = new Vec<>();
    Vec<String> sounds = new Vec<>();
    Vec<String> music = new Vec<>();

    public ResourceBundle() {}

    public ResourceBundle resources(String... ids) {
        resources = new Vec<>(ids);
        return this;
    }

    public ResourceBundle textures(String... ids) {
        textures = new Vec<>(ids);
        return this;
    }

    public ResourceBundle fonts(String... ids) {
        fonts = new Vec<>(ids);
        return this;
    }

    public ResourceBundle sounds(String... ids) {
        sounds = new Vec<>(ids);
        return this;
    }

    public ResourceBundle music(String... ids) {
        music = new Vec<>(ids);
        return this;
    }

}
