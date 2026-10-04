package strategy;

import composite.Folder;
import merge.MergeResult;
import prototype.Branch;

public class DestinationPreferredStrategy implements Strategy {
    @Override
    public MergeResult execute(Folder destination, Branch source) {
        return merge.Merge.applyStrategy(destination, source.getProject(), this, "Destination Preferred");
    }

    @Override
    public String resolveConflict(String sourceContent, String destinationContent) {
        return destinationContent;
    }

    @Override
    public boolean isDeleteIfNotInSource() {
        return false;
    }
}
