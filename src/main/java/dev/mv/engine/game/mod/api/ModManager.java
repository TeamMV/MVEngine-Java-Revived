package dev.mv.engine.game.mod.api;

import dev.mv.engine.files.Directory;
import dev.mv.engine.game.event.Event;
import dev.mv.engine.resources.ResourceLoader;

public interface ModManager {

    void registerListener(Object instance);

    void dispatchEvent(Event event);

    Directory getGameDirectory();

    Directory getModConfigDirectory();

    ResourceLoader getResourceLoader();

}
