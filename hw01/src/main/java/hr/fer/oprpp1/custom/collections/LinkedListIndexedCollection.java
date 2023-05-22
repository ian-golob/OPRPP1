package hr.fer.oprpp1.custom.collections;

/**
 * An ordered linked-list-backed class that contains objects and extends the Collection class.
 *
 * @author Ian Golob
 */
public class LinkedListIndexedCollection extends Collection {

    /**
     * A representation of a node in the linked list, wraps around an object value.
     * Contains references to the previous and next node in the list.
     */
    private static class ListNode {
        ListNode prev;
        ListNode next;
        Object value;

        public ListNode(ListNode prev, ListNode next, Object value) {
            this.prev = prev;
            this.next = next;
            this.value = value;
        }
    }

    private int size;
    private ListNode first;
    private ListNode last;

    /**
     * Creates an empty list
     */
    public LinkedListIndexedCollection() {
        clear();
    }

    /**
     * Creates a new list with elements copied from the given collection
     * @param other Collection whose elements are copied into this newly constructed collection
     * @throws NullPointerException If other is null
     */
    public LinkedListIndexedCollection(Collection other) {
        this();
        addAll(other);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Adds the given object into this list at the end
     * @param value Value to be added to the end of the list
     * @throws NullPointerException if value is null
     */
    @Override
    public void add(Object value) {
        if(value == null){
            throw new NullPointerException();
        }

        ListNode newNode = new ListNode(last, null, value);

        if (size == 0) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;

        size++;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    @Override
    public boolean remove(Object value) {
        for (ListNode node = first; node != null; node = node.next) {

            if (node.value.equals(value)) {

                if(node != first && node != last){

                    node.prev.next = node.next;
                    node.next.prev = node.prev;

                } else {

                    if(node == first){
                        first = node.next;
                        if(first != null){
                            first.prev = null;
                        }
                    }

                    if(node == last){
                        last = node.prev;
                        if(last != null){
                            last.next = null;
                        }
                    }
                }

                size--;
                return true;
            }

        }

        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        ListNode node = first;
        for (int i = 0; node != null; node = node.next, i++) {
            array[i] = node.value;
        }

        return array;
    }

    @Override
    public void forEach(Processor processor) {
        for (ListNode node = first; node != null; node = node.next) {
            processor.process(node.value);
        }
    }

    /**
     * Returns the object that is stored in linked list at given position index
     * @param index position index
     * @return object stored at given position index
     * @throws IndexOutOfBoundsException if index is invalid (less than 0 or more than size-1)
     */
    public Object get(int index){
        if(index < 0 || index > size-1){
            throw new IndexOutOfBoundsException();
        }

        if(index < size/2){

            ListNode node = first;
            for(int i = 0; i < index; i++, node = node.next);

            return node.value;
        }else{

            ListNode node = last;
            for(int i = size-1; i > index; i--, node = node.prev);

            return node.value;
        }
    }

    public void clear(){
        size = 0;
        first = last = null;
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in the list.
     * Elements starting from this position are shifted one position.
     * @param value Value to be inserted to the list
     * @param position Inserts value to the given position index
     * @throws NullPointerException If value is null
     * @throws IndexOutOfBoundsException If position is less than 0 or more than the current list size
     */
    public void insert(Object value, int position){
        if(value == null){
            throw new NullPointerException();
        }

        if(position < 0 || position > size){
            throw new IndexOutOfBoundsException();
        }

        ListNode newNode = new ListNode(null, null, value);

        if (size == 0) {

            first = newNode;
            last = newNode;

        }else{

            if(position == 0){

                first.prev = newNode;
                newNode.next = first;
                first = newNode;

            }else if (position == size){

                last.next = newNode;
                newNode.prev = last;
                last = newNode;

            }else{

                newNode.next = first;
                for(int i = 0; i < position; i++){
                    newNode.prev = newNode.next;
                    newNode.next = newNode.next.next;
                }

                newNode.prev.next = newNode;
                newNode.next.prev = newNode;

            }
        }

        size++;
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given value.
     * The equality is determined using the equals method.
     * @param value value to search
     * @return the index of the first occurrence of the given value or -1 if the value is not found
     */
    public int indexOf(Object value){
        if(value == null){
            return -1;
        }

        ListNode node = first;
        for(int i = 0; i < size; i++, node = node.next){
            if(node.value.equals(value)){
                return i;
            }
        }

        return -1;
    }

    /**
     * Removes the element at specified index from the list
     * @param index position index
     * @throws IndexOutOfBoundsException if given index is invalid (less than 0 or more than size-1)
     */
    public void remove(int index){
        if(index < 0 || index > size-1){
            throw new IndexOutOfBoundsException();
        }

        if(size == 1){

            first = last = null;

        }else{
            if(index == 0){

                first = first.next;
                first.prev = null;

            }else if(index == size-1){

                last = last.prev;
                last.next = null;

            }else{

                ListNode node = first;
                for(int i = 0; i < index; i++, node = node.next);

                node.next.prev = node.prev;
                node.prev.next = node.next;

            }
        }

        size--;
    }

}


