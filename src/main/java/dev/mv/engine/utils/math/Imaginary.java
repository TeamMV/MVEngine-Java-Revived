package dev.mv.engine.utils.math;

import org.jetbrains.annotations.NotNull;

import static dev.mv.engine.utils.math.ComplexMath.*;

public class Imaginary extends ComplexNumber implements Comparable<Imaginary> {

    double val;

    public Imaginary(int val) {
        this.val = val;
    }

    public Imaginary(long val) {
        this.val = val;
    }

    public Imaginary(float val) {
        this.val = val;
    }

    public Imaginary(double val) {
        this.val = val;
    }

    public static Imaginary valueOf(String value) {
        return new Imaginary(Double.parseDouble(value.replace("i", "")));
    }

    public static int compare(Imaginary a, Imaginary b) {
        return Double.compare(a.val, b.val);
    }

    public double value() {
        return val;
    }

    public Imaginary add(Imaginary b) {
        return im(val + b.val);
    }

    public Complex add(Real b) {
        return cm(b.val, val);
    }

    public Complex add(Complex b) {
        return cm(b.re, val + b.im);
    }

    public Imaginary sub(Imaginary b) {
        return im(val - b.val);
    }

    public Complex sub(Real b) {
        return cm(-b.val, val);
    }

    public Complex sub(Complex b) {
        return cm(-b.re, val - b.im);
    }

    public Real mul(Imaginary b) {
        return re(val * b.val * -1);
    }

    public Imaginary mul(Real b) {
        return im(val * b.val);
    }

    public Complex mul(Complex b) {
        return cm(b.im * val * -1, b.re * val);
    }

    public Imaginary div(Real b) {
        return im(val / b.val);
    }

    public Real div(Imaginary b) {
        return re(val / b.val);
    }

    public Complex div(Complex b) {
        double im = val / (b.re + (b.im * b.im) / b.re);
        double re = im * (b.im / b.re);
        return cm(re, im);
    }

    public Real abs() {
        return re(Math.abs(val));
    }

    public Complex sqrt() {
        if (val == 0) return cm(0, 0);
        double root = Math.sqrt(val);
        Complex complex = cm(R2_2.val * root, R2_2.val * root);
        if (val < 0) complex.re *= -1;
        return complex;
    }

    public Complex exp() {
        return cm(Math.cos(val), Math.sin(val));
    }

    public Complex pow(Real b) {
        return re(val).pow(b).mul(im((PI.val * b.val) / 2).exp());
    }

    public Complex pow(Imaginary b) {
        return re(val).pow(b).mul(re((-b.val * PI.val) / 2).exp());
    }

    public Complex pow(Complex b) {
        return re(val).pow(re(b.re)).mul(pow(im(b.im)));
    }

    public String toString() {
        return val + "i";
    }

    public String toHexString() {
        return Double.toHexString(val) + "i";
    }

    @Override
    public int compareTo(@NotNull Imaginary o) {
        return Double.compare(val, o.val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Imaginary)) return false;
        Imaginary other = (Imaginary) obj;
        return other.val == this.val;
    }

    @Override
    public Imaginary clone() {
        return new Imaginary(val);
    }

}
