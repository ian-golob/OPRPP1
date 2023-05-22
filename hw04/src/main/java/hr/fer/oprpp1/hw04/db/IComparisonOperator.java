package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface that represents a comparison operator.
 *
 * @author Ian Golob
 */
public interface IComparisonOperator {

    /**
     * Evaluates the two given string values according to the comparison operator rule.
     * @param value1 The first string literal.
     * @param value2 The second string literal.
     * @return True if the comparison operation evaluates to true, false otherwise.
     */
    boolean satisfied(String value1, String value2);

}
