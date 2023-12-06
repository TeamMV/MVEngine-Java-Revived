package dev.mv.engine.input;

import dev.mv.engine.utils.Toggle;

public interface InputProcessor extends Toggle {
    static InputProcessorDistributor distributor() {
        return InputProcessorDistributor.getInstance();
    }

    void keyPress(int key);
    void keyType(char key);
    void keyRelease(int key);

    void mousePress(int btn);
    void mouseRelease(int btn);
    void mouseMoveX(int x);
    void mouseMoveY(int y);
    void mouseScrollX(float value);
    void mouseScrollY(float value);
}