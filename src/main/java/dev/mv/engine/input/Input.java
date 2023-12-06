package dev.mv.engine.input;

import org.lwjgl.glfw.GLFW;

public class Input {
    // Numeric Keys
    public static final int KEY_0 = 26;
    public static final int KEY_1 = 17;
    public static final int KEY_2 = 18;
    public static final int KEY_3 = 19;
    public static final int KEY_4 = 20;
    public static final int KEY_5 = 21;
    public static final int KEY_6 = 22;
    public static final int KEY_7 = 23;
    public static final int KEY_8 = 24;
    public static final int KEY_9 = 25;

    // Alphabetic Keys
    public static final int KEY_A = 45;
    public static final int KEY_B = 62;
    public static final int KEY_C = 60;
    public static final int KEY_D = 47;
    public static final int KEY_E = 33;
    public static final int KEY_F = 48;
    public static final int KEY_G = 49;
    public static final int KEY_H = 50;
    public static final int KEY_I = 38;
    public static final int KEY_J = 51;
    public static final int KEY_K = 52;
    public static final int KEY_L = 53;
    public static final int KEY_M = 64;
    public static final int KEY_N = 63;
    public static final int KEY_O = 39;
    public static final int KEY_P = 40;
    public static final int KEY_Q = 31;
    public static final int KEY_R = 34;
    public static final int KEY_S = 46;
    public static final int KEY_T = 35;
    public static final int KEY_U = 37;
    public static final int KEY_V = 61;
    public static final int KEY_W = 32;
    public static final int KEY_X = 59;
    public static final int KEY_Y = 36;
    public static final int KEY_Z = 58;

    // Special Keys
    public static final int KEY_ESCAPE = 0;
    public static final int KEY_F1 = 1;
    public static final int KEY_F2 = 2;
    public static final int KEY_F3 = 3;
    public static final int KEY_F4 = 4;
    public static final int KEY_F5 = 5;
    public static final int KEY_F6 = 6;
    public static final int KEY_F7 = 7;
    public static final int KEY_F8 = 8;
    public static final int KEY_F9 = 9;
    public static final int KEY_F10 = 10;
    public static final int KEY_F11 = 11;
    public static final int KEY_F12 = 12;
    public static final int KEY_PRINT_SCREEN = 13;
    public static final int KEY_SCROLL_LOCK = 14;
    public static final int KEY_PAUSE = 15;
    public static final int KEY_GRAVE_ACCENT = 16;
    public static final int KEY_MINUS = 27;
    public static final int KEY_EQUALS = 28;
    public static final int KEY_BACKSPACE = 29;
    public static final int KEY_TAB = 30;
    public static final int KEY_LEFT_BRACKET = 41;
    public static final int KEY_RIGHT_BRACKET = 42;
    public static final int KEY_BACKSLASH = 43;
    public static final int KEY_CAPS_LOCK = 44;
    public static final int KEY_SEMICOLON = 54;
    public static final int KEY_APOSTROPHE = 55;
    public static final int KEY_ENTER = 56;
    public static final int KEY_LEFT_SHIFT = 57;
    public static final int KEY_COMMA = 65;
    public static final int KEY_PERIOD = 66;
    public static final int KEY_SLASH = 67;
    public static final int KEY_RIGHT_SHIFT = 68;
    public static final int KEY_LEFT_CTRL = 69;
    public static final int KEY_LEFT_ALT = 70;
    public static final int KEY_SPACE = 71;
    public static final int KEY_RIGHT_ALT = 72;
    public static final int KEY_RIGHT_CTRL = 73;
    public static final int KEY_LEFT_ARROW = 74;
    public static final int KEY_UP_ARROW = 75;
    public static final int KEY_DOWN_ARROW = 76;
    public static final int KEY_RIGHT_ARROW = 77;
    public static final int KEY_INSERT = 78;
    public static final int KEY_DELETE = 79;
    public static final int KEY_HOME = 80;
    public static final int KEY_END = 81;
    public static final int KEY_PAGE_UP = 82;
    public static final int KEY_PAGE_DOWN = 83;
    public static final int KEY_NUM_LOCK = 84;
    public static final int KEY_KP_DIVIDE = 85;
    public static final int KEY_KP_MULTIPLY = 86;
    public static final int KEY_KP_MINUS = 87;
    public static final int KEY_KP_PLUS = 88;
    public static final int KEY_KP_ENTER = 89;
    public static final int KEY_KP_1 = 90;
    public static final int KEY_KP_2 = 91;
    public static final int KEY_KP_3 = 92;
    public static final int KEY_KP_4 = 93;
    public static final int KEY_KP_5 = 94;
    public static final int KEY_KP_6 = 95;
    public static final int KEY_KP_7 = 96;
    public static final int KEY_KP_8 = 97;
    public static final int KEY_KP_9 = 98;
    public static final int KEY_KP_0 = 99;
    public static final int KEY_KP_PERIOD = 100;
    public static final int KEY_APPLICATION = 101;
    public static final int KEYS = 102;

