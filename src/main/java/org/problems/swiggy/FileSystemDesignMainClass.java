package org.problems.swiggy;

import com.sun.jdi.VMOutOfMemoryException;

public class FileSystemDesignMainClass {

    public static void main(String[] args) {

        //file system creation
        IFileSystem fs = FileSystemFactory.createFileSystem("shashank-fs", 1000, 100);
        System.out.println("File system Space allocated=" + fs.getSpaceLeft());

        //create file
        IFile accountsTextFile = fs.createFile("accounts-txt-file", TextFile.class);
        System.out.println("File system Space left=" + fs.getSpaceLeft());
        accountsTextFile.write("This is test content1");
        accountsTextFile.write("This is test content2");
        //file got more blocks allocated
        System.out.println("File sze=" + accountsTextFile.getSize());
        accountsTextFile.write("This is test content3");
        accountsTextFile.write("This is test content4");
        accountsTextFile.write("This is test content5");
        //file size
        System.out.println("File sze=" + accountsTextFile.getSize());
        //print contents of file
        accountsTextFile.print();
        //delete file
        fs.deleteFile(accountsTextFile);
        System.out.println("File system Space left=" + fs.getSpaceLeft());

        //create bunch of file file
        IFile textFile1 = fs.createFile("txt-file1", TextFile.class);
        IFile textFile2 = fs.createFile("txt-file2", TextFile.class);
        IFile textFile3 = fs.createFile("txt-file3", TextFile.class);
        //list/print files
        fs.list();

        //out of memory scenario
        IFileSystem smallFS = FileSystemFactory.createFileSystem("small-fs", 50, 25);
        IFile textFile4 = smallFS.createFile("txt-file4", TextFile.class);
        try {
            textFile4.write("This is test content5");
            textFile4.write("This is test content6");
            textFile4.write("This is test content7");
        } catch (VMOutOfMemoryException oom) {
            System.out.println("File system ran out of space:" + oom.getMessage());

        }

    }
}
