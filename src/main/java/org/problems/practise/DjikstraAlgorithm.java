package org.problems.practise;

import java.util.HashMap;
import java.util.Map;

public class DjikstraAlgorithm {

    public static final String ILLEGAL_ARGUMENTS = "Check values for source and target";

    public DjikstraAlgorithm() {

        adjacencies = new HashMap<Integer, Map<Integer, Integer>>();
        isVisited = new HashMap<Integer, Boolean>();
    }

    private Map<Integer, Map<Integer, Integer>> adjacencies;

    private Map<Integer, Boolean> isVisited;

    public DjikstraAlgorithm withAdjcacency(int source, int target, int value) {
        adjacencies.putIfAbsent(source, new HashMap<Integer, Integer>());
        adjacencies.get(source).put(target, value);
        adjacencies.putIfAbsent(target, new HashMap<Integer, Integer>());
        adjacencies.get(target).put(source, value);
        return this;
    }

    public int getShortestPath(int source, int target) {
        if (isInValid(source) || isInValid(target)) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENTS);
        }
        if (source == target) {
            return 0;
        }
        isVisited.put(source, Boolean.TRUE);
        int min = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> adjacency : adjacencies.get(source).entrySet()) {
            isVisited.putIfAbsent(adjacency.getKey(), Boolean.FALSE);
            if (isVisited.get(adjacency.getKey())) {
                continue;
            } else {
                int thisPathDist = getShortestPath(adjacency.getKey(), target);
                int dist = thisPathDist == Integer.MAX_VALUE ? thisPathDist : thisPathDist + adjacency.getValue();
                if (min > dist) {
                    min = dist;
                }
            }
        }
        isVisited.put(source, Boolean.FALSE);
        return min;
    }

    private boolean isInValid(int source) {
        return !adjacencies.containsKey(source);
    }

    public static void main(String[] args) {
        DjikstraAlgorithm dijkstraAlgorithm = new DjikstraAlgorithm().withAdjcacency(0, 1, 4)
                .withAdjcacency(0, 7, 8).withAdjcacency(1, 7, 11)
                .withAdjcacency(1, 2, 8).withAdjcacency(7, 8, 7)
                .withAdjcacency(2, 8, 2).withAdjcacency(7, 6, 1)
                .withAdjcacency(2, 5, 4).withAdjcacency(6, 5, 2)
                .withAdjcacency(3, 4, 9).withAdjcacency(3, 5, 14)
                .withAdjcacency(5, 4, 10).withAdjcacency(8, 6, 6)
                .withAdjcacency(2, 3, 7);

        System.out.println("Shortest path=" + dijkstraAlgorithm.getShortestPath(0, 4));
        System.out.println("Shortest path=" + dijkstraAlgorithm.getShortestPath(0, 8));

    }
}
