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

    class TestProcessor<T> implements Processor<T> {

        private final Set<T> objectSet;

        public TestProcessor(Set<T> objectSet){
            this.objectSet = objectSet;
        }

        @Override
        public void process(T value) {
            objectSet.add(value);
        }
    }

    /**
     * Generates a new instance of a Collection that contains no elements.
     * @return Empty instance of a Collection
     */
    Collection<String> generateEmptyStringCollection();

    @Test
    default void testIsEmpty(){
        Collection<String>collection = generateEmptyStringCollection();

        assertTrue(collection.isEmpty());

        collection.add("Value");

        assertFalse(collection.isEmpty());
    }

    @Test
    default void testSizeOnCollection(){
        Collection<String> collection = generateEmptyStringCollection();

        assertEquals(0, collection.size());

        collection.add("Value");

        assertEquals(1, collection.size());
    }

    @Test
    default void testAddOnCollection(){
        Collection<String> collection = generateEmptyStringCollection();

        assertEquals(0, collection.size());
        assertFalse(collection.contains("Value0"));

        collection.add("Value0");
        collection.add("Value1");

        assertEquals(2, collection.size());
        assertTrue(collection.contains("Value0"));
    }

    @Test
    default void testContainsOnCollection(){
        Collection<String> collection = generateEmptyStringCollection();

        assertFalse(collection.contains("Value0"));

        collection.add("Value0");

        assertTrue(collection.contains("Value0"));
    }

    @Test
    default void testRemoveByValueOnCollection(){
        Collection<String> collection = generateEmptyStringCollection();
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
        Collection<String> collection = generateEmptyStringCollection();

        String[] strings = new String[] {"Value0", "Value1", "Value2"};

        for(String string: strings){
            collection.add(string);
        }

        assertArrayEquals(strings, collection.toArray());
    }


    @Test
    default void testForEachOnCollection(){
        Collection<String> collection = generateEmptyStringCollection();

        String[] strings = new String[]{"Value0", "Value1", "Value2"};
        Set<String> expectedStringSet = Arrays.stream(strings).collect(Collectors.toSet());

        for(String string: strings) {
            collection.add(string);
        }

        Set<String> stringSet = new HashSet<>();
        collection.forEach(new TestProcessor<>(stringSet));

        assertEquals(expectedStringSet, stringSet);
    }

    @Test
    default void testAddAllOnCollection(){
        Collection<String> collection = generateEmptyStringCollection();
        Collection<String> other = generateEmptyStringCollection();
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
        Collection<String> collection = generateEmptyStringCollection();

        assertThrows(NullPointerException.class, () -> collection.addAll(null));
    }

    @Test
    default void testElementsGetterIterateOverAllElements(){
        Collection<String> collection = generateEmptyStringCollection();
        String[] strings = new String[]{"Value0", "Value1", "Value2"};
        Set<String> stringSet = new HashSet<>();
        Set<String> expectedStringSet = Arrays.stream(strings).collect(Collectors.toSet());
        for(String string: strings) {
            collection.add(string);
        }

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();
        while(elementsGetter.hasNextElement()){
            stringSet.add(elementsGetter.getNextElement());
        }

        assertEquals(expectedStringSet, stringSet);
    }

    @Test
    default void testElementsGetterGetWhenNoElements(){
        Collection<String> collection = generateEmptyStringCollection();

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();

        assertThrows(NoSuchElementException.class, elementsGetter::getNextElement);
    }

    @Test
    default void testElementsGetterHasNextElementWhenNoElements(){
        Collection<String> collection = generateEmptyStringCollection();

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();

        assertFalse(elementsGetter.hasNextElement());
    }

    @Test
    default void testElementsGetterHasNextElementWhenSomeElements(){
        Collection<String> collection = generateEmptyStringCollection();
        collection.add("Value0");

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();

        assertTrue(elementsGetter.hasNextElement());
    }

    @Test
    default void testElementsGetterGetNextElementWhenCollectionChanges(){
        Collection<String> collection = generateEmptyStringCollection();

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();
        collection.add("Value0");

        assertThrows(ConcurrentModificationException.class, elementsGetter::getNextElement);
    }

    @Test
    default void testElementsGetterHasNextElementWhenCollectionChanges(){
        Collection<String> collection = generateEmptyStringCollection();

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();
        collection.add("Value0");

        assertThrows(ConcurrentModificationException.class, elementsGetter::hasNextElement);
    }

    @Test
    default void testElementsGetterProcessRemainingWhenCollectionChanges(){
        Collection<String> collection = generateEmptyStringCollection();

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();
        collection.add("Value0");

        assertThrows(ConcurrentModificationException.class, () -> elementsGetter.processRemaining(null));
    }

    @Test
    default void testElementsGetterProcessRemaining(){
        Set<String> stringSet = new HashSet<>();
        Set<String> expectedStringSet = Set.of("Value1", "Value2");
        Collection<String> collection = generateEmptyStringCollection();
        collection.add("Value0");
        collection.add("Value1");
        collection.add("Value2");

        ElementsGetter<String> elementsGetter = collection.createElementsGetter();
        elementsGetter.getNextElement();
        elementsGetter.processRemaining(new TestProcessor<>(stringSet));

        assertEquals(expectedStringSet, stringSet);
    }

    @Test
    default void testAddAllSatisfying(){
        Tester<String> tester = (s) -> s.startsWith("V");

        Collection<String> collection = generateEmptyStringCollection();
        Collection<String> other = generateEmptyStringCollection();

        other.add("Value0");
        other.add("");
        other.add("0");
        other.add("Value1");

        collection.addAllSatisfying(other, tester);

        assertEquals(2, collection.size());
    }

}
