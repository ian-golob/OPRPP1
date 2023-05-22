package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.List;

public class ComplexPolynomial {

    private final List<Complex> factors;

    public ComplexPolynomial(Complex ...factors) {

        int order = 0;

        for(int i = 0; i < factors.length; i++){
            if(!factors[i].equals(Complex.ZERO)){
                order = i;
            }
        }

        this.factors = List.of(Arrays.copyOfRange(factors, 0, order+1));
    }

    // returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
    public short order() {
        return (short) factors.size();
    }

    // computes a new polynomial this*p
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        int resultOrder = this.order() + p.order();

        Complex[] resultFactors = new Complex[resultOrder];

        for(int i = 0; i < resultOrder; i++) resultFactors[i] = Complex.ZERO;

        int thisOrder = this.order();
        int pOrder = p.order();

        for(int i = 0; i < thisOrder; i++) {
            for (int j = 0; j < pOrder; j++) {

                Complex thisFactor = this.factors.get(i);
                Complex pFactor = p.factors.get(j);

                resultFactors[i + j] = resultFactors[i + j].add(thisFactor.multiply(pFactor));
            }
        }

        return new ComplexPolynomial(resultFactors);
    }

    // computes first derivative of this polynomial; for example, for
    // (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
    public ComplexPolynomial derive() {
        Complex[] newFactors = new Complex[factors.size()-1];

        for(int i = 1; i < factors.size(); i++){
            newFactors[i-1] = new Complex(i * factors.get(i).getRe(), i * factors.get(i).getIm());
        }

        return new ComplexPolynomial(newFactors);
    }

    // computes polynomial value at given point z
    public Complex apply(Complex z) {
        Complex result = Complex.ZERO;

        for(int i = 0; i < factors.size(); i++){
            result = result.add(z.power(i).multiply(factors.get(i)));
        }

        return result;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        boolean first = true;

        for(int i = factors.size()-1; i >= 0; i--){
            if(first){
                first = false;
            } else{
                sb.append(" + ");
            }

            sb.append("(")
                    .append(factors.get(i))
                    .append(")");

            if(i != 0){
                sb.append(" * z^")
                        .append(i);
            }
        }

        return sb.toString();
    }

}
