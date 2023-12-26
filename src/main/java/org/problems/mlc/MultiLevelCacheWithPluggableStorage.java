package org.problems.mlc;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiLevelCacheWithPluggableStorage<T> implements ICache<T> {

    private Map<Key, String> keyLock;

    private ILevelAwareEvictionStrategy evictionStrategy;

    private IStorage storage;

    private int[] capacity;

    private int LEVELS;

    public MultiLevelCacheWithPluggableStorage(int cacheLevels, int[] capacity, int[] writeTimes, int[] readTimes) {
        keyLock = new ConcurrentHashMap<>();
        storage = new InMemoryStorage(cacheLevels, capacity);
        this.capacity = capacity;
        evictionStrategy = new LowestHitsEvictionStrategy();
    }

    @Override
    public Value<T> add(Key<T> key, Value<T> value) {
        return add(key, value, LEVELS);
    }

    public Value<T> add(Key<T> key, Value<T> value, int levelsAbove) {
        long start = Instant.now().getNano();
        String lockVal = Thread.currentThread().getName() + "-" + start;
        boolean isLocked = false;
        do {
            isLocked = keyLock.putIfAbsent(key, lockVal) == lockVal;
        } while (!isLocked);
        for (int level = levelsAbove - 1; level >= 0; level--) {
            storage.save(key, value, level);
        }
        keyLock.remove(key);
        evictionStrategy.notifyKeyAccess(key, -1);
        long end = Instant.now().getNano();
        long time = end - start;
        return new Value<T>(value.get(), time);
    }


    @Override
    public Value<T> get(Key<T> key) {
        long start = Instant.now().getNano();
        Value<T> value = null;
        int levelFound = -1;
        for (int level = 0; level < LEVELS; level++) {
            value = storage.retrieve(key, level);
            if (value != null) {
                levelFound = level;
                break;
            }
        }
        if (levelFound != -1) {
            add(key, value, levelFound);
            evictionStrategy.notifyKeyAccess(key, levelFound);
        }
        long end = Instant.now().getNano();
        long time = end - start;
        return new Value<T>(value == null ? null : value.get(), time);
    }

    @Override
    public Value<T> remove(Key<T> key) {
        return purge(key, -1);
    }

    @Override
    public boolean clear() {
        storage.flush();
        return false;
    }

    private Value<T> purge(Key<T> key, int purgeLevel) {
        long start = System.currentTimeMillis();
        String lockVal = Thread.currentThread().getName() + "-" + start;
        boolean isLocked = false;
        Value<T> result = null;
        do {
            isLocked = keyLock.putIfAbsent(key, lockVal) == lockVal;
        } while (!isLocked);
        if (purgeLevel == -1) {
            for (int level = 0; level < LEVELS; level++) {
                Value<T> v = storage.purge(key, level);
                result = v == null ? result : v;
            }
        } else {
            Value<T> v = storage.purge(key, purgeLevel);
            result = v == null ? result : v;
        }
        keyLock.remove(key);
        evictionStrategy.notifyKeyRemoval(key, purgeLevel);
        long end = System.currentTimeMillis();
        long time = end - start;
        return new Value<T>(result.get(), time);
    }

    private class LowestHitsEvictionStrategy<T> implements ILevelAwareEvictionStrategy<T> {

        //TODO bucketise eviction
        Map<Integer, TreeMap<Integer, List<T>>> hitsMap = new HashMap<>();

        public LowestHitsEvictionStrategy() {
            for (int i = 0; i < LEVELS; i++) {
                this.hitsMap.put(i, new TreeMap<>());
            }
        }

        @Override
        public void evict(int level) {
            T evictKey = hitsMap.get(level).firstEntry().getValue().get(0);
            purge(new Key(evictKey), level);
        }

        @Override
        public void notifyKeyAccess(Key<T> key, int level) {
            hitsMap.get(key.hits()).remove(key.hits());
            int hits = key.recordHit() + 1;
            if (level != -1) {
                hitsMap.get(level).putIfAbsent(hits, new ArrayList<>());
                hitsMap.get(level).get(hits).add(key.get());
            } else {
                for (int i = 0; i < LEVELS; i++) {
                    hitsMap.get(i).putIfAbsent(hits, new ArrayList<>());
                    hitsMap.get(i).get(hits).add(key.get());
                }
            }

        }

        @Override
        public void notifyKeyRemoval(Key<T> key, int level) {
            hitsMap.get(level).get(key.hits()).remove(key.get());
        }
    }
}
