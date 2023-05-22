package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a collection that stores key-value pairs by using key hash values.
 * Keys can not be null, values can be null.
 *
 * @param <K> Key type.
 * @param <V> Value Type.
 *
 * @author Ian Golob
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private TableEntry<K, V>[] table;

    private int size = 0;

    private int modificationCount = 0;

    /**
     * A representation of a key-value entry in the hashtable.
     * @param <K> Key type.
     * @param <V> Value type.
     */
    public static class TableEntry<K, V> {

        private final K key;
        private V value;
        private TableEntry<K, V> next = null;

        public TableEntry(K key, V value) {
            if(key == null){
                throw new NullPointerException("The given key must not be null.");
            }

            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

    }

    /**
     * An implementation of an iterator for the SimpleHashtable, iterates over table entries.
     */
    private class SimpleHashtableIterator implements Iterator<SimpleHashtable.TableEntry<K, V>> {

        private int savedModificationCount;
        private int entriesLeft;

        TableEntry<K, V> lastEntry = null;
        TableEntry<K, V> nextEntry = null;
        private int currentSlotId = -1;

        public SimpleHashtableIterator(){
            savedModificationCount = modificationCount;
            entriesLeft = size;
        }

        @Override
        public boolean hasNext() {
            checkIfHashtableChanged();

            return entriesLeft > 0;
        }

        @Override
        public TableEntry<K, V> next() {
            checkIfHashtableChanged();

            if(!hasNext()){
                throw new NoSuchElementException();
            }

            while(currentSlotId < table.length){

                if(nextEntry == null){
                    currentSlotId++;

                    nextEntry = table[currentSlotId];
                } else{
                    lastEntry = nextEntry;
                    nextEntry = nextEntry.next;
                    entriesLeft--;

                    return lastEntry;
                }
            }
            throw new RuntimeException();
        }

        @Override
        public void remove() {
            if(lastEntry == null){
                throw new IllegalStateException();
            }

            checkIfHashtableChanged();

            SimpleHashtable.this.remove(lastEntry.key);

            lastEntry = null;
            savedModificationCount++;
            entriesLeft--;
        }

        /**
         * Checks if the SimpleHashtable modification count changed and throws
         * a ConcurrentModificationException if it did.
         * @throws ConcurrentModificationException if the SimpleHashtable modification count changed.
         */
        private void checkIfHashtableChanged() {
            if(modificationCount != savedModificationCount){
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Creates an empty hashtable with the default internal capacity of 16.
     */
    public SimpleHashtable(){
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Creates an empty hashtable with the default internal capacity set to the first power of 2 larger or equal to the given initial capacity.
     * @param capacity The initial capacity to initialize the internal collection size to.
     * @throws IllegalArgumentException If capacity given is less than 1.
     */
    public SimpleHashtable(int capacity){
        if(capacity < 1){
            throw new IllegalArgumentException("The given capacity must be greater than 0.");
        }

        capacity = getNextPowerOf2(capacity);

        // noinspection unchecked
        table = new TableEntry[capacity];
    }

    /**
     * Puts the given value at the entry associated with the given key
     * (creates one if it does not exist).
     * Returns the last value associated with the given key or null if there was no such value.
     * Null key is invalid.
     * @param key The key to put the given value at.
     * @param value The value to put in the entry associated with the given key.
     * @return The last value associated with the given key or null if there was no such value.
     * @throws NullPointerException If the given key is null.
     */
    public V put(K key, V value){
        if(key == null){
            throw new NullPointerException("The given key must not be null.");
        }

        adjustTableCapacity();

        V returnValue = put(key, value, table);

        if(returnValue == null){
            size++;
            modificationCount++;
        }

        return returnValue;
    }

    /**
     * Puts the given value at the entry associated with the given key
     * (creates one if it does not exist)in the given table.
     * Returns the last value associated with the given key or null if there was no such value.
     * Null key is invalid. DOES NOT change the size variable.
     * @param key The key to put the given value at.
     * @param value The value to put in the entry associated with the given key.
     * @param table The value to add the given key-value pair to.
     * @return The last value associated with the given key or null if there was no such value.
     * @throws NullPointerException If the given key or table are null.
     * @throws IllegalArgumentException If the length of the given table is less than 1.
     */
    private V put(K key, V value, TableEntry<K, V>[] table){

        if(key == null){
            throw new NullPointerException("The given key must not be null.");
        }

        if(table == null){
            throw new NullPointerException("The given table must not be null.");
        }

        if(table.length < 1){
            throw new IllegalArgumentException("The table length must be at least 1.");
        }

        int slotId = calculateSlotId(key, table.length);

        if(table[slotId] == null){

            table[slotId] = new TableEntry<>(key, value);

            return null;
        } else {

            TableEntry<K, V> entry = table[slotId];
            for(; entry.next != null && !entry.key.equals(key); entry = entry.next);

            if(entry.key.equals(key)){

                V valueToReturn = entry.value;

                entry.value = value;

                return valueToReturn;
            }else{

                entry.next = new TableEntry<>(key, value);
                return null;
            }
        }
    }

    /**
     * Returs the value associated with the given key or null if there is no such value.
     * Null key values are allowed but always return null.
     * @param key The given key to search for.
     * @return The associated value or null if there is no such value.
     */
    public V get(Object key){

        if(key == null){
            return null;
        }

        int slotId = calculateSlotId(key, table.length);

        TableEntry<K, V> entry = table[slotId];
        for(; entry != null && !entry.key.equals(key); entry = entry.next);

        if(entry != null){
            return entry.value;
        }else{
            return null;
        }
    }

    public int size(){
        return size;
    }

    /**
     * Returns true if the table contains the given key, false otherwise.
     * Always returns null if the given key is null.
     * @param key The key to search for.
     * @return True if the table contains the given key, false otherwise.
     */
    public boolean containsKey(Object key){

        if(key == null){
            return false;
        }

        int slotId = calculateSlotId(key, table.length);

        for(TableEntry<K, V> entry = table[slotId];
            entry != null;
            entry = entry.next){

            if(key.equals(entry.key)){
                return true;
            }

        }

        return false;
    }

    /**
     * Returns true if the table contains the given value, false otherwise.
     * @param value The value to search for.
     * @return True if the table contains the given value, false otherwise.
     */
    public boolean containsValue(Object value){

        for(int slotId = 0; slotId < table.length; slotId++) {

            for (TableEntry<K, V> entry = table[slotId];
                 entry != null;
                 entry = entry.next) {

                if(value == null){

                    if(entry.value == null){
                        return true;
                    }

                }else{

                    if (value.equals(entry.value)) {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * Removes the entry associated with the given key and returns its value
     * or null if the entry does not exist.
     * @param key The key whose entry should be removed.
     * @return The last associated value or null if there is no such value.
     */
    public V remove(Object key){
        if(key == null){
            return null;
        }

        int slotId = calculateSlotId(key, table.length);

        if(table[slotId] == null){
            return null;
        }

        if(key.equals(table[slotId].key)){

            V returnValue = table[slotId].value;
            table[slotId] = table[slotId].next;
            size--;
            modificationCount++;

            return returnValue;
        } else {

            TableEntry<K, V> entry = table[slotId];
            for(; entry.next != null && !key.equals(entry.next.key); entry = entry.next);

            if(entry.next == null){
                return null;
            }else{

                V returnValue = entry.next.value;
                entry.next = entry.next.next;
                size--;
                modificationCount++;

                return returnValue;
            }
        }
    }

    /**
     * Returns true if there are no entries in the table, false otherwise.
     * @return True if there are no entries in the table, false otherwise.
     */
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("[");

        boolean isFirst = true;

        for (var kvTableEntry : table) {
            for (TableEntry<K, V> entry = kvTableEntry;
                 entry != null;
                 entry = entry.next) {

                if (!isFirst) {
                    builder.append(", ");
                } else {
                    isFirst = false;
                }

                builder.append(entry.key)
                        .append("=")
                        .append(entry.value);
            }
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * Returns an array of references to stored key-value pairs.
     * The length of the array returned is equal to the hashtable size.
     * @return An array of references to stored key-value pairs.
     */
    public TableEntry<K, V>[] toArray(){
        // noinspection unchecked
        TableEntry<K, V>[] array = new TableEntry[size];

        int i = 0;
        for (TableEntry<K, V> firstEntry : table) {

            for (TableEntry<K, V> entry = firstEntry;
                 entry != null; entry = entry.next) {
                array[i++] = entry;
            }

        }

        return array;
    }

    /**
     * Removes all of the key-value pairs from the collection.
     */
    public void clear(){

        for(int slotId = 0; slotId < table.length; slotId++){
            table[slotId] = null;
        }

        size = 0;
        modificationCount++;
    }

    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new SimpleHashtableIterator();
    }

    /**
     * Returns the next power of 2 larger or equal to the number given.
     * @param num The number to find the next power of 2 from.
     * @return The next power of 2.
     * @throws IllegalArgumentException If the given number is less than 1.
     */
    private static int getNextPowerOf2(int num) {
        if(num < 1){
            throw new IllegalArgumentException("The number given must be greater than 0.");
        }

        int tmp;
        do{
            tmp = num++;

            while(tmp % 2 == 0) tmp /= 2;

        }while(tmp != 1);

        return num-1;
    }

    /**
     * Calculates the table slot id for the given key by using .hashCode()
     * @param key The key to calculate the table slot id for.
     * @return The table slot id associated with the given key.
     */
    private int calculateSlotId(Object key, int tableLength){
        return Math.abs(key.hashCode()) % tableLength;
    }

    /**
     * Checks if the ratio of the variable size and the internal array capacity is 75% or more
     * and doubles the internal array capacity if that is the case.
     */
    private void adjustTableCapacity() {
        if(((double) size) / table.length < 0.75){
            return;
        }

        int newTableLength = table.length * 2;

        // noinspection unchecked
        TableEntry<K, V>[] newTable = new TableEntry[table.length * 2];
        TableEntry<K, V>[] tableEntries = toArray();

        for(TableEntry<K, V> entry: tableEntries){
            put(entry.key, entry.value, newTable);
        }

        table = newTable;
        modificationCount++;
    }

}
