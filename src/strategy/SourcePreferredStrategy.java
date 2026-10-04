package strategy;

import composite.Folder;
import merge.MergeResult;
import prototype.Branch;

public class SourcePreferredStrategy implements Strategy {
    @Override
    public MergeResult execute(Folder destination, Branch source) {
        return merge.Merge.applyStrategy(destination, source.getProject(), this, "Source Preferred");
    }

    @Override
    public String resolveConflict(String sourceContent, String destinationContent) {
        return sourceContent;
    }

    @Override
    public boolean isDeleteIfNotInSource() {
        return true;
    }
}
