package org.problems.company1;

public interface IFileSystem {

    public int getSpaceLeft();
    
    public IBlock requestMoreSpace(IFile file) throws Exception;

    public IFile createFile(String name, Class<? extends IFile> fileType);

    public void deleteFile(IFile file);

    public void list();

}
