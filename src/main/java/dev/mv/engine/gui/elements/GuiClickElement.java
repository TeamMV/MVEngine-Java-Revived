package dev.mv.engine.gui.elements;

import dev.mv.engine.gui.events.listeners.GuiClickListener;
import dev.mv.engine.input.Input;
import dev.mv.engine.input.processors.GuiInputProcessor;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.utils.collection.Vec;

public abstract class GuiClickElement extends GuiElement {
    private boolean clickedInside;
    protected Vec<GuiClickListener> listeners;

    public GuiClickElement() {
        listeners = new Vec<>();

        event.click((caller, button, x, y) -> {
            if (button == Input.MOUSE_LEFT) {
                clickedInside = true;
            }
        });
        event.release((caller, button, x, y) -> {
            if (clickedInside) {
                if (button == Input.MOUSE_LEFT) {
                    listeners.forEach(l -> l.click(caller, button, x, y));
                }
            }
        });
    }

    @Override
    public void mouseRelease(int btn) {
        super.mouseRelease(btn);
        if (!inside(GuiInputProcessor.mouseX, GuiInputProcessor.mouseY)) {
            clickedInside = false;
        }
    }

    public void onclick(GuiClickListener listener) {
        listeners.push(listener);
    }

    public abstract void draw(DrawContext ctx);
}
