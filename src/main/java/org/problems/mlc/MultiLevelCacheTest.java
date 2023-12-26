package org.problems.mlc;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiLevelCacheTest {

    private MultiLevelCache<String> cache;

    @Before
    public void setUp() {
        cache = new MultiLevelCache<String>(5, new int[]{5, 20, 35, 60, 100}, new int[]{1, 2, 34, 5}, new int[]{1, 2, 34, 5});
    }

    @After
    public void tearDown() {
        cache = null;
    }

    @Test
    public void testStringBasedObjectCaching() {
        Value<String> expected = new Value<>("1");
        Value<String> actual = addAndWrite(cache, "A", "1");
        assertEquals(expected, actual);

        Value<String> expected2 = new Value<>("2");
        Value<String> actual2 = addAndWrite(cache, "B", "2");
        assertEquals(expected2, actual2);


        Value<String> expected3 = new Value<>("1");
        Value<String> actual3 = getAndWrite(cache, "A");
        assertEquals(expected3, actual3);

        addAndWrite(cache, "A", "3");
        addAndWrite(cache, "B", "4");

        Value<String> expected4 = new Value<>("3");
        Value<String> actual4 = getAndWrite(cache, "A");
        assertEquals(expected4, actual4);

        Value<String> expected5 = new Value<>("4");
        Value<String> actual5 = getAndWrite(cache, "B");
        assertEquals(expected5, actual5);

//        MultiLevelCache<Integer> cache2 = new MultiLevelCache<Integer>(5, new int[]{10, 20, 35, 60, 100}, new int[]{1, 2, 34, 5}, new int[]{1, 2, 34, 5});
//        addAndWrite(cache, 1, 35);
//        getAndWrite(cache, 2);
//        getAndWrite(cache, 1);
//        MultiLevelCacheWithPluggableStorage<Integer> cache3 = new MultiLevelCacheWithPluggableStorage<Integer>(5, new int[]{10, 20, 35, 60, 100}, new int[]{1, 2, 34, 5}, new int[]{1, 2, 34, 5});
//        addAndWrite(cache, 10, 55);
//        getAndWrite(cache, 22);
//        getAndWrite(cache, 10);
    }

    @Test
    public void testCacheEviction() {
        addAndWrite(cache, "A", "3");
        addAndWrite(cache, "B", "4");
        addAndWrite(cache, "C", "3");
        addAndWrite(cache, "D", "4");
        addAndWrite(cache, "E", "3");
        getAndWrite(cache, "B");
        getAndWrite(cache, "C");
        getAndWrite(cache, "D");
        getAndWrite(cache, "E");
        Value<String> expected = new Value<>("4");
        Value<String> actual = addAndWrite(cache, "F", "4");
        assertEquals(expected, actual);
        expected = new Value<>(null);
        actual = getAndWrite(cache, "A");
        assertEquals(expected, actual);

    }


    private static Value<String> addAndWrite(MultiLevelCache cache, String k, String v) {
        Key<String> key = new Key<>(k);
        Value<String> value = new Value<>(v);
        value = cache.add(key, value);
        Double time = value.getTimeToAccess();
        System.out.println("add " + key + "=" + value + ", time=" + time);
        return value;
    }

    private static Value<String> getAndWrite(MultiLevelCache cache, String k) {
        Key<String> key = new Key<>(k);
        Value<String> value = cache.get(key);
        System.out.println("get " + key + "=" + value + ", time=" + value.getTimeToAccess());
        return value;
    }


    private static Value<Integer> addAndWrite(MultiLevelCache cache, Integer k, Integer v) {
        Key<Integer> key = new Key<>(k);
        Value<Integer> value = new Value<>(v);
        value = cache.add(key, value);
        Double time = value.getTimeToAccess();
        System.out.println("add " + key + "=" + value + ", time=" + time);
        return value;
    }

    private static Value<String> getAndWrite(MultiLevelCache cache, Integer k) {
        Key<Integer> key = new Key<>(k);
        Value<String> value = cache.get(key);
        System.out.println("get " + key + "=" + value + ", time=" + value.getTimeToAccess());
        return value;
    }

}
