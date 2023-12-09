package dev.mv.engine.input;

import dev.mv.engine.exceptions.Exceptions;

import java.util.ArrayList;
import java.util.List;

public class InputProcessorDistributor implements InputProcessor {
    private static final InputProcessorDistributor instance = new InputProcessorDistributor();
    private boolean enabled = true;
    private List<InputProcessor> processors;

    static InputProcessorDistributor getInstance() {
        return instance;
    }

    private InputProcessorDistributor() {
        processors = new ArrayList<>();
    }

    public void serveProcessor(InputProcessor processor) {
        if (processor != null) {
            processors.add(processor);
        }
    }

    public void dropProcessor(InputProcessor processor) {
        processors.remove(processor);
    }

    @Override
    public void keyPress(int key) {
        processors.forEach(p -> p.keyPress(key));
    }

    @Override
    public void keyType(char key) {
        processors.forEach(p -> p.keyType(key));
    }

    @Override
    public void keyRelease(int key) {
        processors.forEach(p -> p.keyRelease(key));
    }

    @Override
    public void mousePress(int btn) {
        processors.forEach(p -> p.mousePress(btn));
    }

    @Override
    public void mouseRelease(int btn) {
        processors.forEach(p -> p.mouseRelease(btn));
    }

    @Override
    public void mouseMoveX(int x, int prev) {
        processors.forEach(p -> p.mouseMoveX(x, prev));
    }

    @Override
    public void mouseMoveY(int y, int prev) {
        processors.forEach(p -> p.mouseMoveY(y, prev));
    }

    @Override
    public void mouseScrollX(float value) {
        processors.forEach(p -> p.mouseScrollX(value));
    }

    @Override
    public void mouseScrollY(float value) {
        processors.forEach(p -> p.mouseScrollY(value));
    }

    @Override
    public void setEnabled(boolean enabled) {
        Exceptions.send(new IllegalCallerException("Cannot enable/disable the InputProcessorDistributor!"));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
