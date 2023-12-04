package dev.mv.engine.utils.math;

import dev.mv.engine.utils.generic.triplet.Either;

public class ComplexMath {

    public static final Real E = re(2.7182818284590452354);
    public static final Real PI = re(3.14159265358979323846);
    public static final Real R2_2 = re(0.7071067811865475244);
    public static final Imaginary I = im(1);
    private ComplexMath() {
    }

    public static Real re(int value) {
        return new Real(value);
    }

    public static Real re(long value) {
        return new Real(value);
    }

    public static Real re(float value) {
        return new Real(value);
    }

    public static Real re(double value) {
        return new Real(value);
    }

    public static Real re(String value) {
        return Real.valueOf(value);
    }

    public static Imaginary im(int value) {
        return new Imaginary(value);
    }

    public static Imaginary im(long value) {
        return new Imaginary(value);
    }

    public static Imaginary im(float value) {
        return new Imaginary(value);
    }

    public static Imaginary im(double value) {
        return new Imaginary(value);
    }

    public static Imaginary im(String value) {
        return Imaginary.valueOf(value);
    }

    public static Complex cm(int real, int imaginary) {
        return new Complex(real, imaginary);
    }

    public static Complex cm(long real, long imaginary) {
        return new Complex(real, imaginary);
    }

    public static Complex cm(float real, float imaginary) {
        return new Complex(real, imaginary);
    }

    public static Complex cm(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    public static Complex cm(String value) {
        return Complex.valueOf(value);
    }

    public static Real toRadians(Real degrees) {
        degrees.val *= 0.017453292519943295;
        return degrees;
    }

    public static Real toDegrees(Real radians) {
        radians.val *= 57.29577951308232;
        return radians;
    }

    public static Either<Complex, Real, Imaginary> simplifyDirect(ComplexNumber number) {
        Either<Complex, Real, Imaginary> result = new Either<>(Complex.class, Real.class, Imaginary.class);
        if (number instanceof Complex complex) {
            if (complex.re == 0 && complex.im == 0) {
                return result.assign(re(0));
            } else if (complex.re == 0) {
                return result.assign(im(complex.im));
            } else if (complex.im == 0) {
                return result.assign(re(complex.re));
            }
            return result.assign(complex);
        } else return result.assign(number);
    }

    public static ComplexNumber simplify(ComplexNumber number) {
        return simplifyDirect(number).value();
    }

}
