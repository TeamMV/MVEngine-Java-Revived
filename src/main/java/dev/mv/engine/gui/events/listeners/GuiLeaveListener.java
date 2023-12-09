package dev.mv.engine.gui.events.listeners;

import dev.mv.engine.gui.elements.GuiElement;

public interface GuiLeaveListener extends GuiEventListener{
    void leave(GuiElement caller, int mx, int my);
}
