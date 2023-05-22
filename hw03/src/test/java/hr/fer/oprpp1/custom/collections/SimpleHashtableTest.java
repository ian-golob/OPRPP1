package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleHashtableTest {

    @Test
    public void testSimpleHashtableEmptyConstructor(){
        SimpleHashtable<Integer, Integer> table = new SimpleHashtable<>();

        assertEquals(0, table.size());

        table.put(1, 2);
        table.put(2, 1);
        table.put(1, 7);

        assertEquals(2, table.size());
        assertEquals(7, table.get(1));
        assertEquals(7, table.get(1));
    }

    @Test
    public void testSimpleHashtableConstructorWithCapacity(){
        SimpleHashtable<Integer, Integer> table = new SimpleHashtable<>(1);

        assertEquals(0, table.size());

        table.put(1, 2);
        table.put(2, 1);
        table.put(1, 7);
        table.put(3, 7);

        assertEquals(3, table.size());
        assertEquals(7, table.get(1));
        assertEquals(7, table.get(1));
    }

    @Test
    public void testSimpleHashtableConstructorWithInvalidCapacity(){
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<>(0));
    }

    @Test
    public void testPut(){
        SimpleHashtable<String, Number> table = new SimpleHashtable<>(1);

        assertNull(table.put("Value0", Double.valueOf(2.4)));
        assertNull(table.put("Value1", Integer.valueOf(-2)));

        assertEquals(Double.valueOf(2.4), table.put("Value0", null));
        assertEquals(Integer.valueOf(-2), table.put("Value1", Double.valueOf(3.14)));

        assertNull(table.get("Value0"));
        assertEquals(Double.valueOf(3.14), table.get("Value1"));

        assertThrows(NullPointerException.class, () -> table.put(null, 1));
    }

    @Test
    public void testGet(){
        SimpleHashtable<Number, String> table = new SimpleHashtable<>(1);

        assertNull(table.get(2L));
        assertNull(table.get(null));

        table.put(3.14, "Value0");
        table.put(2L, "Value1");
        table.put(2.0, "Value2");

        for(int i = 0; i < 100; i++){
            table.put(i, "Value" + i);
        }

        assertEquals(103, table.size());
        assertEquals("Value0", table.get(3.14));
        assertEquals("Value1", table.get(2L));
        assertEquals("Value2", table.get(2.0));

        for(int i = 0; i < 100; i++){
            assertEquals("Value" + i, table.get(i));
        }

        assertNull(table.get(-1));
        assertNull(table.get(null));
    }

    @Test
    public void testSize(){
        SimpleHashtable<String, String> table = new SimpleHashtable<>(1);

        assertEquals(0, table.size());

        for(int i = 0; i < 100; i++){
            table.put("Value" + i, "Value" + i);
        }

        for(int i = 0; i < 100; i++){
            table.put("Value" + i, "Value" + i * 2);
        }

        assertEquals(100, table.size());
    }

    @Test
    public void testContainsKey(){
        SimpleHashtable<Number, String> table = new SimpleHashtable<>(1);

        for(int i = 0; i < 100; i++){
            table.put(i, "Value" + i);
        }
        for(int i = 0; i < 100; i++){
            table.put(i + 0.1, "Value" + i);
        }

        for(int i = 0; i < 100; i++){
            assertTrue(table.containsKey(i));
        }
        for(int i = 0; i < 100; i++){
            assertTrue(table.containsKey(i + 0.1));
        }
        for(int i = 0; i < 100; i++){
            assertFalse(table.containsKey(i + 0.2));
        }
        assertFalse(table.containsKey(null));
    }

    @Test
    public void testContainsValue(){
        SimpleHashtable<String, Number> table = new SimpleHashtable<>(1);

        for(int i = 0; i < 100; i++){
            table.put("Value" + i, i);
        }
        for(int i = 0; i < 100; i++){
            table.put("FloatingValue" + i, i + 0.1);
        }

        for(int i = 0; i < 100; i++){
            assertTrue(table.containsValue(i));
        }
        for(int i = 0; i < 100; i++){
            assertTrue(table.containsValue(i + 0.1));
        }

        assertFalse(table.containsKey(12.2));
        assertFalse(table.containsValue(null));

        table.put("null", null);

        assertTrue(table.containsValue(null));
    }

    @Test
    public void testRemove(){
        SimpleHashtable<String, String> table = new SimpleHashtable<>(1);

        for(int i = 0; i < 600; i++){
            table.put("Value" + i, "Value" + i);
        }

        table.remove("Value299");

        for(int i = 0; i < 149; i++){
            assertEquals("Value" + i, table.remove("Value" + i));
        }

        for(int i = 0; i < 149; i++){
            assertNull(table.get("Value" + i));
        }

        assertNull(table.remove(null));
        assertNull(table.remove("Value-1"));

        assertEquals(450, table.size());
    }

    @Test
    public void testIsEmpty(){
        SimpleHashtable<Integer, Integer> table = new SimpleHashtable<>(1);

        assertTrue(table.isEmpty());

        table.put(1, 1);

        assertFalse(table.isEmpty());
    }

    @Test
    public void testToString(){
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);

        table.put("Value0", 0);
        table.put("Value1", 1);
        table.put("Value2", 2);

        assertEquals("[Value1=1, Value0=0, Value2=2]", table.toString());
    }

    @Test
    public void testToArray(){
        SimpleHashtable<Double, Integer> table = new SimpleHashtable<>(1);

        for(int i = 0; i < 100; i++){
            table.put(i + 0.1, i);
        }

        SimpleHashtable.TableEntry<Double, Integer>[] array = table.toArray();

        Arrays.sort(array, Comparator.comparingDouble(SimpleHashtable.TableEntry::getKey));

        for(int i = 0; i < 100; i++){
            assertEquals(i + 0.1, array[i].getKey());
            assertEquals(i, array[i].getValue());
        }
    }

    @Test
    public void testClear(){
        SimpleHashtable<Number, Integer> table = new SimpleHashtable<>();

        table.clear();
        assertEquals(0, table.size());

        table.put(5, 5);
        table.put(6.1, 6);

        assertEquals(2, table.size());

        table.clear();

        assertEquals(0, table.size());

        table.put(5, 4);

        assertEquals(1, table.size());
        assertEquals(4, table.get(5));
        assertNull(table.get(6.1));
    }

    @Test
    public void testIteratorNext(){
        SimpleHashtable<Integer, Integer> table = new SimpleHashtable<>();

        table.put(0, 0);
        table.put(1, 1);
        table.put(2, 2);

        Iterator<SimpleHashtable.TableEntry<Integer, Integer>> iterator = table.iterator();

        assertEquals(0, iterator.next().getKey());
        assertEquals(1, iterator.next().getKey());
        assertEquals(2, iterator.next().getKey());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testIteratorRemove(){
        SimpleHashtable<Integer, Integer> table = new SimpleHashtable<>();

        table.put(0, 0);
        table.put(1, 1);
        table.put(2, 2);

        Iterator<SimpleHashtable.TableEntry<Integer, Integer>> iterator = table.iterator();

        assertEquals(0, iterator.next().getKey());
        assertEquals(1, iterator.next().getKey());

        iterator.remove();

        assertThrows(IllegalStateException.class, iterator::remove);

        assertNull(table.get(1));
        assertEquals(0, table.get(0));
    }

    @Test
    public void testIteratorConcurrentModificationException(){
        SimpleHashtable<Integer, Integer> table = new SimpleHashtable<>();

        table.put(0, 0);
        table.put(1, 1);
        table.put(2, 2);

        Iterator<SimpleHashtable.TableEntry<Integer, Integer>> iterator = table.iterator();

        assertEquals(0, iterator.next().getKey());

        table.put(3, 3);

        assertThrows(ConcurrentModificationException.class, iterator::next);
        assertThrows(ConcurrentModificationException.class, iterator::remove);
    }




}
