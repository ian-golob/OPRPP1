package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct. It inherits from Node class.
 *
 * @author Ian Golob
 */
public class ForLoopNode extends Node {

    private final ElementVariable variable;
    private final Element startExpression;
    private final Element endExpression;
    private final Element stepExpression;

    /**
     * @param variable
     * @param startExpression
     * @param endExpression
     * @param stepExpression nullable
     * @throws NullPointerException If variable, startExpression or endExpression given are null.
     */
    public ForLoopNode(ElementVariable variable, Element startExpression,
                       Element endExpression, Element stepExpression) {
        if(variable == null || startExpression == null || endExpression == null){
            throw new NullPointerException();
        }

        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("{$FOR ");

        builder.append(variable.asText());
        builder.append(' ');

        builder.append(startExpression.asText());
        builder.append(' ');

        builder.append(endExpression.asText());
        builder.append(' ');

        if(stepExpression != null){
            builder.append(stepExpression.asText());
            builder.append(' ');
        }

        builder.append("$}");

        builder.append(super.toString());

        builder.append("{$END$}");

        return builder.toString();
    }

}
