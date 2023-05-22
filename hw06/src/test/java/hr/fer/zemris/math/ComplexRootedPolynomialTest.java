package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

public class ComplexRootedPolynomialTest {

    @Test
    public void toComplexPolynomTest(){
        ComplexRootedPolynomial crp1 = new ComplexRootedPolynomial(
                Complex.ONE, Complex.IM);
        System.out.println(crp1.toComplexPolynom());

        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        ComplexPolynomial cp = crp.toComplexPolynom();

        System.out.println(crp);
        System.out.println(cp);
        System.out.println(cp.derive());
    }
}
