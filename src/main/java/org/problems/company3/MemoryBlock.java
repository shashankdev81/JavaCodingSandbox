package org.problems.company3;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MemoryBlock implements Comparable<MemoryBlock> {

    private int start;

    private int end;

    public MemoryBlock(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getSize() {
        return end - start;

    }

    public MemoryBlock[] split(int bytes) {
        MemoryBlock split = new MemoryBlock(start + bytes, end);
        end = start + bytes - 1;
        if (split.start == split.end) {
            split = null;
        }
        return new MemoryBlock[]{this, split};
    }

    public MemoryBlock merge(MemoryBlock block) {
        return new MemoryBlock(Math.min(start, block.start), Math.max(end, block.end));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryBlock that = (MemoryBlock) o;
        return start == that.start && end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public int compareTo(MemoryBlock o) {
        if (getSize() == o.getSize()) {
            return Integer.compare(getStart(), getStart());
        } else {
            return Integer.compare(getSize(), o.getSize());
        }
    }
}
