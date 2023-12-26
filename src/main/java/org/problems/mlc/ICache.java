package org.problems.mlc;

public interface ICache<T> {

    public Value<T> add(Key<T> key, Value<T> value);

    public Value<T> get(Key<T> key);

    public Value<T> remove(Key<T> key);

    public boolean clear();
}
