package hr.fer.oprpp1.custom.collections;

/**
 * An ordered array-backed class that contains objects and extends the Collection class.
 *
 * @author Ian Golob
 */
public class ArrayIndexedCollection extends Collection {

    private int size;
    private Object[] elements;

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
    public ArrayIndexedCollection(Collection other){
        this(other, 16);
    }

    /**
     * @param other Collection to be copied to the new collection
     * @param initialCapacity The initial internal array capacity
     * @throws NullPointerException If other is null
     * @throws IllegalArgumentException If initialCapacity is less than 1
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity){
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

    @Override
    public void forEach(Processor processor){
        for(int i = 0; i < size; i++){
            processor.process(elements[i]);
        }
    }

    /**
     * Adds the given value to the end of the array
     * @param value Value to be added to the collection.
     * @throws NullPointerException If value is null.
     */
    @Override
    public void add(Object value){
        insert(value, size);
    }

    /**
     * @param index Position index
     * @throws IndexOutOfBoundsException If index is les than 0 or more than (array size - 1)
     * @return The object that is stored in the backing array at the position index
     */
    public Object get(int index){
        if(index < 0 || index > size-1){
            throw new IndexOutOfBoundsException("index is out of bounds, index given: " + index);
        }

        return elements[index];
    }

    /**
     * Removes all elements from the collection.
     * The allocated array is left at current capacity.
     */
    public void clear(){
        for(int i = 0; i < size; i++){
            elements[i] = null;
        }

        size = 0;
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in the array.
     * Elements starting from this position are shifted one position.
     * @param value Value to be inserted to the collection
     * @param position Inserts value to the given position index
     * @throws NullPointerException If value is null
     * @throws IndexOutOfBoundsException If position is less than 0 or more than the current collection size
     */
    public void insert(Object value, int position){
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
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given object value
     * The equality is determined using the equals method
     * @param value Object value
     * @return Position index of given value or -1 if value is not found or is null
     */
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

    /**
     * Removes element at the specified index from the collection
     * @param index Specified index
     * @throws IndexOutOfBoundsException If index is less than 0 or more than size-1
     */
    public void remove(int index){
        if(index < 0 || index > size - 1){
            throw new IndexOutOfBoundsException();
        }

        for(int i = index; i < size; i++){
            elements[i] = elements[i+1];
        }
        elements[--size] = null;
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

}
