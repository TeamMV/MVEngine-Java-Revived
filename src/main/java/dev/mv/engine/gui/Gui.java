package dev.mv.engine.gui;

import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.input.InputProcessor;
import dev.mv.engine.render.shared.DrawContext;
import dev.mv.engine.utils.collection.Vec;

public class Gui implements InputProcessor {
    private boolean enabled = true;
    private Vec<GuiElement> elements;

    public Gui() {
        elements = new Vec<>();
    }

    public void addElement(GuiElement e) {
        elements.push(e);
    }

    public void draw(DrawContext ctx) {
        elements.forEach(e -> e.draw(ctx));
    }

    @Override
    public void keyPress(int key) {
        if (!enabled) return;
        elements.forEach(e -> e.keyPress(key));
    }

    @Override
    public void keyType(char key) {
        if (!enabled) return;
        elements.forEach(e -> e.keyType(key));
    }

    @Override
    public void keyRelease(int key) {
        if (!enabled) return;
        elements.forEach(e -> e.keyRelease(key));
    }

    @Override
    public void mousePress(int btn) {
        if (!enabled) return;
        elements.forEach(e -> e.mousePress(btn));
    }

    @Override
    public void mouseRelease(int btn) {
        if (!enabled) return;
        elements.forEach(e -> e.mouseRelease(btn));
    }

    @Override
    public void mouseMoveX(int x, int prev) {
        if (!enabled) return;
        elements.forEach(e -> e.mouseMoveX(x, prev));
    }

    @Override
    public void mouseMoveY(int y, int prev) {
        if (!enabled) return;
        elements.forEach(e -> e.mouseMoveY(y, prev));
    }

    @Override
    public void mouseScrollX(float value) {
        if (!enabled) return;
        elements.forEach(e -> e.mouseScrollX(value));
    }

    @Override
    public void mouseScrollY(float value) {
        if (!enabled) return;
        elements.forEach(e -> e.mouseScrollY(value));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
