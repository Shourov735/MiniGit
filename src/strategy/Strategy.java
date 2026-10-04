package strategy;

import composite.Folder;
import merge.MergeResult;
import prototype.Branch;

public interface Strategy {
    MergeResult execute(Folder destination, Branch source);

    default String resolveConflict(String sourceContent, String destinationContent) {
        return destinationContent;
    }

    default boolean isDeleteIfNotInSource() {
        return false;
    }
}
