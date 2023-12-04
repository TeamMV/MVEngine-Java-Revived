package dev.mv.engine.utils.math;

import org.jetbrains.annotations.NotNull;

import static dev.mv.engine.utils.math.ComplexMath.*;

public class Real extends ComplexNumber implements Comparable<Real> {

    double val;

    public Real(int val) {
        this.val = val;
    }

    public Real(long val) {
        this.val = val;
    }

    public Real(float val) {
        this.val = val;
    }

    public Real(double val) {
        this.val = val;
    }

    public static int compare(Real a, Real b) {
        return Double.compare(a.val, b.val);
    }

    public static Real valueOf(String value) {
        return new Real(Double.parseDouble(value));
    }

    public double value() {
        return val;
    }

    public Real add(Real b) {
        return re(val + b.val);
    }

    public Complex add(Imaginary b) {
        return cm(val, b.val);
    }

    public Complex add(Complex b) {
        return cm(val + b.re, b.im);
    }

    public Real sub(Real b) {
        return re(val - b.val);
    }

    public Complex sub(Imaginary b) {
        return cm(val, -b.val);
    }

    public Complex sub(Complex b) {
        return cm(val - b.re, b.im);
    }

    public Real mul(Real b) {
        return re(val * b.val);
    }

    public Imaginary mul(Imaginary b) {
        return im(val * b.val);
    }

    public Complex mul(Complex b) {
        return cm(val * b.re, val * b.im);
    }

    public Real div(Real b) {
        return re(val / b.val);
    }

    public Imaginary div(Imaginary b) {
        return im(-val / b.val);
    }

    public Complex div(Complex b) {
        double re = val / (b.re + (b.im * b.im) / b.re);
        double im = re * (b.im / b.re) * -1;
        return cm(re, im);
    }

    public Real abs() {
        return re(Math.abs(val));
    }

    public ComplexNumber sqrt() {
        if (val == 0) return re(0);
        if (val < 0) {
            return im(Math.sqrt(Math.abs(val)));
        }
        return re(Math.sqrt(val));
    }

    public Real pow(Real b) {
        return re(Math.pow(val, b.val));
    }

    public Complex pow(Imaginary b) {
        return im(b.val * Math.log(val)).exp();
    }

    public Complex pow(Complex b) {
        return pow(re(b.re)).mul(pow(im(b.im)));
    }

    public Real exp() {
        return re(Math.exp(val));
    }

    public String toString() {
        return Double.toString(val);
    }

    public String toHexString() {
        return Double.toHexString(val);
    }

    @Override
    public int compareTo(@NotNull Real o) {
        return Double.compare(val, o.val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Real)) return false;
        Real other = (Real) obj;
        return other.val == this.val;
    }

    @Override
    public Real clone() {
        return new Real(val);
    }

}
