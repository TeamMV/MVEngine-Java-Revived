package dev.mv.engine.gui.style.value;

public abstract class GuiValue<T> {
    protected boolean isNone = false;
    protected boolean isAuto = false;

    public boolean isNone() {
        return isNone;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public abstract T resolve(ResolveContext context);
}
