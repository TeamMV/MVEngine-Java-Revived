package dev.mv.engine.utils.math;

public abstract class ComplexNumber {

    protected ComplexNumber() {
    }

    public abstract String toString();

    public abstract String toHexString();

    public abstract boolean equals(Object obj);

    public abstract ComplexNumber clone();

}
