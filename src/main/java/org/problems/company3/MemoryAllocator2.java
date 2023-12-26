package org.problems.company3;


import java.util.Arrays;

public class MemoryAllocator2 {
    private int[] memory;
    private boolean[] allocated;
    private int size;

    public MemoryAllocator2(int size) {
        this.memory = new int[size];
        this.allocated = new boolean[size];
        this.size = size;
    }

    public int allocate(int blockSize) {
        int start = findFreeBlock(blockSize);
        if (start != -1) {
            Arrays.fill(allocated, start, start + blockSize, true);
        }
        return start;
    }

    public void deallocate(int start, int blockSize) {
        if (isValidBlock(start, blockSize)) {
            Arrays.fill(allocated, start, start + blockSize, false);
        }
    }

    private int findFreeBlock(int blockSize) {
        int consecutiveFreeBlocks = 0;
        for (int i = 0; i < size; i++) {
            if (!allocated[i]) {
                consecutiveFreeBlocks++;
                if (consecutiveFreeBlocks == blockSize) {
                    return i - blockSize + 1;
                }
            } else {
                consecutiveFreeBlocks = 0;
            }
        }
        return -1;
    }

    private boolean isValidBlock(int start, int blockSize) {
        return start >= 0 && start + blockSize <= size;
    }

    public void displayMemoryStatus() {
        for (int i = 0; i < size; i++) {
            System.out.print(allocated[i] ? "X" : "_");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MemoryAllocator2 allocator = new MemoryAllocator2(20);

        int block1 = allocator.allocate(3);
        int block2 = allocator.allocate(4);
        int block3 = allocator.allocate(2);

        allocator.displayMemoryStatus();

        allocator.deallocate(block2, 4);

        allocator.displayMemoryStatus();
    }
}
