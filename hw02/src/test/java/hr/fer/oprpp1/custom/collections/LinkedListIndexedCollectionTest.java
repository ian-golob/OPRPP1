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

    private LinkedListIndexedCollection listWithData;
    private Object[] listWithDataValues;

    private LinkedListIndexedCollection emptyList;

    @Override
    public LinkedListIndexedCollection generateEmptyCollection() {
        return new LinkedListIndexedCollection();
    }

    @BeforeEach
    public void init(){

        listWithDataValues = new Object[] {"Value0", "Value1", "Value2"};

        listWithData = generateEmptyCollection();
        for(Object value: listWithDataValues){
            listWithData.add(value);
        }

        emptyList = generateEmptyCollection();
    }

    @Test
    public void testCreateListWithEmptyConstructorAndAddValue(){
        LinkedListIndexedCollection newList = new LinkedListIndexedCollection();
        assertEquals(0, newList.size());

        newList.add("Value");

        assertEquals(1, newList.size());
        assertEquals("Value", newList.get(0));
    }

    @Test
    public void testCreateListWithOtherCollection(){
        LinkedListIndexedCollection newList = new LinkedListIndexedCollection(listWithData);

        assertEquals(listWithDataValues.length, newList.size());
        assertEquals(listWithDataValues[0], newList.get(0));
    }

    @Test
    public void testCreateListWithNullOtherCollection(){
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
    }

    @Test
    public void testAddOrder() {
        LinkedListIndexedCollection list = emptyList;

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
        LinkedListIndexedCollection list = listWithData;

        list.clear();

        assertTrue(list.isEmpty());
    }

    @Test
    public void testInsertWithNullValue(){
        LinkedListIndexedCollection list = listWithData;

        assertThrows(NullPointerException.class, () -> list.insert(null,0));
    }

    @Test
    public void testInsertWithIndexTooSmall(){
        LinkedListIndexedCollection list = listWithData;

        assertThrows(IndexOutOfBoundsException.class, () -> list.insert("Value",-1));
    }

    @Test
    public void testInsertWithIndexTooLarge(){
        LinkedListIndexedCollection list = listWithData;

        assertThrows(IndexOutOfBoundsException.class, () -> list.insert("Value",listWithData.size() + 1));
    }

    @Test
    public void testInsertInEmptyList(){
        LinkedListIndexedCollection list = emptyList;

        list.insert("Inserted1",0);
        list.insert("Inserted2",0);
        list.insert("Inserted3",2);
        list.insert("Inserted4",2);

        assertArrayEquals(new Object[]{"Inserted2", "Inserted1", "Inserted4", "Inserted3"}, list.toArray());
    }

    @Test
    public void testIndexOfWithValidValue(){
        LinkedListIndexedCollection list = listWithData;
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
