package dev.mv.engine.input;

import dev.mv.engine.render.shared.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Clipboard {
    private String contents = "";
    private Window window;

    public Clipboard(Window window) {
        this.window = window;
    }

    public boolean hasContents() {
        return !contents.isEmpty();
    }

    public void setContents(String contents) {
        this.contents = contents;
        glfwSetClipboardString(window.getGlfwId(), contents);
    }

    public String getContents() {
        contents = glfwGetClipboardString(window.getGlfwId());
        return contents;
    }
}
