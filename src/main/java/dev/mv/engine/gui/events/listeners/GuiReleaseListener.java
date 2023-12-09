package dev.mv.engine.gui.events.listeners;

import dev.mv.engine.gui.elements.GuiElement;

public interface GuiReleaseListener extends GuiEventListener{
    void release(GuiElement caller, int button, int x, int y);
}
