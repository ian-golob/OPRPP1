package hr.fer.oprpp1.custom.collections;

/**
 * A class that represents a collection of objects.
 *
 * @author Ian Golob
 */
public class Collection {

    /**
     * @return True if collection contains no objects and false otherwise.
     */
    public boolean isEmpty(){
        return size() == 0;
    }

    /**
     * @return The number of currently stored objects in this collection.
     */
    public int size(){
        return 0;
    }

    /**
     * @param value Value to be added to the collection.
     */
    public void add(Object value){}

    /**
     * @return True only if the collection contains given value,
     * as determined by equals method.
     */
    public boolean contains(Object value){
        return false;
    }

    /**
     * @param value Value to be removed from the collection.
     * @return True only if the collection contains given value
     * as determined by equals method and removes one occurrence of it
     * (in this class it is not specified which one).
     */
    public boolean remove(Object value){
        return false;
    }

    /**
     * @return Allocates new array with size equals to the size of this collection,
     * fills it with collection content and returns the array.
     * This method never returns null.
     */
    public Object[] toArray(){
        throw new UnsupportedOperationException();
    }

    /**
     * @param processor Method calls processor.process(.) for each element of this collection.
     * The order in which elements will be sent is undefined in this class.
     */
    public void forEach(Processor processor){}

    /**
     * @param other Method adds into the current collection all elements from the given collection.
     * Other collection remains unchanged.
     * @throws NullPointerException If other is null
     */
    public void addAll(Collection other){
        if(other == null){
            throw new NullPointerException();
        }

        class AddAllProcessor extends Processor {

            private final Collection thisCollection;

            public AddAllProcessor(Collection thisCollection){
                this.thisCollection = thisCollection;
            }

            @Override
            public void process(Object value){
                thisCollection.add(value);
            }
        }

        other.forEach(new AddAllProcessor(this));
    }

}
