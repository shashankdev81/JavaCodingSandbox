package org.problems.fs;

public class MemoryBlock implements IBlock{
    @Override
    public boolean flush(String data) {
        return false;
    }
}
