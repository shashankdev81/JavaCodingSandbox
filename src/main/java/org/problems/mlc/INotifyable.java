package org.problems.mlc;

public interface INotifyable {
    //TODO should eviction strategy expose these abstractions?
    public void notifyKeyAccess(Key key, int level);

    void notifyKeyRemoval(Key key, int level);


}
