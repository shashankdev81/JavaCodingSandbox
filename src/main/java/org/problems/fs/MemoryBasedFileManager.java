package org.problems.fs;

public class MemoryBasedFileManager implements IFileSystem{
    @Override
    public IBlock allocateBlock(IFile file) {
        return null;
    }

    @Override
    public void garbageCollect(IFile file) {

    }
}
