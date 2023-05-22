package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits Element and has single read-only String property: name.
 * Represents a function call expression.
 *
 * @author Ian Golob
 */
public class ElementFunction extends Element {

    private final String name;

    public ElementFunction(String name) {
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
