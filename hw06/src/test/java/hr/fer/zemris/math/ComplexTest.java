package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComplexTest {

    @Test
    public void parseComplexTest(){

        assertEquals(Complex.ZERO, Complex.parseComplex("0"));
        assertEquals(Complex.ZERO, Complex.parseComplex("i0"));
        assertEquals(Complex.ZERO, Complex.parseComplex("0+i0"));
        assertEquals(new Complex(0, -0D), Complex.parseComplex("0-i0"));

        assertEquals(Complex.ONE, Complex.parseComplex("1"));
        assertEquals(Complex.ONE_NEG, Complex.parseComplex("-1 + i0"));
        assertEquals(Complex.IM, Complex.parseComplex("i"));
        assertEquals(Complex.IM_NEG, Complex.parseComplex("0 - i1"));

        assertEquals(new Complex(-4.5, 13.78), Complex.parseComplex("- 4.5 + i 13.78"));
        assertEquals(new Complex(0, -2), Complex.parseComplex("-i2"));

        assertThrows(NumberFormatException.class, () -> Complex.parseComplex(""));
        assertThrows(NumberFormatException.class, () -> Complex.parseComplex("a"));
        assertThrows(NumberFormatException.class, () -> Complex.parseComplex("2.4i"));
    }

}
