package dev.mv.engine.input.processors;

import dev.mv.engine.gui.Gui;
import dev.mv.engine.input.InputProcessor;
import dev.mv.engine.utils.collection.Vec;

public class GuiInputProcessor implements InputProcessor {
    private boolean enabled = true;
    private static final Vec<Gui> guis = new Vec<>();

    private static final GuiInputProcessor instance = new GuiInputProcessor();

    public static int mouseX, mouseY;

    private GuiInputProcessor() {
        InputProcessor.distributor().serveProcessor(this);
    }

    public static GuiInputProcessor getInstance() {
        return instance;
    }

    public static void addGui(Gui gui) {
        guis.push(gui);
    }

    @Override
    public void keyPress(int key) {
        if (!enabled) return;
        guis.forEach(g -> g.keyPress(key));
    }

    @Override
    public void keyType(char key) {
        if (!enabled) return;
        guis.forEach(g -> g.keyType(key));
    }

    @Override
    public void keyRelease(int key) {
        if (!enabled) return;
        guis.forEach(g -> g.keyRelease(key));
    }

    @Override
    public void mousePress(int btn) {
        if (!enabled) return;
        guis.forEach(g -> g.mousePress(btn));
    }

    @Override
    public void mouseRelease(int btn) {
        if (!enabled) return;
        guis.forEach(g -> g.mouseRelease(btn));
    }

    @Override
    public void mouseMoveX(int x, int prev) {
        mouseX = x;
        if (!enabled) return;
        guis.forEach(g -> g.mouseMoveX(x, prev));
    }

    @Override
    public void mouseMoveY(int y, int prev) {
        mouseY = y;
        if (!enabled) return;
        guis.forEach(g -> g.mouseMoveY(y, prev));
    }

    @Override
    public void mouseScrollX(float value) {
        if (!enabled) return;
        guis.forEach(g -> g.mouseScrollX(value));
    }

    @Override
    public void mouseScrollY(float value) {
        if (!enabled) return;
        guis.forEach(g -> g.mouseScrollY(value));
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
