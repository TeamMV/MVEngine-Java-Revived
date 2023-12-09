package dev.mv.engine.gui.events.listeners;

import dev.mv.engine.gui.elements.GuiElement;

public interface GuiClickListener extends GuiEventListener{
    void click(GuiElement caller, int button, int x, int y);
}
