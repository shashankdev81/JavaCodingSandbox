package org.problems.company3;

import java.util.*;

public class MemoryAllocFunction {

    private static final int BLOCK_SIZE = 4096;

    private static final int CHUNK_SIZE = 512;

    private class Block {

        //private String id = UUID.randomUUID().toString();

        private int capacity = BLOCK_SIZE / CHUNK_SIZE;

        private int number;

        private List<Chunk> chunks;

        public Block(int num) {
            this.number = num;
            this.chunks = new LinkedList<Chunk>();
            for (int i = 1; i <= capacity; i++) {
                this.chunks.add(new Chunk(this, false, ((num - 1) * capacity) + i));
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Block block = (Block) o;
            return number == block.number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number);
        }
    }

    private class Chunk {

        private Block block;

        private boolean isAllocated;

        private Integer number;

        public Chunk(Block block, boolean isAllocated, int number) {
            this.block = block;
            this.isAllocated = isAllocated;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Chunk chunk = (Chunk) o;
            return number == chunk.number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number);
        }

        @Override
        public String toString() {
            return "Chunk{" + number +
                    ',' + "Block{" + block.number +
                    '}';
        }
    }

    private Queue<Chunk> availableMemory;

    private class ChunkComparator implements Comparator<Chunk> {

        @Override
        public int compare(Chunk o1, Chunk o2) {
            return o1.number.compareTo(o2.number);
        }
    }

    public MemoryAllocFunction(int size) {
        availableMemory = new PriorityQueue<Chunk>(new ChunkComparator());
        int blocks = size % BLOCK_SIZE == 0 ? size / BLOCK_SIZE : (size / BLOCK_SIZE + 1);
        for (int i = 1; i < blocks; i++) {
            Block block = new Block(i);
            availableMemory.addAll(block.chunks);
        }

    }

    public List<Chunk> malloc(int size) throws Exception {
        int chunks = size % CHUNK_SIZE == 0 ? size / CHUNK_SIZE : (size / CHUNK_SIZE + 1);
        List<Chunk> result = new LinkedList<Chunk>();
        for (int i = 0; i < chunks; i++) {
            if (availableMemory.isEmpty()) {
                throw new Exception("No memory left");
            }
            result.add(availableMemory.poll());
        }

        return result;
    }

    public void free(List<Chunk> allocated) {
        for (Chunk chunk : allocated) {
            availableMemory.add(chunk);
        }
    }

    public int getFreeMemory() {
        return availableMemory.size() * CHUNK_SIZE;
    }

    public static void main(String[] args) throws Exception {
        MemoryAllocFunction memoryAlloc = new MemoryAllocFunction(200000);
        System.out.println("Free memory:" + memoryAlloc.getFreeMemory());
        List<Chunk> chunks1 = memoryAlloc.malloc(500);
        System.out.println("Allocted memory:" + chunks1);
        System.out.println("Free memory:" + memoryAlloc.getFreeMemory());
        List<Chunk> chunks2 = memoryAlloc.malloc(2090);
        System.out.println("Allocted memory:" + chunks2);
        System.out.println("Free memory:" + memoryAlloc.getFreeMemory());
        List<Chunk> chunks3 = memoryAlloc.malloc(5000);
        System.out.println("Allocted memory:" + chunks3);
        System.out.println("Free memory:" + memoryAlloc.getFreeMemory());
        memoryAlloc.free(chunks1);
        memoryAlloc.free(chunks2);
        memoryAlloc.free(chunks3);
        System.out.println("Free memory:" + memoryAlloc.getFreeMemory());

    }
}


