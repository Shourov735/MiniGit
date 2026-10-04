package strategy;

import composite.Folder;
import merge.MergeResult;
import prototype.Branch;

public class ConflictMarkerStrategy implements Strategy {
    @Override
    public MergeResult execute(Folder destination, Branch source) {
        return merge.Merge.applyStrategy(destination, source.getProject(), this, "Conflict Marker (Git Style)");
    }
}
