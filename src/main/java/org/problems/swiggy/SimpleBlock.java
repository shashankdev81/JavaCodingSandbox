package org.problems.swiggy;

import com.sun.jdi.VMOutOfMemoryException;

public class SimpleBlock implements IBlock {

    private int size;

    private StringBuilder builder = new StringBuilder();

    public SimpleBlock(int size) {
        this.size = size;
    }

    @Override
    public boolean write(String content) {
        if (isOverflow(content)) {
            return false;
            //throw new VMOutOfMemoryException("Not enough spcae");
        } else {
            builder.append(content);
            return true;
        }
    }

    @Override
    public String getContent() {
        return builder.toString();
    }

    private boolean isOverflow(String content) {
        return builder.length() + content.length() >= size;
    }

    @Override
    public int getSize() {
        return size;
    }

}
