package dev.mv.engine.async;

import dev.mv.engine.exceptions.Exceptions;

public class Signal {
    public void wake() {
        try {
             notifyAll();
        } catch (Exception e) {
            Exceptions.send(e);
        }
    }
}
