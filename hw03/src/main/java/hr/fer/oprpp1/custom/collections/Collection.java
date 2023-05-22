package hr.fer.oprpp1.custom.collections;

/**
 * A class that represents a collection of objects.
 *
 * @author Ian Golob
 */
public interface Collection<E> {

    /**
     * @return True if collection size is 0 and false otherwise.
     */
    default boolean isEmpty(){
        return size() == 0;
    }

    /**
     * @return The number of currently stored objects in this collection.
     */
    int size();

    /**
     * @param value Value to be added to the collection.
     */
    void add(E value);

    /**
     * @return True only if the collection contains the given value,
     * as determined by the Equals method.
     */
    boolean contains(Object value);

    /**
     * @param value Value to be removed from the collection.
     * @return True only if the collection contains given value
     * as determined by the Equals method and removes one occurrence of it
     * (in this class it is not specified which one).
     */
    boolean remove(Object value);

    /**
     * @return Allocates a new array with its size equals to the size of this collection,
     * fills it with collection content and returns the array.
     * This method never returns null.
     */
    Object[] toArray();

    /**
     * @param processor Method calls processor.process(.) for each element of this collection.
     * The order in which elements will be sent is undefined in this class.
     */
    default void forEach(Processor<? super E> processor){
        ElementsGetter<E> elementsGetter = createElementsGetter();

        while(elementsGetter.hasNextElement()){
            processor.process(elementsGetter.getNextElement());
        }
    }

    /**
     * @param other Method adds into the current collection all elements from the given collection.
     * Other collection remains unchanged.
     * @throws NullPointerException If other is null
     */
    default void addAll(Collection<? extends E> other){
        if(other == null){
            throw new NullPointerException();
        }

        class AddAllProcessor<T> implements Processor<T> {

            private final Collection<T> collectionToAddTo;

            /**
             * Constructor with only one argument: collection that the processor should add objects to.
             * @param collectionToAddTo The collection that the processor should add objects to.
             */
            public AddAllProcessor(Collection<T> collectionToAddTo){
                this.collectionToAddTo = collectionToAddTo;
            }

            @Override
            public void process(T value){
                collectionToAddTo.add(value);
            }

        }

        other.forEach(new AddAllProcessor<E>(this));
    }

    ElementsGetter<E> createElementsGetter();

    /**
     * Removes all the elements from the collection.
     */
    void clear();

    /**
     * Adds all the elements from the given collection
     * that are acceptable according to the given tester to this collection.
     * @param col Given collection
     * @param tester Given tester
     */
    default void addAllSatisfying(Collection<? extends E> col, Tester<? super E> tester) {
        ElementsGetter<? extends E> elementsGetter = col.createElementsGetter();

        while (elementsGetter.hasNextElement()) {
            E element = elementsGetter.getNextElement();

            if (tester.test(element)) {
                add(element);
            }
        }
    }

}
