package org.problems.mlc;

import java.util.TreeMap;

//https://github.com/anomaly2104/multi-level-cache-low-level-system-design/tree/main
public class MainClass {

    public static void main(String[] args) {
        //TODO builder pattern?
        MultiLevelCache<String> cache = new MultiLevelCache<String>(5, new int[]{10, 20, 35, 60, 100}, new int[]{1, 2, 34, 5}, new int[]{1, 2, 34, 5});
        Result result = null;
        addAndWrite(cache, "Dummy", "1");
        addAndWrite(cache, "A", "1");
        addAndWrite(cache, "B", "2");
        getAndWrite(cache, "A");
        addAndWrite(cache, "A", "3");
        addAndWrite(cache, "B", "4");
        getAndWrite(cache, "B");
        addAndWrite(cache, "C", "1");
        addAndWrite(cache, "D", "2");
        getAndWrite(cache, "C");
        addAndWrite(cache, "E", "3");
        addAndWrite(cache, "F", "4");
        getAndWrite(cache, "E");
        MultiLevelCache<Integer> cache2 = new MultiLevelCache<Integer>(5, new int[]{10, 20, 35, 60, 100}, new int[]{1, 2, 34, 5}, new int[]{1, 2, 34, 5});
        addAndWrite(cache, 1, 35);
        getAndWrite(cache, 2);
        getAndWrite(cache, 1);
        MultiLevelCacheWithPluggableStorage<Integer> cache3 = new MultiLevelCacheWithPluggableStorage<Integer>(5, new int[]{10, 20, 35, 60, 100}, new int[]{1, 2, 34, 5}, new int[]{1, 2, 34, 5});
        addAndWrite(cache, 10, 55);
        getAndWrite(cache, 22);
        getAndWrite(cache, 10);
        TreeMap<Integer, Integer> uomToUserMap = new TreeMap<>();
        uomToUserMap.headMap(10, true).values().stream().mapToInt(i -> i.intValue()).toArray();


    }

    private static void addAndWrite(MultiLevelCache cache, String k, String v) {
        Key<String> key = new Key<>(k);
        Value<String> value = new Value<>(v);
        value = cache.add(key, value);
        Double time = value.getTimeToAccess();
        System.out.println("add " + key + "=" + value + ", time=" + time);
    }

    private static void getAndWrite(MultiLevelCache cache, String k) {
        Key<String> key = new Key<>(k);
        Value<String> value = cache.get(key);
        System.out.println("get " + key + "=" + value + ", time=" + value.getTimeToAccess());
    }


    private static void addAndWrite(MultiLevelCache cache, Integer k, Integer v) {
        Key<Integer> key = new Key<>(k);
        Value<Integer> value = new Value<>(v);
        value = cache.add(key, value);
        Double time = value.getTimeToAccess();
        System.out.println("add " + key + "=" + value + ", time=" + time);
    }

    private static void getAndWrite(MultiLevelCache cache, Integer k) {
        Key<Integer> key = new Key<>(k);
        Value<String> value = cache.get(key);
        System.out.println("get " + key + "=" + value + ", time=" + value.getTimeToAccess());
    }
}
