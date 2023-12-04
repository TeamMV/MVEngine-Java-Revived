package dev.mv.engine.utils.generic.pair;

public class Either<T, U> {

    Class<? extends T> t;
    Class<? extends U> u;
    Object value;
    boolean isT = false;

    public Either(Class<? extends T> t, Class<? extends U> u) {
        this.t = t;
        this.u = u;
    }

    public Either<T, U> assign(Object value) {
        if (t.isAssignableFrom(value.getClass())) {
            this.value = value;
            isT = true;
        } else if (u.isAssignableFrom(value.getClass())) {
            this.value = value;
            isT = false;
        } else {
            throw new IllegalArgumentException("Value is not one of the possible classes");
        }
        return this;
    }

    public boolean isAssigned() {
        return value != null;
    }

    public boolean is(Class<?> clazz) {
        if (value == null) return false;
        if (clazz.isAssignableFrom(t)) {
            return isT;
        } else if (clazz.isAssignableFrom(u)) {
            return !isT;
        }
        return false;
    }

    public <R> R value() {
        return (R) value;
    }

}