    //Mouse
    public static final int MOUSE_LEFT = 0;
    public static final int MOUSE_RIGHT = 1;
    public static final int MOUSE_MIDDLE = 2;
    public static final int MOUSE_3 = 3;
    public static final int MOUSE_4 = 4;
    public static final int MOUSE_5 = 5;
    public static final int MOUSE_6 = 6;
    public static final int MOUSE_7 = 7;
    public static final int MOUSES = 8;

    //Scroll
    public static final int MOUSE_SCROLL_UP = 0;
    public static final int MOUSE_SCROLL_DOWN = 1;
    public static final int MOUSE_SCROLL_LEFT = 2;
    public static final int MOUSE_SCROLL_RIGHT = 3;
    public static final int SCROLLS = 4;

    public static boolean[] keys = new boolean[KEYS];
    public static InputState[] keyStates = new InputState[KEYS];
    public static boolean[] mouse = new boolean[KEYS];
    public static InputState[] mouseStates = new InputState[KEYS];
    public static boolean[] scroll = new boolean[KEYS];
    public static float[] scrollStates = new float[KEYS];

    public static int x, y;

    public static void update() {
        for (int i = 0; i < KEYS; i++) {
            if (keyStates[i] == InputState.JUST_PRESSED) keyStates[i] = InputState.PRESSED;
            if (keyStates[i] == InputState.JUST_RELEASED) keyStates[i] = InputState.RELEASED;
        }
        for (int i = 0; i < MOUSES; i++) {
            if (mouseStates[i] == InputState.JUST_PRESSED) mouseStates[i] = InputState.PRESSED;
            if (mouseStates[i] == InputState.JUST_RELEASED) mouseStates[i] = InputState.RELEASED;
        }
        for (int i = 0; i < SCROLLS; i++) {
            scroll[i] = false;
            scrollStates[i] = 0f;
        }
    }



    public static String stringFromMouse(int mouse) {
        return switch (mouse) {
            case MOUSE_LEFT -> "MOUSE_LEFT";
            case MOUSE_RIGHT -> "MOUSE_RIGHT";
            case MOUSE_MIDDLE -> "MOUSE_MIDDLE";
            case MOUSE_3 -> "MOUSE_3";
            case MOUSE_4 -> "MOUSE_4";
            case MOUSE_5 -> "MOUSE_5";
            case MOUSE_6 -> "MOUSE_6";
            case MOUSE_7 -> "MOUSE_7";
            default -> "UNKNOWN";
        };
    }

    public static int mouseFromString(String s) {
        return switch (s.toLowerCase()) {
            case "mouse_left" -> MOUSE_LEFT;
            case "mouse_right" -> MOUSE_RIGHT;
            case "mouse_middle" -> MOUSE_MIDDLE;
            case "mouse_3" -> MOUSE_3;
            case "mouse_4" -> MOUSE_4;
            case "mouse_5" -> MOUSE_5;
            case "mouse_6" -> MOUSE_6;
            case "mouse_7" -> MOUSE_7;
            default -> Integer.MAX_VALUE;
        };
    }

    public static String stringFromScroll(int scroll) {
        return switch (scroll) {
            case MOUSE_SCROLL_UP -> "MOUSE_SCROLL_UP";
            case MOUSE_SCROLL_DOWN -> "MOUSE_SCROLL_DOWN";
            case MOUSE_SCROLL_LEFT -> "MOUSE_SCROLL_LEFT";
            case MOUSE_SCROLL_RIGHT -> "MOUSE_SCROLL_RIGHT";
            default -> "UNKNOWN";
        };
    }

    public static int scrollFromString(String s) {
        return switch (s.toLowerCase()) {
            case "mouse_scroll_up" -> MOUSE_SCROLL_UP;
            case "mouse_scroll_down" -> MOUSE_SCROLL_DOWN;
            case "mouse_scroll_left" -> MOUSE_SCROLL_LEFT;
            case "mouse_scroll_right" -> MOUSE_SCROLL_RIGHT;
            default -> Integer.MAX_VALUE;
        };
    }

