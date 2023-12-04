package dev.mv.engine.utils.nullHandler;

/**
 * Generic return value that can be used after {@link NullHandler} sets a return value.
 */
public class NullHandlerReturn {

    private Object value;

    public NullHandlerReturn(Object value) {
        this.value = value;
    }

    /**
     * Get the value cast to a type.
     *
     * @return the value as it's cast type.
     */
    public <T> T value() {
        return (T) value;
    }

    /**
     * Get the value as an {@link Object}.
     *
     * @return the value as an {@link Object} type.
     */
    public Object any() {
        return value;
    }

}
