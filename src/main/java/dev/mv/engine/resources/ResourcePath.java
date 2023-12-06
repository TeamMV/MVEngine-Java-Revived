package dev.mv.engine.resources;

import dev.mv.engine.MVEngine;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.files.Directory;
import dev.mv.engine.game.Game;
import dev.mv.engine.game.mod.loader.ModIntegration;
import dev.mv.engine.utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourcePath {

    private Type type;
    private String path;

    private ResourcePath(Type type, String path) {
        this.type = type;
        this.path = path;
    }

    public InputStream getInputStream() {
        return switch (type) {
            case INTERNAL -> ModIntegration.getResourceAsStream(path);
            case EXTERNAL -> {
                try {
                    yield new FileInputStream(path);
                } catch (FileNotFoundException e) {
                    Exceptions.send(e);
                    yield null;
                }
            }
            case GAME_DIR -> {
                Game game = MVEngine.instance().getGame();
                if (game == null) {
                    Exceptions.send(new NullPointerException("Game is null!"));
                }
                Directory dir = game.getGameDirectory();
                if (dir == null) {
                    Exceptions.send(new IllegalStateException("Game not initialized!"));
                }
                String dirPath = dir.getAbsolutePath();
                try {
                    yield new FileInputStream(Utils.getPath(dirPath, path));
                } catch (FileNotFoundException e) {
                    Exceptions.send(e);
                    yield null;
                }
            }
            case BLANK -> Utils.streamString(path);
        };
    }

    public String getPath() {
        return path;
    }

    boolean isBlank() {
        return type == Type.BLANK;
    }

    public static ResourcePath internal(String path) {
        return new ResourcePath(Type.INTERNAL, path);
    }

    public static ResourcePath external(String path) {
        return new ResourcePath(Type.EXTERNAL, path);
    }

    public static ResourcePath gameDir(String path) {
        return new ResourcePath(Type.GAME_DIR, path);
    }

    static ResourcePath blank(String path) {
        return new ResourcePath(Type.BLANK, path);
    }

    private enum Type {
        INTERNAL,
        EXTERNAL,
        GAME_DIR,
        BLANK
    }

}
