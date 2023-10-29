package dev.mv.engine.gui.styles.value;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.utils.Utils;

public class GuiValuePercentage<T> extends GuiValue<T> {
    private T value;
    private float percentage;

    public GuiValuePercentage(T value, float percentage) {
        this.value = value;
        this.percentage = percentage;
    }

    @Override
    public T resolve(ResolveContext context) {
        try {
            return (T) Float.valueOf(Utils.getValue(percentage, (int) value));
        } catch (ClassCastException e) {
            Exceptions.send("GuiValuePercentage can only be used on Number values.");
            return null;
        }
    }
}
