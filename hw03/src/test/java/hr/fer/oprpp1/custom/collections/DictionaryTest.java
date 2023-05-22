package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {

    @Test
    public void testIsEmpty(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        assertTrue(dictionary.isEmpty());

        dictionary.put("Value", 0);

        assertFalse(dictionary.isEmpty());
    }

    @Test
    public void testSize(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        assertEquals(0, dictionary.size());

        dictionary.put("Value0", 0);
        assertEquals(1, dictionary.size());

        dictionary.put("Value0", 0);
        assertEquals(1, dictionary.size());

        dictionary.put("Value1", 1);
        assertEquals(2, dictionary.size());

        dictionary.clear();
        assertEquals(0, dictionary.size());
    }

    @Test
    public void testClear(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("Value0", 0);
        dictionary.put("Value1", 1);

        dictionary.clear();

        assertEquals(0, dictionary.size());
    }

    @Test
    public void testPut(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        assertNull(dictionary.put("Value0", 0));

        assertEquals(0, dictionary.put("Value0", 1));

        assertThrows(NullPointerException.class, () -> dictionary.put(null, 2));

        assertEquals(1, dictionary.get("Value0"));
        assertEquals(1, dictionary.size());
    }

    @Test
    public void testGet(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        dictionary.put("Value0", 0);
        dictionary.put("Value1", 1);

        assertEquals(0, dictionary.get("Value0"));
        assertEquals(1, dictionary.get("Value1"));
        assertNull(dictionary.get("Value2"));
        assertNull(dictionary.get(null));
    }

    @Test
    public void testRemove(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        dictionary.put("Value0", 0);
        dictionary.put("Value1", 1);

        assertEquals(0, dictionary.remove("Value0"));
        assertNull(dictionary.remove("Value0"));

        assertNull(dictionary.remove(null));

        assertEquals(1, dictionary.size());
    }

}
