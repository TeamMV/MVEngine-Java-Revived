package dev.mv.engine.gui.events;

import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.events.listeners.GuiClickListener;

public class GuiClickEvent extends GuiEvent<GuiClickListener>{
    public void call(GuiElement caller, int btn, int x, int y) {
        if (caller.inside(x, y)) {
            for (GuiClickListener listener : listeners) {
                listener.click(caller, btn, x, y);
            }
        }
    }
}
