package dev.mv.engine.gui.style.value;

import dev.mv.engine.gui.elements.GuiElement;

public class ResolveContext {
    public float dpi;
    public GuiElement parent;

    public ResolveContext() {
        this.dpi = 1f;
        this.parent = null;
    }
}
