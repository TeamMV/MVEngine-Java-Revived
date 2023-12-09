package dev.mv.engine.gui.events;

import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.events.listeners.GuiEnterListener;
import dev.mv.engine.gui.events.listeners.GuiLeaveListener;

public class GuiLeaveEvent extends GuiEvent<GuiLeaveListener>{
    private boolean inside;

    public void call(GuiElement caller, int x, int y) {
        if (!caller.inside(x, y) && inside) {
            inside = false;
            for (GuiLeaveListener listener : listeners) {
                listener.leave(caller, x, y);
            }
        }
        if (caller.inside(x, y)) {
            inside = true;
        }
    }
}
