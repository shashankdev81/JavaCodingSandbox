package org.problems.practise;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class TreeQueries {

    private Node root;

    public TreeQueries() {
        //test data
        Node leaf1 = new Node(1, null, null);
        Node leaf3 = new Node(3, null, null);
        Node leaf5 = new Node(5, null, null);
        Node leaf7 = new Node(7, null, null);
        Node mid2 = new Node(2, leaf1, leaf3);
        Node mid6 = new Node(6, leaf5, leaf7);
        root = new Node(4, mid2, mid6);

    }

    private class Node {

        private int val;

        private Node left;

        private Node right;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return val == node.val;
        }

        @Override
        public int hashCode() {
            return Objects.hash(val);
        }

        public Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    private void addValue(int x, int y, int w) throws NoSuchFieldException {
        Node lcm = findLCM(root, x, y);
        if (lcm.equals(x)) {
            add(lcm, y, w);
        } else if (lcm.equals(y)) {
            add(lcm, x, w);
        } else {
            add(lcm, x, w);
            add(lcm, y, w);
            lcm.val -= w;
        }

    }

    private int findMinValue(int x, int y) throws NoSuchFieldException {
        Queue<Node> path1 = findPath(root, x);
        Queue<Node> path2 = findPath(root, y);

        int min = Integer.MAX_VALUE;
        while (!path1.isEmpty()) {
            Node n = path1.poll();
            if (min > n.val) {
                min = n.val;
            }
        }

        while (!path2.isEmpty()) {
            Node n = path2.poll();
            if (min > n.val) {
                min = n.val;
            }
        }

        return min;

    }

    private void add(Node root, int y, int w) throws NoSuchFieldException {
        root.val += w;
        if (root.val == y) {
            return;
        } else {
            if (exists(root.left, y)) {
                add(root.left, y, w);
            } else if (exists(root.right, y)) {
                add(root.right, y, w);
            }
        }
    }


    private Node findLCM(Node root, int x, int y) throws NoSuchFieldException {
        Queue<Node> path1 = findPath(root, x);
        Queue<Node> path2 = findPath(root, y);
        Node p1 = path1.poll();
        Node p2 = path2.poll();
        Node parent = p1;
        while (p1.equals(p2) && !(path1.isEmpty() || path2.isEmpty())) {
            parent = p1;
            p1 = path1.poll();
            p2 = path2.poll();
        }
        return parent;

    }

    private Queue<Node> findPath(Node root, int target) throws NoSuchFieldException {
        Queue<Node> queue = new ArrayDeque<Node>();
        queue.add(root);
        if (root == null) {
            throw new NoSuchFieldException("Not found:" + target);
        }
        if (root.val > target) {
            queue.addAll(findPath(root.left, target));
        } else if (root.val < target) {
            queue.addAll(findPath(root.right, target));
        } else {
            return queue;
        }
        return queue;
    }

    private boolean exists(Node root, int target) throws NoSuchFieldException {
        if (root == null) {
            return false;
        }
        if (root.val > target) {
            return exists(root.left, target);
        } else if (root.val < target) {
            return exists(root.right, target);
        } else {
            return true;
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        TreeQueries queries = new TreeQueries();
        queries.printLevel();
        queries.addValue(2, 5, 10);
        //queries.addValue(6, 5, 10);
        queries.printLevel();
        //System.out.println("Min value=" + queries.findMinValue(3, 6));
    }

    public void printLevel() {
        Queue<Node> queue = new ArrayDeque<Node>();
        System.out.println("--------------------------");
        queue.add(root);
        while (!queue.isEmpty()) {
            int count = queue.size();
            StringBuilder builder = new StringBuilder();
            while (count > 0) {
                Node curr = queue.poll();
                builder.append(curr.val).append("-");
                if (curr.left != null)
                    queue.add(curr.left);
                if (curr.right != null)
                    queue.add(curr.right);
                count--;
            }
            System.out.println(builder.toString());
        }
        System.out.println("------------------------------------------");
    }

}
