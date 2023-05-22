package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that represents an unmodifiable complex number.
 *
 * @author Ian Golob
 */
public class Complex  {

    private final double re;

    private final double im;

    public static final Complex ZERO = new Complex(0,0);
    public static final Complex ONE = new Complex(1,0);
    public static final Complex ONE_NEG = new Complex(-1,0);
    public static final Complex IM = new Complex(0,1);
    public static final Complex IM_NEG = new Complex(0,-1);

    public Complex() {
        this(0,0);
    }

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // returns module of complex number
    public double module() {
        return Math.sqrt(Math.pow(im, 2) + Math.pow(re, 2));
    }

    // returns this*c
    public Complex multiply(Complex c) {
        double newRe = this.re * c.re - this.im * c.im;
        double newIm = this.im * c.re + this.re * c.im;

        return new Complex(newRe, newIm);
    }

    // returns this/c
    public Complex divide(Complex c) {
        double divisor = (Math.pow(c.re, 2) + Math.pow(c.im, 2));

        double newRe = (this.re * c.re + this.im * c.im) / divisor;

        double newIm = (this.im * c.re - this.re * c.im) / divisor;

        return new Complex(newRe, newIm);
    }

    // returns this+c
    public Complex add(Complex c) {
        double newRe = this.re + c.re;
        double newIm = this.im + c.im;

        return new Complex(newRe, newIm);
    }


    // returns this-c
    public Complex sub(Complex c) {
        double newRe = this.re - c.re;
        double newIm = this.im - c.im;

        return new Complex(newRe, newIm);
    }

    // returns -this
    public Complex negate() {
        return new Complex(-re, -im);
    }

    // returns this^n, n is non-negative integer
    public Complex power(int n) {
        if(n < 0){
            throw new IllegalArgumentException("n must be greater than 0.");
        }

        double newTheta = n * theta();
        double newModule = Math.pow(module(), n);


        double newRe = newModule * Math.cos(newTheta);
        double newIm = newModule * Math.sin(newTheta);

        return new Complex(newRe, newIm);
    }

    // returns n-th root of this, n is positive integer
    public List<Complex> root(int n) {
        if(n < 1){
            throw new IllegalArgumentException("n must be greater than 0.");
        }

        double newModule = Math.rint(Math.pow(module(), 1.0/n));

        double startingTheta = theta() / n;
        double thetaAddition = 2 * Math.PI / n;

        List<Complex> complexList = new ArrayList<>(n);

        double currentTheta = startingTheta;
        for(int i = 0; i < n; i++, currentTheta+=thetaAddition){

            double newRe = newModule * Math.cos(currentTheta);
            double newIm = newModule * Math.sin(currentTheta);

            complexList.add(new Complex(newRe, newIm));
        }

        return complexList;
    }

    public double theta(){
        return Math.atan2(im, re);
    }

    /**
     * Calculates the distance between two complex numbers.
     * @param c1 The first complex number.
     * @param c2 The second complex number.
     * @return The distance between two complex numbers.
     */
    public static double distance(Complex c1, Complex c2){
        return Math.sqrt(Math.pow(c1.re - c2.re, 2) + Math.pow(c1.im - c2.im, 2));
    }

    public static Complex parseComplex(String string){
        if(string == null || string.length() == 0){
            throw new NumberFormatException();
        }

        string = removeWhitespace(string);

        double re = 0;
        double im = 0;

        if(!string.contains("i")){
            re = Double.parseDouble(string);
        } else if (string.startsWith("+i") ||
            string.startsWith("-i") ||
            string.startsWith("i")){

            String imString;

            if(string.endsWith("i")){
                im = 1;
            } else {
                im = Double.parseDouble(string.substring(string.indexOf("i")+1));
            }

            if(string.startsWith("-")){
                im = -im;
            }
        } else {

            int indexOfI = string.indexOf("i");

            if (string.contains("+i") || string.contains("-i")){
                im = parseComplex(string.substring(indexOfI-1)).getIm();
                re = parseComplex(string.substring(0, indexOfI-1)).getRe();
            } else {
                throw new NumberFormatException();
            }
        }

        return new Complex(re, im);
    }

    private static String removeWhitespace(String string) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < string.length(); i++){
            if(!Character.isWhitespace(string.charAt(i))){
                sb.append(string.charAt(i));
            }
        }

        return sb.toString();
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public String toString() {
        return re + " + " + im + "i";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.re, re) == 0 && Double.compare(complex.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }
}
