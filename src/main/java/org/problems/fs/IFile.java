package org.problems.fs;

public interface IFile {
    void write(String data);

    String read();

    void delete();

    String getFileName();
}
