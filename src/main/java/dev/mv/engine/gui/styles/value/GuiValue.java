package dev.mv.engine.gui.styles.value;

public abstract class GuiValue<T> {
    public abstract T resolve(ResolveContext context);
}
