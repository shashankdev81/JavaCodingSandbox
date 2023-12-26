package org.problems.company1;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleFileSystemImpl implements IFileSystem {

    private Map<IFile, List<IBlock>> filesToBlocks;

    private int blockSize;

    private String name;

    private int totalBlocks;

    private int allocated;

    public SimpleFileSystemImpl(String name, int totalSize, int blockSize) {
        this.name = name;
        this.totalBlocks = totalSize / blockSize;
        this.allocated = totalBlocks;
        this.filesToBlocks = new ConcurrentHashMap<IFile, List<IBlock>>(totalBlocks);
        this.blockSize = blockSize;
    }

    @Override
    public int getSpaceLeft() {
        return totalBlocks * blockSize;
    }

    @Override
    public IBlock requestMoreSpace(IFile file) throws Exception {
        if (totalBlocks == 0) {
            throw new Exception("Ran out of space for this file system!");
        }

        //var impl
        IBlock block = new SimpleBlock(blockSize);
        filesToBlocks.get(file).add(block);
        totalBlocks--;
        return block;
    }

    @Override
    public IFile createFile(String name, Class<? extends IFile> fileType) {
        IBlock block = new SimpleBlock(blockSize);
        IFile file = new TextFile(name, this, block);
        filesToBlocks.putIfAbsent(file, new LinkedList<IBlock>());
        //TODO check thread safety due to concurrent access within a file
        filesToBlocks.get(file).add(block);
        totalBlocks--;
        System.out.println("File create: name=" + name);
        return file;
    }

    @Override
    public void deleteFile(IFile file) {
        List<IBlock> removedBlocks = filesToBlocks.remove(file);
        totalBlocks += removedBlocks.size();
    }

    @Override
    public void list() {
        for (IFile file : filesToBlocks.keySet()) {
            StringBuilder builder = new StringBuilder();
            builder.append("File Name:").append(file.getName()).append(", File Type:").append(file.getClass())
                    .append(",Blocks: ").append(file.getSize());
            System.out.println(builder.toString());
        }
    }
}
