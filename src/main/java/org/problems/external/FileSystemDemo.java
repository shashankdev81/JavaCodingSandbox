package org.problems.external;

import org.problems.fs.IFile;
import org.problems.fs.DiskBackedFile;


public class FileSystemDemo {

    public static void main(String[] args) {
        IFile file = new DiskBackedFile("testFile.txt");
        file.write("test data");

    }
}
