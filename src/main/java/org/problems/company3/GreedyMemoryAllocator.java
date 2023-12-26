package org.problems.company3;

import java.util.*;

public class GreedyMemoryAllocator implements IMemoryAllocator {

    private Memory memory;

    public GreedyMemoryAllocator(int capacity) {
        memory = new Memory(capacity);
    }

    public static void main(String[] args) throws Exception {
        GreedyMemoryAllocator memoryAllocator = new GreedyMemoryAllocator(1024);
        System.out.println("Free memory=" + memoryAllocator.memory.getFreeMemory().stream().map(b -> b.getSize()).mapToInt(i -> i.intValue()).sum());
        memoryAllocator.allocate("1", 2);
        memoryAllocator.allocate("2", 2);
        memoryAllocator.allocate("3", 3);
        memoryAllocator.allocate("4", 4);
        memoryAllocator.allocate("5", 2);
        System.out.println("Free memory=" + memoryAllocator.memory.getFreeMemory().stream().map(b -> b.getSize()).mapToInt(i -> i.intValue()).sum());
        memoryAllocator.deallocate("2");
        memoryAllocator.deallocate("4");
        memoryAllocator.deallocate("3");
        System.out.println("Free memory=" + memoryAllocator.memory.getFreeMemory().stream().map(b -> b.getSize()).mapToInt(i -> i.intValue()).sum());
        memoryAllocator.deallocate("1");
        memoryAllocator.deallocate("5");
        System.out.println("Free memory=" + memoryAllocator.memory.getFreeMemory().stream().map(b -> b.getSize()).mapToInt(i -> i.intValue()).sum());
    }

    @Override
    public int[] allocate(String uid, int bytes) {
        MemoryBlock key = new MemoryBlock(Integer.MIN_VALUE, Integer.MAX_VALUE);
        NavigableSet<MemoryBlock> avlMem = memory.getFreeMemory().tailSet(key, true);
        if (avlMem.isEmpty()) {
            throw new RuntimeException("Out of memory");
        }
        MemoryBlock allocated = avlMem.first();
        memory.getFreeMemory().remove(allocated);
        MemoryBlock[] split = allocated.split(bytes);

        memory.getFreeMemory().add(split[1]);
        memory.getAllocatedMemoryMap().putIfAbsent(uid, new ArrayList<>());
        memory.getAllocatedMemoryMap().get(uid).add(split[0]);

        int[] range = new int[2];
        range[0] = allocated.getStart();
        range[1] = allocated.getEnd();
        return range;
    }

    @Override
    public void deallocate(String uid) {
        List<MemoryBlock> deallocated = memory.getAllocatedMemoryMap().remove(uid);
        for (MemoryBlock block : deallocated) {
            MemoryBlock merged = block;
            MemoryBlock prev = memory.getFreeMemory().floor(block);
            if (prev != null && prev.getEnd() == block.getStart() - 1) {
                memory.getFreeMemory().remove(prev);
                merged = block.merge(prev);
            }
            MemoryBlock next = memory.getFreeMemory().ceiling(block);
            if (next != null && next.getStart() == block.getEnd() + 1) {
                memory.getFreeMemory().remove(next);
                merged = block.merge(next);
            }
            memory.getFreeMemory().add(merged);

        }

    }

    @Override
    public int[] free() {
        return new int[0];
    }
}
