package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits Element, and has a single read-only
 * String property: name.
 * Represents a variable name expression.
 *
 * @author Ian Golob
 */
public class ElementVariable extends Element {

    private final String name;

    public ElementVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String asText() {
        return name;
    }

}
