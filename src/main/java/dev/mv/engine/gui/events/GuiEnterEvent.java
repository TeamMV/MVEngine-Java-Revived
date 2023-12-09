package dev.mv.engine.gui.events;

import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.events.listeners.GuiEnterListener;

public class GuiEnterEvent extends GuiEvent<GuiEnterListener> {
    private boolean inside;

    public void call(GuiElement caller, int x, int y) {
        if (caller.inside(x, y) && !inside) {
            inside = true;
            for (GuiEnterListener listener : listeners) {
                listener.enter(caller, x, y);
            }
        }
        if (!caller.inside(x, y)) {
            inside = false;
        }
    }
}
