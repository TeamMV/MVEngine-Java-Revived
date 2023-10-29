package dev.mv.engine.gui.styles.value;

import dev.mv.engine.gui.GuiElement;

public class ResolveContext {
    public int dpi;
    public GuiElement parent;

    public ResolveContext() {
        this.dpi = 1;
        this.parent = null;
    }
}
