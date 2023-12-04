package dev.mv.engine.utils.math;

import org.jetbrains.annotations.NotNull;

import static dev.mv.engine.utils.math.ComplexMath.*;

public class Complex extends ComplexNumber implements Comparable<Complex> {

    double re, im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex(Real re, Imaginary im) {
        this.re = re.val;
        this.im = im.val;
    }

    public Complex(double re) {
        this.re = re;
        this.im = 0;
    }

    public Complex(Imaginary im) {
        this.re = 0;
        this.im = im.val;
    }

    public Complex(Real re) {
        this.re = re.val;
        this.im = 0;
    }

    public static Complex valueOf(String s) {
        String[] parts = s.replaceAll(" ", "").split("\\+");
        if (parts.length == 1) {
            if (parts[0].contains("i")) {
                return cm(0, Double.parseDouble(parts[0].replaceAll("i", "")));
            }
            return cm(Double.parseDouble(parts[0]), 0);
        }
        String re = parts[0].contains("i") ? parts[1] : parts[0];
        String im = parts[1].contains("i") ? parts[0] : parts[1];
        return cm(Double.parseDouble(re), Double.parseDouble(im.replaceAll("i", "")));
    }

    public double realValue() {
        return re;
    }

    public double imaginaryValue() {
        return im;
    }

    public Complex add(Complex b) {
        return cm(re + b.re, im + b.im);
    }

    public Complex add(Real b) {
        return cm(re + b.val, im);
    }

    public Complex add(Imaginary b) {
        return cm(re, im + b.val);
    }

    public Complex sub(Complex b) {
        return cm(re - b.re, im - b.im);
    }

    public Complex sub(Real b) {
        return cm(re - b.val, im);
    }

    public Complex sub(Imaginary b) {
        return cm(re, im - b.val);
    }

    public Complex mul(Real b) {
        return cm(re * b.val, im * b.val);
    }

    public Complex mul(Imaginary b) {
        double re = im * b.val * -1;
        double im = re * b.val;
        return cm(re, im);
    }

    public Complex mul(Complex b) {
        double ac = re * b.re;
        double ad = re * b.im;
        double bc = im * b.re;
        double bd = im * b.im;
        return cm(ac - bd, ad + bc);
    }

    public Complex div(Real b) {
        double r = im / b.val;
        double i = re / b.val * -1;
        return cm(r, i);
    }

    public Complex div(Complex b) {
        double i = (im * b.re - re * b.im) / (b.re * b.re + b.im * b.im);
        double r = (re + im * b.im) / b.re;
        return cm(r, i);
    }

    public Complex div(Imaginary b) {
        return cm(re / b.val, im / b.val);
    }

    public Real abs() {
        return re(Math.sqrt(re * re + im * im));
    }

    public Complex sqrt() {
        double theta = Math.atan(im / re) / 2;
        double rootLen = Math.sqrt(abs().val);
        return cm(Math.cos(theta) * rootLen, Math.sin(theta) * rootLen);
    }

    public Complex exp() {
        return im(im).exp().mul(re(re).exp());
    }

    public Complex pow(Real b) {
        double r = Math.pow(Math.sqrt(re * re + im * im), b.val);
        double theta = Math.atan(im / re) * b.val;
        return cm(r * Math.cos(theta), r * Math.sin(theta));
    }

    public String toString() {
        return re + " + " + im + "i";
    }

    public String toHexString() {
        return Double.toHexString(re) + " + " + Double.toHexString(im) + "i";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Complex other)) return false;
        return other.re == re && other.im == im;
    }

    @Override
    public Complex clone() {
        return new Complex(re, im);
    }

    @Override
    public int compareTo(@NotNull Complex other) {
        return abs().compareTo(other.abs());
    }
}
