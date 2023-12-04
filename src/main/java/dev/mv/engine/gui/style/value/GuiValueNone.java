package dev.mv.engine.gui.style.value;

public class GuiValueNone<T> extends GuiValue<T> {
    public GuiValueNone() {
        isNone = true;
    }

    @Override
    public T resolve(ResolveContext context) {
        return null;
    }
}
