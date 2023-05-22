package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits Element and has a single read-only double property: value.
 * Represents a constant integer expression.
 *
 * @author Ian Golob
 */
public class ElementConstantDouble extends Element {

    private final double value;

    public ElementConstantDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String asText() {
        return Double.toString(value);
    }

}
