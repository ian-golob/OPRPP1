package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest extends CollectionTest {

    private LinkedListIndexedCollection listWithData;
    private Object[] listWithDataValues;

    private LinkedListIndexedCollection emptyList;

    @BeforeEach
    public void init(){

        listWithDataValues = new Object[] {"Value0", "Value1", "Value2"};

        listWithData = new LinkedListIndexedCollection();
        for(Object value: listWithDataValues){
            listWithData.add(value);
        }

        emptyList = new LinkedListIndexedCollection();
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
    public void testIsEmpty(){
        super.testIsEmpty(emptyList);
    }

    @Test
    public void testSize() {
        super.testSizeOnCollection(emptyList);
    }

    @Test
    public void testAdd() {
        LinkedListIndexedCollection list = emptyList;

        super.testAddOnCollection(list);

        assertEquals(2, list.size());
        assertEquals("Value0", list.get(0));
        assertEquals("Value1", list.get(1));
    }

    @Test
    public void testAddNull() {
        assertThrows(NullPointerException.class, () -> emptyList.add(null));
    }

    @Test
    public void testContains() {
        super.testContainsOnCollection(emptyList);
    }

    @Test
    public void testRemoveByValue() {
        super.testRemoveByValueOnCollection(emptyList);
    }

    @Test
    public void testToArray() {
        super.testToArrayOnCollection(emptyList);
    }

    @Test
    public void testForEach(){
        super.testForEachOnCollection(emptyList);
    }

    @Test
    public void testAddAll(){
        super.testAddAllOnCollection(emptyList, listWithData);
    }

    @Test
    public void testAddAllWithNullOther(){
        super.testAddAllOnCollectionWithNullOther(emptyList);
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
