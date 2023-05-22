package hr.fer.oprpp1.custom.collections;

/**
 * Representation of an object stack.
 * The implementation uses an ArrayIndexedCollection.
 *
 * @author Ian Golob
 */
public class ObjectStack<E> {

    private final ArrayIndexedCollection<E> array;

    /**
     * Creates an empty object stack
     */
    public ObjectStack(){
        array = new ArrayIndexedCollection<E>();
    }

    /**
     * @return True if stack contains no objects and false otherwise.
     */
    public boolean isEmpty(){
        return array.isEmpty();
    }

    /**
     * @return The number of currently stored objects in the stack.
     */
    public int size(){
        return array.size();
    }

    /**
     * Pushes given value on top of the stack.
     * Null values are not allowed.
     * @param value value to be pushed
     * @throws NullPointerException if value given is null.
     */
    public void push(E value){
        array.add(value);
    }

    /**
     * Removes last value pushed on the stack from the stack and returns it.
     * @return last value pushed on the stack
     * @throws EmptyStackException if the stack is empty
     */
    public E pop(){
        E value = peek();

        array.remove(array.size()-1);

        return value;
    }

    /**
     * Returns the last element placed on the stack but does not delete it from the stack.
     * @return last element placed on the stack
     * @throws EmptyStackException if the stack is empty
     */
    public E peek(){
        if(array.size() == 0){
            throw new EmptyStackException();
        }

        return array.get(array.size()-1);
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear(){
        array.clear();
    }

}
