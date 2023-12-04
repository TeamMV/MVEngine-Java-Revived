package dev.mv.engine.gui.style.value;

public class GuiValueAuto<T> extends GuiValue<T> {
    public GuiValueAuto() {
        isAuto = true;
    }

    @Override
    public T resolve(ResolveContext context) {
        return null;
    }
}
