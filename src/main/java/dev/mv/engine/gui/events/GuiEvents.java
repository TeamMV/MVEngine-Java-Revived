package dev.mv.engine.gui.events;

import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.events.listeners.GuiClickListener;
import dev.mv.engine.gui.events.listeners.GuiEnterListener;
import dev.mv.engine.gui.events.listeners.GuiLeaveListener;
import dev.mv.engine.gui.events.listeners.GuiReleaseListener;
import dev.mv.engine.input.InputProcessor;

public class GuiEvents implements InputProcessor {
    private final GuiElement element;
    private int px, py;

    private final GuiEnterEvent enter;
    private final GuiLeaveEvent leave;
    private final GuiClickEvent click;
    private final GuiReleaseEvent release;

    public GuiEvents(GuiElement element) {
        this.element = element;

        enter = new GuiEnterEvent();
        leave = new GuiLeaveEvent();
        click = new GuiClickEvent();
        release = new GuiReleaseEvent();
    }

    public void enter(GuiEnterListener enterListener) {
        enter.appendListener(enterListener);
    }

    public GuiEnterEvent enter() {
        return enter;
    }

    public void leave(GuiLeaveListener leaveListener) {
        leave.appendListener(leaveListener);
    }

    public GuiLeaveEvent levae() {
        return leave;
    }

    public void click(GuiClickListener clickListener) {
        click.appendListener(clickListener);
    }

    public GuiClickEvent click() {
        return click;
    }

    public void release(GuiReleaseListener releaseListener) {
        release.appendListener(releaseListener);
    }

    public GuiReleaseEvent release() {
        return release;
    }

    @Override
    public void keyPress(int key) {

    }

    @Override
    public void keyType(char key) {

    }

    @Override
    public void keyRelease(int key) {

    }

    @Override
    public void mousePress(int btn) {
        click.call(element, btn, px, py);
    }

    @Override
    public void mouseRelease(int btn) {
        release.call(element, btn, px, py);
    }

    @Override
    public void mouseMoveX(int x, int prev) {
        px = x;
        enter.call(element, x, py);
        leave.call(element, x, py);
    }

    @Override
    public void mouseMoveY(int y, int prev) {
        py = y;
        enter.call(element, px, y);
        leave.call(element, px, y);
    }

    @Override
    public void mouseScrollX(float value) {

    }

    @Override
    public void mouseScrollY(float value) {

    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
