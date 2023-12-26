package org.problems.fs;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class DiskBasedFileManager implements IFileSystem {

    protected static DiskBasedFileManager INSTANCE = new DiskBasedFileManager();

    private List<DiskBuffer> bufferList = new LinkedList<>();

    protected void createBuffer() throws RuntimeException {
        try {
            bufferList.add(new DiskBuffer(10, 1000));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public IBlock allocateBlock(IFile file) {
        Optional<DiskBuffer> buffer = bufferList.stream().filter(b -> b.isSpaceLeft()).findFirst();
        if (!buffer.isPresent()) {
            createBuffer();
            allocateBlock(file);
        } else {
            return buffer.get().assignBlock(file);
        }
        return null;
    }

    public void garbageCollect(IFile file) {
        bufferList.stream().forEach(b -> b.purgeBlocks(file));

    }

}
