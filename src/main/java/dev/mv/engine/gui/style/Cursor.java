package dev.mv.engine.gui.style;

import static org.lwjgl.glfw.GLFW.*;

public enum Cursor {
    ARROW,
    IBEAM,
    CROSSHAIR,
    HAND,
    HRESIZE,
    VRESIZE,
    NS_RESIZE,
    EW_RESIZE,
    MOVE;

    public long getGLFW() {
        return switch (this) {
            case ARROW -> glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
            case IBEAM -> glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
            case CROSSHAIR -> glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR);
            case HAND -> glfwCreateStandardCursor(GLFW_HAND_CURSOR);
            case HRESIZE, EW_RESIZE -> glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
            case VRESIZE, NS_RESIZE -> glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
            case MOVE -> glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        };
    }
}
