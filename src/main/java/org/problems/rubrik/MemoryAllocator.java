package org.problems.rubrik;

import com.sun.jdi.VMOutOfMemoryException;

import java.util.ArrayList;
import java.util.List;

public class MemoryAllocator {


    public static final int MIN_MEM_ALLOC = 512;

    private Node root;

    private int index;

    private Node[] memoryBuffer;

    private int BUFFER_SIZE = 4096;


    public MemoryAllocator(int capacity) {

        //initTestData();
        memoryBuffer = new Node[capacity];
        root = buildTree(capacity);
        System.out.println("Tree built");

    }

    private void initTestData() {
        memoryBuffer = new Node[4];

        Node leaf1 = new LeafNode(0, null, null, "xyz");
        Node leaf2 = new LeafNode(1, null, null, "abc");
        Node leaf3 = new LeafNode(2, null, null, "pqr");
        Node leaf4 = new LeafNode(3, null, null, "ghk");

        Node mid1 = new MiddleNode(leaf1, leaf2);
        Node mid2 = new MiddleNode(leaf3, leaf4);

        root = new MiddleNode(mid1, mid2);

        memoryBuffer[0] = leaf1;
        memoryBuffer[1] = leaf2;
        memoryBuffer[2] = leaf3;
        memoryBuffer[3] = leaf4;
    }

    private Node buildTree(int capacity) {
        if (capacity == 1) {
            Node leaf = new LeafNode(0, null, null, "xyz");
            memoryBuffer[index] = leaf;
            return leaf;
        } else {
            return new MiddleNode(buildTree(capacity / 2), buildTree(capacity / 2));
        }
    }

    protected abstract class Node {


        protected Node left;

        protected Node right;

        protected Node(Node left, Node right) {
            this.left = left;
            this.right = right;
        }

        protected abstract int getSize();

        protected abstract Range getIndexRange();

        protected abstract boolean isLeaf();

        protected abstract boolean isFree();

        protected abstract int getFreeMemory();

        protected class Range {
            private int start;
            private int end;

            public Range(int start, int end) {
                this.start = start;
                this.end = end;
            }
        }

    }

    protected class MiddleNode extends Node {

        private int startIndex;

        private int endIndex;

        protected MiddleNode(Node left, Node right) {
            super(left, right);
        }

        @Override
        protected Range getIndexRange() {
            return new Range(left.getIndexRange().start, right.getIndexRange().end);
        }

        @Override
        protected int getSize() {
            return (left == null ? 0 : left.getSize()) + (right == null ? 0 : right.getSize());
        }

        @Override
        protected boolean isLeaf() {
            return false;
        }

        @Override
        public boolean isFree() {
            return (left == null ? false : left.isFree()) || (right == null ? false : right.isFree());
        }

        @Override
        protected int getFreeMemory() {
            return left.getFreeMemory() + right.getFreeMemory();
        }
    }

    protected class LeafNode extends Node {

        private int index;

        private String memoryLocation;

        private int[] offsets = new int[8];

        public LeafNode(int index, Node left, Node right, String loc) {
            super(left, right);
            this.index = index;
            this.memoryLocation = loc;
        }

        @Override
        protected int getSize() {
            return getFreeMemory();
        }

        @Override
        protected Range getIndexRange() {
            return new Range(index, index);
        }

        @Override
        protected int getFreeMemory() {
            int freeMem = 0;
            for (int i = 0; i < offsets.length; i++) {
                if (offsets[i] == 0) {
                    freeMem += MIN_MEM_ALLOC;
                }
            }

            return freeMem;
        }


        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public boolean isFree() {

            for (int i = 0; i < offsets.length; i++) {
                if (offsets[i] == 0) {
                    return true;
                }
            }
            return false;
        }

        public MemoryHandle allocate(int size) {
            int ind = 0;
            int demand = size;
            MemoryHandle memoryHandle = new MemoryHandle();
            memoryHandle.setIndex(index);
            while (ind < offsets.length) {
                if (offsets[ind] == 1) {
                    ind++;
                    continue;
                } else {
                    offsets[ind] = 1;
                    memoryHandle.addOffset(ind);
                }
                ind++;
                demand = demand - MIN_MEM_ALLOC;
                if (demand <= 0) {
                    break;
                }
            }

            return memoryHandle;
        }


    }

    private class MemoryHandle {

        private int index;

        private List<Integer> offsets = new ArrayList<Integer>();

        private int getAllocatedMemory() {
            return offsets.size() * MIN_MEM_ALLOC;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        private void addOffset(int offset) {
            offsets.add(offset);
        }


        @Override
        public String toString() {
            return "MemoryHandle{" +
                    "index=" + index +
                    ", offsets=" + offsets +
                    ", allocated memory=" + getAllocatedMemory() +
                    "}";
        }
    }

    public List<MemoryHandle> alloc(int size) {
        int demand = size;
        Node freeMemorySubTree = find(root, size);
        if (freeMemorySubTree.getSize() < size) {
            throw new VMOutOfMemoryException("No memory");
        }

        Node.Range range = freeMemorySubTree.getIndexRange();
        List<MemoryHandle> memoryHandles = new ArrayList<MemoryHandle>();

        for (int i = range.start; i <= range.end; i++) {
            LeafNode buff = (LeafNode) memoryBuffer[i];
            MemoryHandle handle = buff.allocate(demand);
            memoryHandles.add(handle);
            demand = demand - handle.getAllocatedMemory();
            if (demand <= 0) {
                break;
            }
        }
        return memoryHandles;
    }

    private Node find(Node root, int size) {

        if (root.left.getFreeMemory() >= size && !root.left.isLeaf()) {
            return find(root.left, size);
        } else if (root.right.getFreeMemory() >= size && !root.right.isLeaf()) {
            return find(root.right, size);
        } else {
            return root;
        }
    }

    public static void main(String[] args) {

        MemoryAllocator allocator = new MemoryAllocator(4);
        List<MemoryHandle> handles = allocator.alloc(2048);
        handles.stream().forEach(h -> System.out.println(h.toString()));
        List<MemoryHandle> handles2 = allocator.alloc(8048);
        handles2.stream().forEach(h -> System.out.println(h.toString()));
    }

}

