package org.problems.company3;

import java.util.*;

public class Memory {

    private final TreeSet<MemoryBlock> freeMemory = new TreeSet<>();

    private final Map<String, List<MemoryBlock>> allocatedMemoryMap = new HashMap<>();

    public Memory(int capacity) {
        freeMemory.add(new MemoryBlock(0, capacity - 1));
    }

    public TreeSet<MemoryBlock> getFreeMemory() {
        return freeMemory;
    }

    public Map<String, List<MemoryBlock>> getAllocatedMemoryMap() {
        return allocatedMemoryMap;
    }
}
