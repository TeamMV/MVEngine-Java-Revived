package dev.mv.engine.gui.style.value;

public class GuiValueJust<T> extends GuiValue<T> {
    protected T t;

    public GuiValueJust(T t) {
        this.t = t;
    }

    @Override
    public T resolve(ResolveContext context) {
        return t;
    }
}
