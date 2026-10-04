package state;

import strategy.ConflictMarkerStrategy;
import strategy.SourcePreferredStrategy;
import strategy.Strategy;

public class Conflict extends ProjectState {
    private String path;
    private String sourceContent;
    private String destinationContent;

    public Conflict() {
        this("", "", "");
    }

    public Conflict(String path, String sourceContent, String destinationContent) {
        this.path = path;
        this.sourceContent = sourceContent;
        this.destinationContent = destinationContent;
    }

    @Override
    public boolean isConflict() {
        return true;
    }

    @Override
    public String resolve(Strategy strategy, String sourceContent, String destinationContent) {
        if (strategy != null) {
            return strategy.resolveConflict(sourceContent, destinationContent);
        }
        return destinationContent;
    }

    public void resolve(String selectedContent) {
        if (selectedContent == null) {
            return;
        }
        this.destinationContent = selectedContent;
    }

    public void add(String content) {
        this.sourceContent = content == null ? "" : content;
    }

    public void delete() {
        this.sourceContent = null;
    }

    public String getPath() {
        return path;
    }

    public String getSourceContent() {
        return sourceContent;
    }

    public String getDestinationContent() {
        return destinationContent;
    }
}
