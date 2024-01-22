package dev.mv.engine.input.processors;

import dev.mv.engine.input.Input;
import dev.mv.engine.input.InputProcessor;
import dev.mv.engine.input.InputState;

public class MainInputProcessor implements InputProcessor {
    private static final MainInputProcessor instance = new MainInputProcessor();
    private boolean enabled = true;

    public static MainInputProcessor getInstance() {
        return instance;
    }

    private MainInputProcessor() {
        InputProcessor.distributor().serveProcessor(this);
    }

    @Override
    public void keyPress(int key) {
        if (!enabled) return;
        if (key < Input.KEYS && key > 0) {
            Input.keys[key] = true;
            Input.keyStates[key] = InputState.JUST_PRESSED;
        }
    }

    @Override
    public void keyType(char key) {
        //not necessary, cuz handled by GuiInputProcessor
    }

    @Override
    public void keyRelease(int key) {
        if (!enabled) return;
        if (key < Input.KEYS && key > 0) {
            Input.keys[key] = false;
            Input.keyStates[key] = InputState.JUST_RELEASED;
        }
    }

    @Override
    public void mousePress(int btn) {
        if (!enabled) return;
        Input.mouse[btn] = true;
        Input.mouseStates[btn] = InputState.JUST_PRESSED;
    }

    @Override
    public void mouseRelease(int btn) {
        if (!enabled) return;
        Input.mouse[btn] = false;
        Input.mouseStates[btn] = InputState.JUST_RELEASED;
    }

    @Override
    public void mouseMoveX(int x, int prev) {
        if (!enabled) return;
        Input.x = x;
    }

    @Override
    public void mouseMoveY(int y, int prev) {
        if (!enabled) return;
        Input.y = y;
    }

    @Override
    public void mouseScrollX(float value) {
        if (!enabled) return;
        if (value < 0f) {
            Input.scroll[Input.MOUSE_SCROLL_LEFT] = true;
            Input.scrollStates[Input.MOUSE_SCROLL_LEFT] = value;
        } else if (value > 0f) {
            Input.scroll[Input.MOUSE_SCROLL_RIGHT] = true;
            Input.scrollStates[Input.MOUSE_SCROLL_RIGHT] = value;
        }
    }

    @Override
    public void mouseScrollY(float value) {
        if (!enabled) return;
        if (value < 0f) {
            Input.scroll[Input.MOUSE_SCROLL_DOWN] = true;
            Input.scrollStates[Input.MOUSE_SCROLL_DOWN] = value;
        } else if (value > 0f) {
            Input.scroll[Input.MOUSE_SCROLL_UP] = true;
            Input.scrollStates[Input.MOUSE_SCROLL_UP] = value;
        }
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
