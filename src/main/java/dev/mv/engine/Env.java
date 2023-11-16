package dev.mv.engine;

public class Env {
    private static boolean RESOURCE_READY = false;

    public static boolean isResourceReady() {
        return RESOURCE_READY;
    }

    static void setResourceReady() {
        RESOURCE_READY = true;
    }
}
