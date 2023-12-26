package org.problems.fs;

import java.io.FileWriter;

interface BlockAssignmentStrategy {

    public IBlock assign(IFile file, FileWriter writer);

}
