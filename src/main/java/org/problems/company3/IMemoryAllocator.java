package org.problems.company3;

public interface IMemoryAllocator {

    public int[] allocate(String uid, int bytes);

    public void deallocate(String uid);

    public int[] free();


}