    static int mouseFromGLFW(int glfwMouseButton) {
        return switch (glfwMouseButton) {
            case 0 -> MOUSE_LEFT;
            case 1 -> MOUSE_RIGHT;
            case 2 -> MOUSE_MIDDLE;
            case 3 -> MOUSE_3;
            case 4 -> MOUSE_4;
            case 5 -> MOUSE_5;
            case 6 -> MOUSE_6;
            case 7 -> MOUSE_7;
            default -> Integer.MAX_VALUE;
        };
    }

    static int keyFromGLFW(int glfwKey) {
        return switch (glfwKey) {
            case GLFW.GLFW_KEY_ESCAPE -> KEY_ESCAPE;
            case GLFW.GLFW_KEY_F1 -> KEY_F1;
            case GLFW.GLFW_KEY_F2 -> KEY_F2;
            case GLFW.GLFW_KEY_F3 -> KEY_F3;
            case GLFW.GLFW_KEY_F4 -> KEY_F4;
            case GLFW.GLFW_KEY_F5 -> KEY_F5;
            case GLFW.GLFW_KEY_F6 -> KEY_F6;
            case GLFW.GLFW_KEY_F7 -> KEY_F7;
            case GLFW.GLFW_KEY_F8 -> KEY_F8;
            case GLFW.GLFW_KEY_F9 -> KEY_F9;
            case GLFW.GLFW_KEY_F10 -> KEY_F10;
            case GLFW.GLFW_KEY_F11 -> KEY_F11;
            case GLFW.GLFW_KEY_F12 -> KEY_F12;
            case GLFW.GLFW_KEY_PRINT_SCREEN -> KEY_PRINT_SCREEN;
            case GLFW.GLFW_KEY_SCROLL_LOCK -> KEY_SCROLL_LOCK;
            case GLFW.GLFW_KEY_PAUSE -> KEY_PAUSE;
            case GLFW.GLFW_KEY_GRAVE_ACCENT -> KEY_GRAVE_ACCENT;
            case GLFW.GLFW_KEY_1 -> KEY_1;
            case GLFW.GLFW_KEY_2 -> KEY_2;
            case GLFW.GLFW_KEY_3 -> KEY_3;
            case GLFW.GLFW_KEY_4 -> KEY_4;
            case GLFW.GLFW_KEY_5 -> KEY_5;
            case GLFW.GLFW_KEY_6 -> KEY_6;
            case GLFW.GLFW_KEY_7 -> KEY_7;
            case GLFW.GLFW_KEY_8 -> KEY_8;
            case GLFW.GLFW_KEY_9 -> KEY_9;
            case GLFW.GLFW_KEY_0 -> KEY_0;
            case GLFW.GLFW_KEY_MINUS -> KEY_MINUS;
            case GLFW.GLFW_KEY_EQUAL -> KEY_EQUALS;
            case GLFW.GLFW_KEY_BACKSPACE -> KEY_BACKSPACE;
            case GLFW.GLFW_KEY_TAB -> KEY_TAB;
            case GLFW.GLFW_KEY_Q -> KEY_Q;
            case GLFW.GLFW_KEY_W -> KEY_W;
            case GLFW.GLFW_KEY_E -> KEY_E;
            case GLFW.GLFW_KEY_R -> KEY_R;
            case GLFW.GLFW_KEY_T -> KEY_T;
            case GLFW.GLFW_KEY_Y -> KEY_Y;
            case GLFW.GLFW_KEY_U -> KEY_U;
            case GLFW.GLFW_KEY_I -> KEY_I;
            case GLFW.GLFW_KEY_O -> KEY_O;
            case GLFW.GLFW_KEY_P -> KEY_P;
            case GLFW.GLFW_KEY_LEFT_BRACKET -> KEY_LEFT_BRACKET;
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> KEY_RIGHT_BRACKET;
            case GLFW.GLFW_KEY_BACKSLASH -> KEY_BACKSLASH;
            case GLFW.GLFW_KEY_CAPS_LOCK -> KEY_CAPS_LOCK;
            case GLFW.GLFW_KEY_A -> KEY_A;
            case GLFW.GLFW_KEY_S -> KEY_S;
            case GLFW.GLFW_KEY_D -> KEY_D;
            case GLFW.GLFW_KEY_F -> KEY_F;
            case GLFW.GLFW_KEY_G -> KEY_G;
            case GLFW.GLFW_KEY_H -> KEY_H;
            case GLFW.GLFW_KEY_J -> KEY_J;
            case GLFW.GLFW_KEY_K -> KEY_K;
            case GLFW.GLFW_KEY_L -> KEY_L;
            case GLFW.GLFW_KEY_SEMICOLON -> KEY_SEMICOLON;
            case GLFW.GLFW_KEY_APOSTROPHE -> KEY_APOSTROPHE;
            case GLFW.GLFW_KEY_ENTER -> KEY_ENTER;
            case GLFW.GLFW_KEY_LEFT_SHIFT -> KEY_LEFT_SHIFT;
            case GLFW.GLFW_KEY_Z -> KEY_Z;
            case GLFW.GLFW_KEY_X -> KEY_X;
            case GLFW.GLFW_KEY_C -> KEY_C;
            case GLFW.GLFW_KEY_V -> KEY_V;
            case GLFW.GLFW_KEY_B -> KEY_B;
            case GLFW.GLFW_KEY_N -> KEY_N;
            case GLFW.GLFW_KEY_M -> KEY_M;
            case GLFW.GLFW_KEY_COMMA -> KEY_COMMA;
            case GLFW.GLFW_KEY_PERIOD -> KEY_PERIOD;
            case GLFW.GLFW_KEY_SLASH -> KEY_SLASH;
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> KEY_RIGHT_SHIFT;
            case GLFW.GLFW_KEY_LEFT_CONTROL -> KEY_LEFT_CTRL;
            case GLFW.GLFW_KEY_LEFT_ALT -> KEY_LEFT_ALT;
            case GLFW.GLFW_KEY_SPACE -> KEY_SPACE;
            case GLFW.GLFW_KEY_RIGHT_ALT -> KEY_RIGHT_ALT;
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> KEY_RIGHT_CTRL;
            case GLFW.GLFW_KEY_LEFT -> KEY_LEFT_ARROW;
            case GLFW.GLFW_KEY_UP -> KEY_UP_ARROW;
            case GLFW.GLFW_KEY_DOWN -> KEY_DOWN_ARROW;
            case GLFW.GLFW_KEY_RIGHT -> KEY_RIGHT_ARROW;
            case GLFW.GLFW_KEY_INSERT -> KEY_INSERT;
            case GLFW.GLFW_KEY_DELETE -> KEY_DELETE;
            case GLFW.GLFW_KEY_HOME -> KEY_HOME;
            case GLFW.GLFW_KEY_END -> KEY_END;
            case GLFW.GLFW_KEY_PAGE_UP -> KEY_PAGE_UP;
            case GLFW.GLFW_KEY_PAGE_DOWN -> KEY_PAGE_DOWN;
            case GLFW.GLFW_KEY_NUM_LOCK -> KEY_NUM_LOCK;
            case GLFW.GLFW_KEY_KP_DIVIDE -> KEY_KP_DIVIDE;
            case GLFW.GLFW_KEY_KP_MULTIPLY -> KEY_KP_MULTIPLY;
            case GLFW.GLFW_KEY_KP_SUBTRACT -> KEY_KP_MINUS;
            case GLFW.GLFW_KEY_KP_ADD -> KEY_KP_PLUS;
            case GLFW.GLFW_KEY_KP_ENTER -> KEY_KP_ENTER;
            case GLFW.GLFW_KEY_KP_1 -> KEY_KP_1;
            case GLFW.GLFW_KEY_KP_2 -> KEY_KP_2;
            case GLFW.GLFW_KEY_KP_3 -> KEY_KP_3;
            case GLFW.GLFW_KEY_KP_4 -> KEY_KP_4;
            case GLFW.GLFW_KEY_KP_5 -> KEY_KP_5;
            case GLFW.GLFW_KEY_KP_6 -> KEY_KP_6;
            case GLFW.GLFW_KEY_KP_7 -> KEY_KP_7;
            case GLFW.GLFW_KEY_KP_8 -> KEY_KP_8;
            case GLFW.GLFW_KEY_KP_9 -> KEY_KP_9;
            case GLFW.GLFW_KEY_KP_0 -> KEY_KP_0;
            case GLFW.GLFW_KEY_KP_DECIMAL -> KEY_KP_PERIOD;
            case GLFW.GLFW_KEY_MENU -> KEY_APPLICATION;
            default -> Integer.MAX_VALUE;
        };
    }
}
