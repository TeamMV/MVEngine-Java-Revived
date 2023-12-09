package dev.mv.engine.gui.events;

import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.events.listeners.GuiEventListener;
import dev.mv.engine.utils.collection.Vec;

import java.util.function.Consumer;

public abstract class GuiEvent<L extends GuiEventListener> {
    protected Vec<L> listeners;

    public GuiEvent() {
        listeners = new Vec<>();
    }

    public void appendListener(L listener) {
        listeners.push(listener);
    }

    public void removeListener(L listener) {
        listeners.remove(listener);
    }
}
