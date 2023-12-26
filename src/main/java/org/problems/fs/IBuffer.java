package org.problems.fs;

public interface IBuffer {

    IBlock assignBlock(IFile file);

    void purgeBlocks(IFile file);

    boolean isSpaceLeft();

    String getId();
}
