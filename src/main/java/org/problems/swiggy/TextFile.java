package org.problems.swiggy;

import com.sun.jdi.VMOutOfMemoryException;

import java.util.LinkedList;
import java.util.List;

public class TextFile implements IFile {

    private String name;

    private IFileSystem fs;

    private List<IBlock> blocks;

    private int currBlock = 0;

    public TextFile(String fName, IFileSystem fs, IBlock block) {
        this.name = fName;
        blocks = new LinkedList<IBlock>();
        this.fs = fs;
        blocks.add(block);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        return blocks.isEmpty() ? 0 : blocks.size() * blocks.get(0).getSize();
    }

    @Override
    public void write(String content) throws VMOutOfMemoryException {
        if (!blocks.get(currBlock).write(content)) {
            IBlock newBlock = fs.requestMoreSpace(this);
            System.out.println("One more block got allocated for this file:" + name);
            newBlock.write(content);
            blocks.add(newBlock);
            currBlock++;
        }
    }

    @Override
    public void print() {
        StringBuilder builder = new StringBuilder();
        for (IBlock block : blocks) {
            builder.append(block.getContent());
        }
        System.out.println("File content=" + builder.toString());
    }
}
