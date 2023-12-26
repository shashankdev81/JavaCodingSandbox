package org.problems.fs;

interface IFileSystem {

    public IBlock allocateBlock(IFile file);

    public void garbageCollect(IFile file);
}
