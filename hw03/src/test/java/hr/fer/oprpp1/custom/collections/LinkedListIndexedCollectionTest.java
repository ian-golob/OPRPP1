package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LinkedListIndexedCollection.
 *
 * @author Ian Golob
 */
public class LinkedListIndexedCollectionTest implements CollectionTest {

    private LinkedListIndexedCollection<String> listWithData;
    private String[] listWithDataValues;

    private LinkedListIndexedCollection<String> emptyList;

    @Override
    public LinkedListIndexedCollection<String> generateEmptyStringCollection() {
        return new LinkedListIndexedCollection<>();
    }

    @BeforeEach
    public void init(){

        listWithDataValues = new String[] {"Value0", "Value1", "Value2"};

        listWithData = generateEmptyStringCollection();
        for(String value: listWithDataValues){
            listWithData.add(value);
        }

        emptyList = generateEmptyStringCollection();
    }

    @Test
    public void testCreateListWithEmptyConstructorAndAddValue(){
        LinkedListIndexedCollection<String> newList = new LinkedListIndexedCollection<>();
        assertEquals(0, newList.size());

        newList.add("Value");

        assertEquals(1, newList.size());
        assertEquals("Value", newList.get(0));
    }

    @Test
    public void testCreateListWithOtherCollection(){
        LinkedListIndexedCollection<String> newList = new LinkedListIndexedCollection<>(listWithData);

        assertEquals(listWithDataValues.length, newList.size());
        assertEquals(listWithDataValues[0], newList.get(0));
    }

    @Test
    public void testCreateListWithNullOtherCollection(){
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection<>(null));
    }

    @Test
    public void testAddOrder() {
        LinkedListIndexedCollection<String> list = emptyList;

        list.add("Value0");
        list.add("Value1");

        assertEquals(2, list.size());
        assertEquals("Value0", list.get(0));
        assertEquals("Value1", list.get(1));
    }

    @Test
    public void testAddNull() {
        assertThrows(NullPointerException.class, () -> emptyList.add(null));
    }

    @Test
    public void testGetWithValidIndex(){
        assertEquals("Value0", listWithData.get(0));
        assertEquals("Value1", listWithData.get(1));
        assertEquals("Value2", listWithData.get(2));
    }

    @Test
    public void testGetWithIndexTooSmall(){
        assertThrows(IndexOutOfBoundsException.class, () -> listWithData.get(-1));
    }

    @Test
    public void testGetWithIndexTooLarge(){
        assertThrows(IndexOutOfBoundsException.class, () -> listWithData.get(listWithData.size()));
    }

    @Test
    public void testClear(){
        LinkedListIndexedCollection<String> list = listWithData;

        list.clear();

        assertTrue(list.isEmpty());
    }

    @Test
    public void testInsertWithNullValue(){
        LinkedListIndexedCollection<String> list = listWithData;

        assertThrows(NullPointerException.class, () -> list.insert(null,0));
    }

    @Test
    public void testInsertWithIndexTooSmall(){
        LinkedListIndexedCollection<String> list = listWithData;

        assertThrows(IndexOutOfBoundsException.class, () -> list.insert("Value",-1));
    }

    @Test
    public void testInsertWithIndexTooLarge(){
        LinkedListIndexedCollection<String> list = listWithData;

        assertThrows(IndexOutOfBoundsException.class, () -> list.insert("Value",listWithData.size() + 1));
    }

    @Test
    public void testInsertInEmptyList(){
        LinkedListIndexedCollection<String> list = emptyList;

        list.insert("Inserted1",0);
        list.insert("Inserted2",0);
        list.insert("Inserted3",2);
        list.insert("Inserted4",2);

        assertArrayEquals(new Object[]{"Inserted2", "Inserted1", "Inserted4", "Inserted3"}, list.toArray());
    }

    @Test
    public void testIndexOfWithValidValue(){
        LinkedListIndexedCollection<String> list = listWithData;
        list.add("Value0");

        assertEquals(0, list.indexOf("Value0"));
        assertEquals(1, list.indexOf("Value1"));
        assertEquals(2, list.indexOf("Value2"));
        assertEquals(-1, list.indexOf("Other"));
    }

    @Test
    public void testIndexOfWithNullValue(){
        assertEquals(-1, listWithData.indexOf(null));
    }

    @Test
    public void testRemoveByIndexWithInvalidIndex(){
        assertThrows(IndexOutOfBoundsException.class, () -> listWithData.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> listWithData.remove(listWithData.size()));
    }

    @Test
    public void testRemoveByIndexAtTheBeginning(){

        listWithData.remove(0);

        assertEquals(2, listWithData.size());
        assertEquals("Value1", listWithData.get(0));
    }

    @Test
    public void testRemoveByIndexAtTheMiddle(){

        listWithData.remove(1);

        assertEquals(2, listWithData.size());
        assertEquals("Value2", listWithData.get(1));
    }

    @Test
    public void testRemoveByIndexAtTheEnd(){

        listWithData.remove(2);

        assertEquals(2, listWithData.size());
        assertEquals("Value1", listWithData.get(1));
    }

    @Test
    public void testRemoveByIndexWithOneElement(){
        listWithData.remove(1);
        listWithData.remove(0);
        listWithData.remove(0);

        assertTrue(listWithData.isEmpty());
    }

}
