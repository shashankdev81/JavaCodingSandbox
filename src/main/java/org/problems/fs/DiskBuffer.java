package org.problems.fs;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


class DiskBuffer implements IBuffer {

    private String id;

    private int BLOCK_SIZE;

    private int MAX_BLOCKS;

    private File file;

    private Map<IFile, IBlock> memoryBlocks;

    private BlockAssignmentStrategy assignmentStrategy;

    private FileWriter writer;

    public DiskBuffer(int blocks, int size) throws IOException {
        id = UUID.randomUUID().toString();
        memoryBlocks = new HashMap<>(blocks);
        BLOCK_SIZE = size;
        MAX_BLOCKS = blocks;
        assignmentStrategy = new GreedyDiskBlockAssignmentStrategy(memoryBlocks, BLOCK_SIZE, MAX_BLOCKS);
        file = new File("/" + id + "/memoryBuffer.txt");
        file.setWritable(true);
        writer = new FileWriter(file);
    }

    @Override
    public IBlock assignBlock(IFile file) {
        return assignmentStrategy.assign(file, writer);
    }

    @Override
    public void purgeBlocks(IFile file) {
        memoryBlocks.remove(file);
    }

    @Override
    public boolean isSpaceLeft() {
        return memoryBlocks.size() < MAX_BLOCKS;

    }

    @Override
    public String getId() {
        return id;
    }
}
