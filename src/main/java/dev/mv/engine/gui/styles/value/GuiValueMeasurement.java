package dev.mv.engine.gui.styles.value;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.logic.unit.Unit;

public class GuiValueMeasurement<T extends Number> extends GuiValue<T> {
    private float value;
    private Unit unit;

    @Override
    public T resolve(ResolveContext context) {
        try {
            return (T) Integer.valueOf(unit.intoPx(value, context.dpi));
        } catch (ClassCastException e) {
            Exceptions.send("GuiValueMeasurement can only be used on Number values.");
            return null;
        }
    }
}
