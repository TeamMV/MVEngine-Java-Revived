package dev.mv.engine.utils.generic.triplet;

public class Either<T, U, V> {

    Class<? extends T> t;
    Class<? extends U> u;
    Class<? extends V> v;
    Object value;
    Type which;

    public Either(Class<? extends T> t, Class<? extends U> u, Class<? extends V> v) {
        this.t = t;
        this.u = u;
        this.v = v;
    }

    public Either<T, U, V> assign(Object value) {
        if (t.isAssignableFrom(value.getClass())) {
            this.value = value;
            which = Type.T;
        } else if (u.isAssignableFrom(value.getClass())) {
            this.value = value;
            which = Type.U;
        } else if (v.isAssignableFrom(value.getClass())) {
            this.value = value;
            which = Type.V;
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
            return which == Type.T;
        } else if (clazz.isAssignableFrom(u)) {
            return which == Type.U;
        } else if (clazz.isAssignableFrom(v)) {
            return which == Type.V;
        }
        return false;
    }

    public <R> R value() {
        return (R) value;
    }

    public enum Type {
        T,
        U,
        V;
    }

}
