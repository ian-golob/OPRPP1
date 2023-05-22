package hr.fer.oprpp1.custom.collections;

/**
 * An interface that represents an ordered Collection.
 *
 * @author Ian Golob
 */
public interface List extends Collection {

    /**
     * Gets the object stored in the list on the given index.
     * @param index Position index
     * @throws IndexOutOfBoundsException If given index is invalid (less than 0 or more than size-1)
     * @return The object that is stored in the backing list at the position index
     */
    Object get(int index);

    /**
     * Inserts (does not overwrite) the given value at the given position in the list.
     * Elements starting from this position are shifted one position.
     * @param value Value to be inserted to the collection
     * @param position Inserts value to the given position index
     * @throws NullPointerException If value is null
     * @throws IndexOutOfBoundsException If position is less than 0 or more than the current list size
     */
    void insert(Object value, int position);

    /**
     * Searches the list and returns the index of the first occurrence of the given object value.
     * The equality is determined using the Equals method.
     * @param value Object value
     * @return Position index of given value or -1 if value is not found or is null
     */
    int indexOf(Object value);

    /**
     * Removes the element at specified index from the list
     * @param index position index
     * @throws IndexOutOfBoundsException if given index is invalid (less than 0 or more than size-1)
     */
    void remove(int index);

}
