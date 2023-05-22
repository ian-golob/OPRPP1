package hr.fer.zemris.math;

import java.util.function.Function;

public class NewtonRaphsonFractalFunction implements Function<Complex, Short> {

    private final int maxIter;

    private final ComplexRootedPolynomial rootedPolynomial;
    private final ComplexPolynomial polynomial;
    private final ComplexPolynomial derivation;

    private final double convergenceThreshold;

    private final double rootThreshold;

    public NewtonRaphsonFractalFunction(int maxIter, ComplexRootedPolynomial rootedPolynomial,
                                        double convergenceThreshold, double rootThreshold){
        this.maxIter = maxIter;
        this.rootedPolynomial = rootedPolynomial;
        this.polynomial = rootedPolynomial.toComplexPolynom();
        this.rootThreshold = rootThreshold;
        this.derivation = polynomial.derive();
        this.convergenceThreshold = convergenceThreshold;
    }


    @Override
    public Short apply(Complex c) {
        Complex zn = c;
        Complex znOld;
        int iterNum = 0;
        do {
            znOld = zn;
            zn = zn.sub(polynomial.apply(zn).divide(derivation.apply(zn)));
            iterNum++;
        } while(zn.sub(znOld).module() > convergenceThreshold && iterNum<maxIter);

        return (short) (rootedPolynomial.indexOfClosestRootFor(zn, rootThreshold) + 1);
    }
}