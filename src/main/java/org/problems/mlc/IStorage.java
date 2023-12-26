package org.problems.mlc;

public interface IStorage {
    public void save(Key key, Value value, int level);

    public Value retrieve(Key key, int level);

    public Value purge(Key key, int level);

    public void flush();
}
