package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionTest {

    /**
     * @param collection empty collection
     */
    public void testIsEmpty(Collection collection){
        assertTrue(collection.isEmpty());

        collection.add("Value");

        assertFalse(collection.isEmpty());
    }

    /**
     * @param collection empty collection
     */
    public void testSizeOnCollection(Collection collection){
        assertEquals(0, collection.size());

        collection.add("Value");

        assertEquals(1, collection.size());
    }

    /**
     * @param collection empty collection
     */
    public void testAddOnCollection(Collection collection){
        assertEquals(0, collection.size());
        assertFalse(collection.contains("Value0"));

        collection.add("Value0");
        collection.add("Value1");

        assertEquals(2, collection.size());
        assertTrue(collection.contains("Value0"));
    }

    /**
     * @param collection empty collection
     */
    public void testContainsOnCollection(Collection collection){
        assertFalse(collection.contains("Value0"));

        collection.add("Value0");

        assertTrue(collection.contains("Value0"));
    }


    /**
     * @param collection empty collection
     */
    public void testRemoveByValueOnCollection(Collection collection){
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

    /**
     * @param collection empty collection
     */
    public void testToArrayOnCollection(Collection collection){
        Object[] objects = new Object[] {"Value0", "Value1", "Value2"};

        assertArrayEquals(new Object[]{}, collection.toArray());

        for(Object object: objects){
            collection.add(object);
        }

        assertArrayEquals(objects, collection.toArray() );
    }


    /**
     * @param collection empty collection
     */
    public void testForEachOnCollection(Collection collection){
        assertEquals(0, collection.size());

        Object[] objects = new Object[]{"Value0", "Value1", "Value2"};
        Set<Object> expectedObjectSet = Arrays.stream(objects).collect(Collectors.toSet());

        for(Object object: objects) {
            collection.add(object);
        }

        class TestProcessor extends Processor {

            private final Set<Object> objectSet;

            public TestProcessor(Set<Object> objectSet){
                this.objectSet = objectSet;
            }

            @Override
            public void process(Object value) {
                objectSet.add(value);
            }
        }

        Set<Object> objectSet = new HashSet<>();
        collection.forEach(new TestProcessor(objectSet));

        assertEquals(expectedObjectSet, objectSet);
    }

    /**
     * @param collection empty collection
     * @param other collection to be added to the empty collection
     */
    public void testAddAllOnCollection(Collection collection, Collection other){
        assertEquals(0, collection.size());

        int otherSize = other.size();

        collection.addAll(other);

        assertEquals(otherSize, collection.size());
        assertEquals(otherSize, other.size());
        assertTrue(collection.contains("Value0"));
        assertTrue(collection.contains("Value1"));
        assertTrue(collection.contains("Value2"));
    }

    /**
     * @param collection empty collection
     */
    public void testAddAllOnCollectionWithNullOther(Collection collection){
        assertThrows(NullPointerException.class, () -> collection.addAll(null));
    }

}
