package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * An ordered linked-list-backed class that contains objects and extends the Collection class.
 *
 * @author Ian Golob
 */
public class LinkedListIndexedCollection<E> implements List<E> {

    /**
     * A representation of a node in the linked list, wraps around an object value.
     * Contains references to the previous and next node in the list.
     */
    private static class ListNode<E> {
        ListNode<E> prev;
        ListNode<E> next;
        E value;

        public ListNode(ListNode<E> prev, ListNode<E> next, E value) {
            this.prev = prev;
            this.next = next;
            this.value = value;
        }
    }

    private int size;
    private ListNode<E> first;
    private ListNode<E> last;
    private int modificationCount = 0;

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
    public LinkedListIndexedCollection(Collection<? extends E> other) {
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
    public void add(E value) {
        if(value == null){
            throw new NullPointerException();
        }

        ListNode<E> newNode = new ListNode<>(last, null, value);

        if (size == 0) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;

        size++;
        modificationCount++;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    @Override
    public boolean remove(Object value) {
        for (ListNode<E> node = first; node != null; node = node.next) {

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
                modificationCount++;
                return true;
            }

        }

        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        ListNode<E> node = first;
        for (int i = 0; node != null; node = node.next, i++) {
            array[i] = node.value;
        }

        return array;
    }

    @Override
    public E get(int index){
        if(index < 0 || index > size-1){
            throw new IndexOutOfBoundsException();
        }

        if(index < size/2){

            ListNode<E> node = first;
            for(int i = 0; i < index; i++, node = node.next);

            return node.value;
        }else{

            ListNode<E> node = last;
            for(int i = size-1; i > index; i--, node = node.prev);

            return node.value;
        }
    }

    @Override
    public void clear(){
        size = 0;
        first = last = null;
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

        ListNode<E> newNode = new ListNode<>(null, null, value);

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
        modificationCount++;
    }

    @Override
    public int indexOf(Object value){
        if(value == null){
            return -1;
        }

        ListNode<E> node = first;
        for(int i = 0; i < size; i++, node = node.next){
            if(node.value.equals(value)){
                return i;
            }
        }

        return -1;
    }

    @Override
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

                ListNode<E> node = first;
                for(int i = 0; i < index; i++, node = node.next);

                node.next.prev = node.prev;
                node.prev.next = node.next;

            }
        }

        size--;
        modificationCount++;
    }

    /**
     * Implementation of the ElementsGetter interface for the LinkedListIndexedCollection class.
     *
     * @author Ian Golob
     */
    private static class LinkedListElementsGetter<E> implements ElementsGetter<E> {

        private final LinkedListIndexedCollection<E> linkedListIndexedCollection;
        private ListNode<E> nextNode;
        private final int savedModificationCount;

        /**
         * @param linkedListIndexedCollection A reference to the LinkedListIndexedCollection that is iterated over.
         */
        public LinkedListElementsGetter(LinkedListIndexedCollection<E> linkedListIndexedCollection){
            this.linkedListIndexedCollection = linkedListIndexedCollection;
            nextNode = linkedListIndexedCollection.first;
            savedModificationCount = linkedListIndexedCollection.modificationCount;
        }

        @Override
        public E getNextElement() {
            if(modificationCountChanged()){
                throw new ConcurrentModificationException();
            }

            if(!hasNextElement()){
                throw new NoSuchElementException();
            }

            E element = nextNode.value;

            nextNode = nextNode.next;

            return element;
        }

        @Override
        public boolean hasNextElement() {
            if(modificationCountChanged()){
                throw new ConcurrentModificationException();
            }

            return nextNode != null;
        }

        /**
         * Checks if the saved modification count differs from the one in the given LinkedListIndexedCollection.
         * @return False if the saved modification count is equal to the one in the LinkedListIndexedCollection, true otherwise.
         */
        private boolean modificationCountChanged(){
            return savedModificationCount != linkedListIndexedCollection.modificationCount;
        }

    }

    @Override
    public ElementsGetter<E> createElementsGetter() {
        return new LinkedListElementsGetter<>(this);
    }

}


