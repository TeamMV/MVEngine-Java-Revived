package dev.mv.engine.input;

import dev.mv.engine.render.shared.Window;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class InputCollector {
    private InputProcessor processor;

    private Window window;

    public InputCollector(Window window) {
        this.processor = InputProcessor.distributor();
        this.window = window;
    }

    public void start() {
        glfwSetInputMode(window.getGlfwId(), GLFW_LOCK_KEY_MODS, GLFW_TRUE);

        glfwSetScrollCallback(window.getGlfwId(), (win, xOffset, yOffset) -> {
            if (win != window.getGlfwId()) return;
            if (xOffset != 0.0) {
                processor.mouseScrollX((float) xOffset);
            }
            if (yOffset != 0.0) {
                processor.mouseScrollX((float) yOffset);
            }
        });

        glfwSetMouseButtonCallback(window.getGlfwId(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long win, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    processor.mousePress(Input.mouseFromGLFW(button));
                }
                if (action == GLFW_RELEASE) {
                    processor.mouseRelease(Input.mouseFromGLFW(button));
                }
            }
        });

        glfwSetCursorPosCallback(window.getGlfwId(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long wind, double x, double y) {
                processor.mouseMoveX((int) x);
                processor.mouseMoveY(window.getHeight() - (int) y);
            }
        });

        glfwSetKeyCallback(window.getGlfwId(), new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                    processor.keyPress(Input.keyFromGLFW(key));
                } else if (action == GLFW_RELEASE) {
                    processor.keyRelease(Input.keyFromGLFW(key));
                }
            }
        });

        glfwSetCharCallback(window.getGlfwId(), new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                processor.keyType((char) codepoint);
            }
        });
    }
}
