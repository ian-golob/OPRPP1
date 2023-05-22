package hr.fer.oprpp1.hw04.db;

/**
 * A class that contains some implementations of IComparisonOperator for student records.
 *
 * @author Ian Golob
 */
public class ComparisonOperators {

    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;

    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;

    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;

    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;

    public static final IComparisonOperator EQUALS = String::equals;

    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> !value1.equals(value2);

    public static final IComparisonOperator LIKE = (value1, value2) -> {

        if(!value2.contains("*")){
            return value1.equals(value2);
        }

        if(value2.indexOf('*') != value2.lastIndexOf('*')){
            throw new WildcardException();
        }

        String value2Start = value2.substring(0, value2.indexOf('*'));
        String value2End = value2.substring(value2.indexOf('*') + 1);

        return value1.startsWith(value2Start) && value1.endsWith(value2End);
    };


}
