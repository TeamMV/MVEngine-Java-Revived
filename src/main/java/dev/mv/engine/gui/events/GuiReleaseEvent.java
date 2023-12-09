package dev.mv.engine.gui.events;

import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.events.listeners.GuiReleaseListener;

public class GuiReleaseEvent extends GuiEvent<GuiReleaseListener> {
    public void call(GuiElement caller, int btn, int x, int y) {
        if (caller.inside(x, y)) {
            for (GuiReleaseListener listener : listeners) {
                listener.release(caller, btn, x, y);
            }
        }
    }
}
