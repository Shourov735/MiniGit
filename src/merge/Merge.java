package merge;

import composite.File;
import composite.Folder;
import prototype.Branch;
import repository.Component;
import state.Conflict;
import state.NotConflict;
import state.ProjectState;
import strategy.DestinationPreferredStrategy;
import strategy.SourcePreferredStrategy;
import strategy.Strategy;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Merge implements Component {
    private final Strategy strategy;
    private Branch sourceBranch;
    private Branch destinationBranch;

    public Merge(Strategy strategy) {
        this.strategy = strategy;
    }

    public void submit(Branch sourceBranch, Branch destinationBranch) {
        this.sourceBranch = sourceBranch;
        this.destinationBranch = destinationBranch;
    }

    public MergeResult approve() {
        if (sourceBranch == null || destinationBranch == null) {
            throw new IllegalStateException("Source and destination branches must be selected.");
        }
        return strategy.execute(destinationBranch.getProject(), sourceBranch);
    }

    public ProjectState inspect(String sourceContent, String destinationContent, String path) {
        if (sourceContent != null && destinationContent != null && !sourceContent.equals(destinationContent)) {
            return new Conflict(path, sourceContent, destinationContent);
        }
        return new NotConflict();
    }

    public static MergeResult applyStrategy(Folder destination, Folder source, boolean sourcePreferred, String strategyName) {
        Strategy strat = sourcePreferred ? new SourcePreferredStrategy() : new DestinationPreferredStrategy();
        return applyStrategy(destination, source, strat, strategyName);
    }

    public static MergeResult applyStrategy(Folder destination, Folder source, Strategy strategy, String strategyName) {
        Map<String, String> sourceFiles = operations.FileCollectorOperation.collect(source);
        Map<String, String> destinationFiles = operations.FileCollectorOperation.collect(destination);
        Set<String> allPaths = new LinkedHashSet<>();
        allPaths.addAll(sourceFiles.keySet());
        allPaths.addAll(destinationFiles.keySet());

        MergeResult result = new MergeResult(strategyName);

        for (String path : allPaths) {
            boolean inSource = sourceFiles.containsKey(path);
            boolean inDestination = destinationFiles.containsKey(path);

            if (inSource && !inDestination) {
                destination.addFileAtPath(path, sourceFiles.get(path));
                result.addChange(new Change(path, Change.Status.ADDED, null, sourceFiles.get(path)));
            } else if (!inSource) {
                if (strategy != null && strategy.isDeleteIfNotInSource()) {
                    destination.detachFile(path);
                    result.addChange(new Change(path, Change.Status.DELETED, destinationFiles.get(path), null));
                } else {
                    result.addChange(new Change(path, Change.Status.UNCHANGED, destinationFiles.get(path), destinationFiles.get(path)));
                }
            } else if (sourceFiles.get(path).equals(destinationFiles.get(path))) {
                result.addChange(new Change(path, Change.Status.UNCHANGED,
                        destinationFiles.get(path), sourceFiles.get(path)));
            } else {
                ProjectState state = new Conflict(path, sourceFiles.get(path), destinationFiles.get(path));
                if (state.isConflict()) {
                    String resolvedContent = state.resolve(strategy, sourceFiles.get(path), destinationFiles.get(path));
                    destination.updateOrAddFile(path, resolvedContent);
                    result.addChange(new Change(path, Change.Status.CONFLICT,
                            destinationFiles.get(path), sourceFiles.get(path)));
                    result.addChange(new Change(path, Change.Status.RESOLVED,
                            destinationFiles.get(path), resolvedContent));
                    result.addChange(new Change(path, Change.Status.MODIFIED,
                            destinationFiles.get(path), resolvedContent));
                }
            }
        }

        destination.refreshPaths("");
        return result;
    }

    @Override
    public void methods() {
        System.out.println("Merge operation");
    }
}
