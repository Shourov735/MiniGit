package strategy;

import composite.Folder;
import merge.MergeResult;
import prototype.Branch;

/**
 * Context class in the Strategy Pattern for managing and executing merge strategies.
 */
public class MergeContext implements Strategy {
    private Strategy strategy;

    public MergeContext() {
        this(new SourcePreferredStrategy());
    }

    public MergeContext(Strategy strategy) {
        this.strategy = strategy == null ? new SourcePreferredStrategy() : strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy == null ? new SourcePreferredStrategy() : strategy;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    @Override
    public MergeResult execute(Folder destination, Branch source) {
        return strategy.execute(destination, source);
    }

    @Override
    public String resolveConflict(String sourceContent, String destinationContent) {
        return strategy.resolveConflict(sourceContent, destinationContent);
    }

    @Override
    public boolean isDeleteIfNotInSource() {
        return strategy.isDeleteIfNotInSource();
    }
}
