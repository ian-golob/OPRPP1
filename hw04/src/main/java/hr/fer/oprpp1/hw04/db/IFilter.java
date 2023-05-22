package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface that filters StudentRecords.
 *
 * @author Ian Golob
 */
public interface IFilter {

    /**
     * A method that takes a StudentRecord and returns true
     * if the record conforms with the filter's rule, returns false otherwise.
     * @param record A student record.
     * @return True if the record conforms with the filter's rule, false otherwise.
     */
    boolean accepts(StudentRecord record);

}
