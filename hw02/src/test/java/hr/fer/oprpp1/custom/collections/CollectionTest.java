package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for classes that implement the Collection interface,
 * includes default test implementations.
 *
 * @author Ian Golob
 */
public interface CollectionTest {

    class TestProcessor implements Processor {

        private final Set<Object> objectSet;

        public TestProcessor(Set<Object> objectSet){
            this.objectSet = objectSet;
        }

        @Override
        public void process(Object value) {
            objectSet.add(value);
        }
    }

    /**
     * Generates a new instance of a Collection that contains no elements.
     * @return Empty instance of a Collection
     */
    Collection generateEmptyCollection();

    @Test
    default void testIsEmpty(){
        Collection collection = generateEmptyCollection();

        assertTrue(collection.isEmpty());

        collection.add("Value");

        assertFalse(collection.isEmpty());
    }

    @Test
    default void testSizeOnCollection(){
        Collection collection = generateEmptyCollection();

        assertEquals(0, collection.size());

        collection.add("Value");

        assertEquals(1, collection.size());
    }

    @Test
    default void testAddOnCollection(){
        Collection collection = generateEmptyCollection();

        assertEquals(0, collection.size());
        assertFalse(collection.contains("Value0"));

        collection.add("Value0");
        collection.add("Value1");

        assertEquals(2, collection.size());
        assertTrue(collection.contains("Value0"));
    }

    @Test
    default void testContainsOnCollection(){
        Collection collection = generateEmptyCollection();

        assertFalse(collection.contains("Value0"));

        collection.add("Value0");

        assertTrue(collection.contains("Value0"));
    }

    @Test
    default void testRemoveByValueOnCollection(){
        Collection collection = generateEmptyCollection();
        assertEquals(0, collection.size());

        collection.add("Value0");
        collection.add("Value1");
        collection.add("Value2");
        collection.add("Value3");
        collection.add("Value4");

        assertTrue(collection.remove("Value0"));
        assertFalse(collection.remove("Value0"));
        assertTrue(collection.remove("Value2"));
        assertFalse(collection.remove("Value2"));
        assertTrue(collection.remove("Value4"));
        assertFalse(collection.remove("Value4"));
        assertTrue(collection.remove("Value3"));
        assertFalse(collection.remove("Value3"));
        assertTrue(collection.remove("Value1"));
        assertFalse(collection.remove("Value1"));

        assertEquals(0, collection.size());
    }

    @Test
    default void testToArrayOnCollection(){
        Collection collection = generateEmptyCollection();

        Object[] objects = new Object[] {"Value0", "Value1", "Value2"};

        for(Object object: objects){
            collection.add(object);
        }

        assertArrayEquals(objects, collection.toArray() );
    }


    @Test
    default void testForEachOnCollection(){
        Collection collection = generateEmptyCollection();

        Object[] objects = new Object[]{"Value0", "Value1", "Value2"};
        Set<Object> expectedObjectSet = Arrays.stream(objects).collect(Collectors.toSet());

        for(Object object: objects) {
            collection.add(object);
        }

        Set<Object> objectSet = new HashSet<>();
        collection.forEach(new TestProcessor(objectSet));

        assertEquals(expectedObjectSet, objectSet);
    }

    @Test
    default void testAddAllOnCollection(){
        Collection collection = generateEmptyCollection();
        Collection other = generateEmptyCollection();
        other.add("Value0");
        other.add("Value1");
        other.add("Value2");
        int otherSize = other.size();

        collection.addAll(other);

        assertEquals(otherSize, collection.size());
        assertEquals(otherSize, other.size());
        assertTrue(collection.contains("Value0"));
        assertTrue(collection.contains("Value1"));
        assertTrue(collection.contains("Value2"));
    }

    @Test
    default void testAddAllOnCollectionWithNullOther(){
        Collection collection = generateEmptyCollection();

        assertThrows(NullPointerException.class, () -> collection.addAll(null));
    }

    @Test
    default void testElementsGetterIterateOverAllElements(){
        Collection collection = generateEmptyCollection();
        Object[] objects = new Object[]{"Value0", "Value1", "Value2"};
        Set<Object> objectSet = new HashSet<>();
        Set<Object> expectedObjectSet = Arrays.stream(objects).collect(Collectors.toSet());
        for(Object object: objects) {
            collection.add(object);
        }

        ElementsGetter elementsGetter = collection.createElementsGetter();
        while(elementsGetter.hasNextElement()){
            objectSet.add(elementsGetter.getNextElement());
        }

        assertEquals(expectedObjectSet, objectSet);
    }

    @Test
    default void testElementsGetterGetWhenNoElements(){
        Collection collection = generateEmptyCollection();

        ElementsGetter elementsGetter = collection.createElementsGetter();

        assertThrows(NoSuchElementException.class, elementsGetter::getNextElement);
    }

    @Test
    default void testElementsGetterHasNextElementWhenNoElements(){
        Collection collection = generateEmptyCollection();

        ElementsGetter elementsGetter = collection.createElementsGetter();

        assertFalse(elementsGetter.hasNextElement());
    }

    @Test
    default void testElementsGetterHasNextElementWhenSomeElements(){
        Collection collection = generateEmptyCollection();
        collection.add("Value0");

        ElementsGetter elementsGetter = collection.createElementsGetter();

        assertTrue(elementsGetter.hasNextElement());
    }

    @Test
    default void testElementsGetterGetNextElementWhenCollectionChanges(){
        Collection collection = generateEmptyCollection();

        ElementsGetter elementsGetter = collection.createElementsGetter();
        collection.add("Value0");

        assertThrows(ConcurrentModificationException.class, elementsGetter::getNextElement);
    }

    @Test
    default void testElementsGetterHasNextElementWhenCollectionChanges(){
        Collection collection = generateEmptyCollection();

        ElementsGetter elementsGetter = collection.createElementsGetter();
        collection.add("Value0");

        assertThrows(ConcurrentModificationException.class, elementsGetter::hasNextElement);
    }

    @Test
    default void testElementsGetterProcessRemainingWhenCollectionChanges(){
        Collection collection = generateEmptyCollection();

        ElementsGetter elementsGetter = collection.createElementsGetter();
        collection.add("Value0");

        assertThrows(ConcurrentModificationException.class, () -> elementsGetter.processRemaining(null));
    }

    @Test
    default void testElementsGetterProcessRemaining(){
        Set<Object> objectSet = new HashSet<>();
        Set<Object> expectedObjectSet = Set.of("Value1", "Value2");
        Collection collection = generateEmptyCollection();
        collection.add("Value0");
        collection.add("Value1");
        collection.add("Value2");

        ElementsGetter elementsGetter = collection.createElementsGetter();
        elementsGetter.getNextElement();
        elementsGetter.processRemaining(new TestProcessor(objectSet));

        assertEquals(expectedObjectSet, objectSet);
    }

    @Test
    default void testAddAllSatisfying(){
        Tester tester = (obj) -> obj instanceof String;

        Collection collection = generateEmptyCollection();
        Collection other = generateEmptyCollection();

        other.add("Value0");
        other.add(1);
        other.add(new HashSet<String>());
        other.add("Value1");

        collection.addAllSatisfying(other, tester);

        assertEquals(2, collection.size());
    }

}
