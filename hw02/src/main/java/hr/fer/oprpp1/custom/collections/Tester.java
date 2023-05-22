package hr.fer.oprpp1.custom.collections;

/**
 * A functional class containing a method that tests if a given object is acceptable or not.
 *
 * @author Ian Golob
 */
public interface Tester {

    /**
     * Tests if a given object is acceptable.
     * @param obj Given object
     * @return True if the object is acceptable, false otherwise.
     */
    boolean test(Object obj);

}
