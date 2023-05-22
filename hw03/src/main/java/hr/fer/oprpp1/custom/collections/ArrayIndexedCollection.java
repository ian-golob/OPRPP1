package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * An ordered array-backed class that contains objects and extends the Collection class.
 *
 * @author Ian Golob
 */
public class ArrayIndexedCollection<E> implements List<E> {

    private int size;
    private Object[] elements;
    private int modificationCount = 0;

    /**
     * Creates an empty array with the default internal capacity: 16.
     */
    public ArrayIndexedCollection(){
        this(16);
    }

    /**
     * @param initialCapacity The initial internal array capacity
     * @throws IllegalArgumentException If initialCapacity is less than 1
     */
    public ArrayIndexedCollection(int initialCapacity){
        if(initialCapacity < 1){
            throw new IllegalArgumentException("initialCapacity must be at least 1, initialCapacity given: " + initialCapacity);
        }

        elements = new Object[initialCapacity];
    }

    /**
     * @param other Collection to be copied to the new collection
     * @throws NullPointerException If other is null
     */
    public ArrayIndexedCollection(Collection<? extends E> other){
        this(other, 16);
    }

    /**
     * @param other Collection to be copied to the new collection
     * @param initialCapacity The initial internal array capacity
     * @throws NullPointerException If other is null
     * @throws IllegalArgumentException If initialCapacity is less than 1
     */
    public ArrayIndexedCollection(Collection<? extends E> other, int initialCapacity){
        this(Math.max(other.size(), initialCapacity));

        this.addAll(other);
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean contains(Object value){
        return indexOf(value) != -1;
    }

    @Override
    public boolean remove(Object value){
        int index = indexOf(value);

        if(index == -1) {
            return false;
        }

        remove(index);

        modificationCount++;
        return true;
    }

    @Override
    public Object[] toArray(){
        Object[] array = new Object[size];

        for(int i = 0; i < size; i++){
            array[i] = elements[i];
        }

        return array;
    }

    /**
     * Adds the given value to the end of the array.
     * @param value Value to be added to the collection.
     * @throws NullPointerException If value is null.
     */
    @Override
    public void add(E value){
        insert(value, size);
    }

    @Override
    public E get(int index){
        if(index < 0 || index > size-1){
            throw new IndexOutOfBoundsException("index is out of bounds, index given: " + index);
        }

        return (E) elements[index];
    }

    /**
     * Removes all the elements from the collection.
     * The allocated array is left at the current capacity.
     */
    @Override
    public void clear(){
        for(int i = 0; i < size; i++){
            elements[i] = null;
        }

        size = 0;
        modificationCount++;
    }

    @Override
    public void insert(E value, int position){
        if(value == null){
            throw new NullPointerException();
        }

        if(position < 0 || position > size){
            throw new IndexOutOfBoundsException();
        }

        if(size == elements.length){
            duplicateElementsCapacity();
        }

        for(int i = size; i > position; i--){
            elements[i] = elements[i-1];
        }
        elements[position] = value;

        size++;
        modificationCount++;
    }

    @Override
    public int indexOf(Object value){
        if(value == null){
            return -1;
        }

        for(int i = 0, len = elements.length; i < len; i++){
            if(value.equals(elements[i])){
                return i;
            }
        }

        return -1;
    }

    @Override
    public void remove(int index){
        if(index < 0 || index > size - 1){
            throw new IndexOutOfBoundsException();
        }

        for(int i = index; i < size; i++){
            elements[i] = elements[i+1];
        }
        elements[--size] = null;

        modificationCount++;
    }

    /**
     * Duplicates the current internal array capacity.
     */
    private void duplicateElementsCapacity(){
        Object[] newElements = new Object[elements.length * 2];
        for(int i = 0, len = elements.length; i < len; i++){
            newElements[i] = elements[i];
        }

        elements = newElements;
    }

    /**
     * Implementation of the ElementsGetter interface for the ArrayIndexedCollection class.
     *
     * @author Ian Golob
     */
    private static class ArrayIndexedElementsGetter<E> implements ElementsGetter<E> {

        private final ArrayIndexedCollection<E> arrayIndexedCollection;
        private int nextPosition;
        private final int savedModificationCount;

        /**
         * @param arrayIndexedCollection A reference to the ArrayIndexedCollection that is iterated over.
         */
        public ArrayIndexedElementsGetter(ArrayIndexedCollection<E> arrayIndexedCollection){
            this.arrayIndexedCollection = arrayIndexedCollection;
            nextPosition = 0;
            savedModificationCount = arrayIndexedCollection.modificationCount;
        }

        @Override
        public E getNextElement() {
            if(modificationCountChanged()){
                throw new ConcurrentModificationException();
            }

            if(!hasNextElement()){
                throw new NoSuchElementException();
            }

            return (E) arrayIndexedCollection.elements[nextPosition++];
        }

        @Override
        public boolean hasNextElement() {
            if(modificationCountChanged()){
                throw new ConcurrentModificationException();
            }

            return nextPosition < arrayIndexedCollection.size;
        }

        /**
         * Checks if the saved modification count differs from the one in the given ArrayIndexedCollection.
         * @return False if the saved modification count is equal to the one in the ArrayIndexedCollection, true otherwise.
         */
        private boolean modificationCountChanged(){
            return savedModificationCount != arrayIndexedCollection.modificationCount;
        }

    }

    @Override
    public ElementsGetter<E> createElementsGetter() {
        return new ArrayIndexedElementsGetter<>(this);
    }

}
