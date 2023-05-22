package hr.fer.oprpp1.hw04.db;

/**
 * A class that represents a singular conditional expression.
 *
 * @author Ian Golob
 */
public class ConditionalExpression {

    private final IFieldValueGetter fieldGetter;

    private final String stringLiteral;

    private final IComparisonOperator comparisonOperator;

    public ConditionalExpression(IFieldValueGetter fieldValueGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldValueGetter;
        this.stringLiteral = stringLiteral;
        this.comparisonOperator = comparisonOperator;
    }

    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    public String getStringLiteral() {
        return stringLiteral;
    }

    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

}
