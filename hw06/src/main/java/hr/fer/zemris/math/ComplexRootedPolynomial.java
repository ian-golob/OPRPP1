package hr.fer.zemris.math;

import java.util.List;
import java.util.Objects;

public class ComplexRootedPolynomial {

    private final Complex constant;

    private final List<Complex> roots;

    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        this.constant = Objects.requireNonNull(constant);
        this.roots = List.of(roots);
    }

    // computes polynomial value at given point z
    public Complex apply(Complex z) {
        Complex result = constant;

        roots.forEach(root -> result.multiply(z.sub(root)));

        return result;
    }

    // converts this representation to ComplexPolynomial type
    public ComplexPolynomial toComplexPolynom() {

        Complex[] factors = new Complex[1];
        factors[0] = constant;

        for(int rootIndex = 0; rootIndex < roots.size(); rootIndex++){

            Complex[] newFactors = new Complex[rootIndex+2];
            for(int i = 0; i < rootIndex+2; i++) newFactors[i] = Complex.ZERO;

            Complex root = roots.get(rootIndex);
            for(int factorIndex = 0; factorIndex < rootIndex+1; factorIndex++){

                newFactors[factorIndex] = newFactors[factorIndex].add(factors[factorIndex].multiply(root.negate()));
                newFactors[factorIndex+1] = factors[factorIndex];

            }

            factors = newFactors;
        }

        return new ComplexPolynomial(factors);
    }


    // finds index of closest root for given complex number z that is within
    // treshold; if there is no such root, returns -1
    // first root has index 0, second index 1, etc
    public int indexOfClosestRootFor(Complex z, double treshold) {
        int closestIndex = -1;
        double closestDistance = Double.MAX_VALUE;

        for(int i = 0; i < roots.size(); i++){

            double distance = Complex.distance(z, roots.get(i));

            if(distance < treshold &&
                    (closestIndex == -1 || distance < closestDistance)){
                closestIndex = i;
                closestDistance = distance;
            }

        }

        return closestIndex;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("(").append(constant.toString()).append(")");

        for(Complex root: roots){
            sb.append(" * (z - (")
                    .append(root.toString())
                    .append("))");
        }

        return sb.toString();
    }

}