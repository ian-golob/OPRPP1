package hr.fer.oprpp1.custom.collections;

/**
 * Represents a collection that stores key-value pairs.
 * Keys can not be null, values can be null.
 *
 * @param <K> Key type.
 * @param <V> Value Type.
 *
 * @author Ian Golob
 */
public class Dictionary<K, V> {

    private final ArrayIndexedCollection<Entry<K,V>> array = new ArrayIndexedCollection<>();

    /**
     * Represents a key-value pair stored in the dictionary.
     * @param <K> Key.
     * @param <V> Value.
     */
    private static class Entry<K, V> {

        private final K key;

        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    /**
     * Returns true if the dictionary is empty, false otherwise.
     * @return True if the dictionary is empty, false otherwise.
     */
    public boolean isEmpty(){
        return array.isEmpty();
    }

    /**
     * Returns the number of key-value pairs stored in the dictionary.
     * @return The number of key-value pairs stored in the dictionary.
     */
    public int size(){
        return array.size();
    }

    /**
     * Removes all the key-value pairs from the dictionary.
     */
    public void clear(){
        array.clear();
    }

    /**
     * Puts the given value at the given key.
     * @param key Key to put the given value at.
     * @param value Value to put at the given key at.
     * @return The last value associated with the given key or null if there was no value associated with the given key.
     * @throws NullPointerException If the given key is null.
     */
    public V put(K key, V value){
        if(key == null){
            throw new NullPointerException("Key must not be null.");
        }

        Entry<K, V> entry = getEntry(key);
        V lastValue = null;

        if(entry != null){
            lastValue = entry.value;
            entry.value = value;
        }else{
            array.add(new Entry<>(key, value));
        }

        return lastValue;
    }

    /**
     * Returns the value stored at the given key.
     * @param key The key to find the value at.
     * @return The value stored at the given key or null if there is no value at the given key.
     */
    public V get(Object key){
        if(key == null){
            return null;
        }

        Entry<K, V> entry = getEntry(key);

        if(entry != null){
            return entry.value;
        }

        return null;
    }

    /**
     * Removes the entry associated with the given key.
     * @param key The key whose entry should be removed.
     * @return The last value associated with the given key or null if there is no value associated with the key.
     */
    public V remove(K key){
        if(key == null){
            return null;
        }

        int index;
        for(index = 0;
            index < array.size() && !array.get(index).key.equals(key);
            index++);

        if(index < array.size()){
            V lastValue = array.get(index).value;
            array.remove(index);

            return lastValue;
        }

        return null;
    }


    /**
     * Searches for the entry with the given key.
     * @param key The key to search for.
     * @return The entry with the given key or null if it does not exist.
     */
    private Entry<K, V> getEntry(Object key){
        if(key == null){
            return null;
        }

        for(int index = 0;index < array.size(); index++){
            Entry<K, V> entry = array.get(index);

            if(entry.key.equals(key)){
                return entry;
            }

        }

        return null;
    }

}
