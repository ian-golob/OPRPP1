package hr.fer.oprpp1.custom.collections;

/**
 * A representation of an iterator.
 * Iterates over the elements in the associated Collection class.
 *
 * @author Ian Golob
 */
public interface ElementsGetter {

    /**
     * A method that iterates over the collection.
     * When called returns the next un-iterated element in the collection (by this ElementsGetter).
     * @return The next element in the collection.
     * @throws java.util.NoSuchElementException If the next element does not exist.
     * @throws java.util.ConcurrentModificationException If the collection has changed since creating this object.
     */
    Object getNextElement();

    /**
     * A method that returns if there are any un-iterated elements in the collection (by this ElementsGetter).
     * @return True if there are un-iterated elements in the collection, false otherwise.
     * @throws java.util.ConcurrentModificationException If the collection has changed since creating this object.
     */
    boolean hasNextElement();

    /**
     * Processes the remaining un-iterated elements with the given Processor.
     * @param p The processor that processes the remaining elements.
     * @throws java.util.ConcurrentModificationException If the collection has changed since creating this object.
     */
    default void processRemaining(Processor p){

        while(hasNextElement()){
            p.process(getNextElement());
        }

    }

}
