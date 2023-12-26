package org.problems.company1;


public interface IFile {

    public String getName();

    public int getSize();

    public void write(String content) throws Exception;

    public void print();

}
