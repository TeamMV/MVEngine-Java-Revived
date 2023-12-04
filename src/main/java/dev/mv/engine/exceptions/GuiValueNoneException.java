package dev.mv.engine.exceptions;

public class GuiValueNoneException extends Exception{
    public GuiValueNoneException() {
    }

    public GuiValueNoneException(String message) {
        super(message);
    }

    public GuiValueNoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuiValueNoneException(Throwable cause) {
        super(cause);
    }

    public GuiValueNoneException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
