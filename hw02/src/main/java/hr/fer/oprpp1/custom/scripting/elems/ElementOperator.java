package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits Element and has single read-only String property: symbol.
 * Represents an operator expression.
 *
 * @author Ian Golob
 */
public class ElementOperator extends Element {

    private final String symbol;

    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String asText() {
        return symbol;
    }

}
