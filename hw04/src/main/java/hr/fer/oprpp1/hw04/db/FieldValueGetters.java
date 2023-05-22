package hr.fer.oprpp1.hw04.db;

/**
 * A class that contains some implementations of IFieldValueGetter for student records.
 *
 * @author Ian Golob
 */
public class FieldValueGetters {
    
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

    public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

    public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;

}
