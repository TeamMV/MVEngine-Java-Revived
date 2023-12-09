package dev.mv.engine.gui.events.listeners;

import dev.mv.engine.gui.elements.GuiElement;

public interface GuiEnterListener extends GuiEventListener{
    void enter(GuiElement caller,  int mx, int my);
}
