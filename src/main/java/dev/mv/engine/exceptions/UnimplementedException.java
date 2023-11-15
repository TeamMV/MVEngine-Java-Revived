package dev.mv.engine.exceptions;

public class UnimplementedException extends RuntimeException {
    public UnimplementedException() {
        super("This method isn't implemented yet!");
    }
}
