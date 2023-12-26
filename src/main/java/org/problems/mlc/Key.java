package org.problems.mlc;

import java.util.concurrent.atomic.AtomicInteger;

public class Key<T> {

    private T genericKey;
    private AtomicInteger hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<T> key1 = (Key<T>) o;
        return genericKey.equals(key1.genericKey);
    }

    @Override
    public int hashCode() {
        return genericKey.hashCode();
    }

    public Key(T key) {
        this.genericKey = key;
        this.hits = new AtomicInteger();
    }

    public T get() {
        return genericKey;
    }

    public int hits() {
        return hits.get();
    }

    public int recordHit() {
        return hits.incrementAndGet();
    }


    @Override
    public String toString() {
        return "{" + genericKey + '}';
    }
}
