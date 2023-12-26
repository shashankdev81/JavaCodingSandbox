package org.problems.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CloudResourceConsumptionReport {

    private int totalSize = 0;

    private Map<String, Integer> tagToSizeMap = new HashMap<>();

    private TreeMap<Integer, List<String>> sizeToTagMap = new TreeMap<>(Comparator.reverseOrder());

    private File file1 = new File(10,
        Arrays.stream(new String[]{"audio", "video", "movie"}).collect(
            Collectors.toList()));

    private File file2 = new File(20,
        Arrays.stream(new String[]{"image", "large", "jpeg"}).collect(
            Collectors.toList()));

    private File file3 = new File(20,
        Arrays.stream(new String[]{"video", "large", "jpeg"}).collect(
            Collectors.toList()));

    private File file4 = new File(15,
        Arrays.stream(new String[]{"video", "large", "jpeg"}).collect(
            Collectors.toList()));

    public CloudResourceConsumptionReport() {

    }

    //private PriorityQueue<Tag> queue = new PriorityQueue<>((t1, t2) -> t2.totalSize - t1.totalSize);

    public static void main(String[] args) {
        CloudResourceConsumptionReport report = new CloudResourceConsumptionReport();
        report.addFile(report.file1);
        report.addFile(report.file2);
        report.addFile(report.file3);
        report.addFile(report.file4);
        System.out.println("Total file consumption:" + report.getTotalConsumption());
        System.out.println("Top k files are:" + report.getTotalConsumptionByTags(3));

    }

    public int getTotalConsumption() {
        return totalSize;
    }

    public List<List<String>> getTotalConsumptionByTags(int k) {
        int count = 0;
        List<List<String>> results = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> tagEntry : sizeToTagMap.entrySet()) {
            List<String> tags = tagEntry.getValue();
            boolean isDone = false;
            for (String tag : tags) {
                List<String> result = new ArrayList<>();
                result.add(tag);
                result.add(tagEntry.getKey().toString());
                results.add(result);
                count++;
                if (count == k) {
                    isDone = true;
                    break;
                }
            }
            if (isDone) {
                break;
            }
        }
        return results;
    }

    public void addFile(File file) {
        totalSize += file.size;
        for (String tag : file.tags) {
            tagToSizeMap.putIfAbsent(tag, 0);
            int currTagSize = tagToSizeMap.get(tag);
            if (sizeToTagMap.containsKey(currTagSize)) {
                sizeToTagMap.get(currTagSize).remove(tag);
            }
            currTagSize = tagToSizeMap.get(tag) + file.size;
            tagToSizeMap.put(tag, tagToSizeMap.get(tag) + file.size);
            sizeToTagMap.putIfAbsent(currTagSize, new ArrayList<>());
            sizeToTagMap.get(currTagSize).add(tag);
        }
    }

    public void removeFile(File file) {
        totalSize -= file.size;
        for (String tag : file.tags) {
            int currTagSize = tagToSizeMap.get(tag);
            sizeToTagMap.get(currTagSize).remove(tag);
            currTagSize = tagToSizeMap.get(tag) - file.size;
            tagToSizeMap.put(tag, tagToSizeMap.get(tag) - file.size);
            sizeToTagMap.putIfAbsent(currTagSize, new ArrayList<>());
            sizeToTagMap.get(currTagSize).add(tag);
        }
    }

    public class File {

        private int size;
        private List<String> tags;

        public File(int size, List<String> tags) {
            this.size = size;
            this.tags = tags;
        }
    }

    public class Tag {

        private String tag;
        private int totalSize;

        public Tag(String tag, int totalSize) {
            this.tag = tag;
            this.totalSize = totalSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tag tag1 = (Tag) o;
            return Objects.equals(tag, tag1.tag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tag);
        }
    }
}
