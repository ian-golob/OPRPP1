package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest extends CollectionTest{

    private ArrayIndexedCollection array;
    private ArrayIndexedCollection emptyArray;

    @BeforeEach
    public void init(){
        array = new ArrayIndexedCollection();
        array.add("Value0");
        array.add("Value1");
        array.add("Value2");

        emptyArray = new ArrayIndexedCollection();
    }

    @Test
    public void testCreateArrayWithEmptyConstructorAndAddValue(){
        ArrayIndexedCollection array = new ArrayIndexedCollection();
        assertEquals(0, array.size());

        array.add("Value");
        assertEquals(1, array.size());
        assertEquals("Value", array.get(0));
    }

    @Test
    public void testCreateArrayWithInitialCapacityAndAddValue(){
        ArrayIndexedCollection array = new ArrayIndexedCollection(4);
        assertEquals(0, array.size());

        array.add("Value");
        assertEquals(1, array.size());
        assertEquals("Value", array.get(0));
    }

    @Test
    public void testCreateArrayWithInvalidInitialCapacity(){
        assertThrows(IllegalArgumentException.class,() -> new ArrayIndexedCollection(0));
    }

    @Test
    public void testCreateArrayWithOtherCollection(){
        ArrayIndexedCollection other = new ArrayIndexedCollection();
        other.add("Value");

        ArrayIndexedCollection collection = new ArrayIndexedCollection(other);

        assertEquals(1, collection.size());
        assertEquals("Value", collection.get(0));
    }

    @Test
    public void testCreateArrayWithOtherEmptyCollection(){
        ArrayIndexedCollection other = new ArrayIndexedCollection();

        ArrayIndexedCollection collection = new ArrayIndexedCollection(other);

        assertEquals(0, collection.size());
    }

    @Test
    public void testCreateArrayWithNullCollection(){
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
    }

    @Test
    public void testCreateArrayWithOtherCollectionAndLargeInitialCapacity(){
        ArrayIndexedCollection other = array;
        ArrayIndexedCollection collection = new ArrayIndexedCollection(other, 16);

        assertEquals(3, collection.size());
        assertEquals("Value0", collection.get(0));
        assertEquals("Value1", collection.get(1));
        assertEquals("Value2", collection.get(2));
    }

    @Test
    public void testCreateArrayWithOtherCollectionAndSmallInitialCapacity(){
        ArrayIndexedCollection other = array;

        ArrayIndexedCollection collection = new ArrayIndexedCollection(other, 1);

        assertEquals(3, collection.size());
        assertEquals("Value0", collection.get(0));
        assertEquals("Value1", collection.get(1));
        assertEquals("Value2", collection.get(2));
    }

    @Test
    public void testCreateArrayWithInitialCapacityAndInvalidOtherCollection(){
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 16));
    }

    @Test
    public void testCreateArrayWithOtherCollectionAndInvalidInitialCapacity(){
        ArrayIndexedCollection other = new ArrayIndexedCollection();

        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(other, 0));
    }

    @Test
    public void testIsEmpty(){
        super.testIsEmpty(emptyArray);
    }

    @Test
    public void testSize() {
        super.testSizeOnCollection(emptyArray);
    }

    @Test
    public void testAdd() {
        ArrayIndexedCollection array = emptyArray;

        super.testAddOnCollection(array);

        assertEquals(2, array.size());
        assertEquals("Value0", array.get(0));
        assertEquals("Value1", array.get(1));
    }

    @Test
    public void testAddNull() {
        assertThrows(NullPointerException.class, () -> emptyArray.add(null));
    }

    @Test
    public void testContains() {
        super.testContainsOnCollection(emptyArray);
    }

    @Test
    public void testRemoveByValue() {
        super.testRemoveByValueOnCollection(emptyArray);
    }

    @Test
    public void testToArray() {
        super.testToArrayOnCollection(emptyArray);
    }

    @Test
    public void testForEach(){
        super.testForEachOnCollection(emptyArray);
    }

    @Test
    public void testAddAll(){
        super.testAddAllOnCollection(emptyArray, array);
    }

    @Test
    public void testAddAllWithNullOther(){
        super.testAddAllOnCollectionWithNullOther(emptyArray);
    }

    @Test
    public void testGetWithValidIndex(){
        ArrayIndexedCollection array = emptyArray;

        array.add("Value0");

        assertEquals("Value0", array.get(0));
    }

    @Test
    public void testGetWithInvalidIndex(){
        ArrayIndexedCollection array = emptyArray;

        array.add("Value0");

        assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(1));
    }

    @Test
    public void testGetWithEmptyArray() {
        assertThrows(IndexOutOfBoundsException.class, () -> emptyArray.get(0));
    }


    @Test
    public void testClear(){
        array.clear();

        assertEquals(0, array.size());
    }

    @Test
    public void testInsertWithNullValue(){
        assertThrows(NullPointerException.class, () -> array.insert(null, 0));
    }

    @Test
    public void testInsertWithTooSmallIndex(){
        assertThrows(IndexOutOfBoundsException.class, () -> array.insert("Pozdrav", -1));
    }

    @Test
    public void testInsertWithTooLargeIndex(){
        assertThrows(IndexOutOfBoundsException.class, () -> array.insert("Pozdrav", 4));
    }

    @Test
    public void testInsert(){
        array.insert("Inserted",1);

        assertArrayEquals(new Object[]{"Value0", "Inserted", "Value1", "Value2"}, array.toArray());
    }

    @Test
    public void testIndexOfWithNullValue(){
        assertEquals(-1, array.indexOf(null));
    }

    @Test
    public void testIndexOfWithNonExistingValue(){
        assertEquals(-1, array.indexOf("NonExisting"));
    }

    @Test
    public void testIndexOfWithExistingValue(){
        assertEquals(1, array.indexOf("Value1"));
    }


    @Test
    public void testMultipleInserts(){
        for(int i = 0; i < 32; i++){
            emptyArray.insert("Value", 0);
        }

        assertEquals(32, emptyArray.size());
    }

    @Test
    public void testRemoveByIndexWithInvalidIndex(){
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(array.size()));
    }

    @Test
    public void testRemoveByIndex(){
        array.remove(0);

        assertEquals(2, array.size());
        assertEquals("Value1", array.get(0));
    }

}
