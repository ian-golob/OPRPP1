package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface that returns some field from the given StudentRecord.
 *
 * @author Ian Golob
 */
public interface IFieldValueGetter {

    /**
     * Returns a field from the given record.
     * @param record Record whose field should be returned.
     * @return A field from the given record.
     */
    String get(StudentRecord record);

}
