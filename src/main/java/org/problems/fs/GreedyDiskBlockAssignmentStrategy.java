package org.problems.fs;

import java.io.FileWriter;
import java.util.Map;

class GreedyDiskBlockAssignmentStrategy implements BlockAssignmentStrategy {

    private Map<IFile, IBlock> memBlocks;

    private int CAPACITY;

    private int SIZE;

    public GreedyDiskBlockAssignmentStrategy(Map<IFile, IBlock> blocks, int BLOCK_SIZE, int MAX_BLOCKS) {
        memBlocks = blocks;
        SIZE = BLOCK_SIZE;
        CAPACITY = MAX_BLOCKS;
    }

    @Override
    public IBlock assign(IFile file, FileWriter writer) {
        int startOffset = memBlocks.size() * CAPACITY;
        IBlock block = new DiskBlock(startOffset, writer);
        memBlocks.put(file, new DiskBlock(startOffset, writer));
        return block;
    }
}
