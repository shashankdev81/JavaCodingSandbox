package org.problems.fs;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class DiskBlock implements IBlock {

    private int startOffset;

    private int currentOffset;

    private FileWriter writer;

    public DiskBlock(int startOffset, FileWriter writer) {
        this.startOffset = startOffset;
        this.currentOffset = startOffset;
        this.writer = writer;
    }

    public boolean flush(String data) {
        try {
            int length = data.getBytes(StandardCharsets.UTF_8).length;
            //TODO use file channels
            synchronized (writer) {
                writer.write(data, currentOffset, length);
            }
            currentOffset += length;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }
}
