package org.problems.fs;

import java.util.LinkedList;
import java.util.List;

public class DiskBackedFile implements IFile {

    private String fileName;

    private List<IBlock> blocks;

    private IBlock currentBlock;


    public DiskBackedFile(String fileName) {
        blocks = new LinkedList<>();
        currentBlock = DiskBasedFileManager.INSTANCE.allocateBlock(this);
        blocks.add(currentBlock);
    }

    @Override
    public void write(String data) {
        currentBlock.flush(data);
    }

    @Override
    public String read() {
        return "";
    }

    @Override
    public void delete() {
        DiskBasedFileManager.INSTANCE.garbageCollect(this);
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
