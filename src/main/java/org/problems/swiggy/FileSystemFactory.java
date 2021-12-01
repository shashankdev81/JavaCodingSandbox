package org.problems.swiggy;

import java.util.HashMap;
import java.util.Map;

public class FileSystemFactory {

    private static Map<String, IFileSystem> fileSystemMap = new HashMap<String, IFileSystem>();

    public static IFileSystem createFileSystem(String name, int totalSize, int blockSize) {
        //vary impl
        IFileSystem fs = new SimpleFileSystemImpl(name, totalSize, blockSize);
        return fs;
    }
}
