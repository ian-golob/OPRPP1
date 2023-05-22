package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits Element and has a single read-only int property: value.
 * Represents a constant integer expression.
 *
 * @author Ian Golob
 */
public class ElementConstantInteger extends Element {

    private final int value;

    public ElementConstantInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String asText() {
        return Integer.toString(value);
    }

}
