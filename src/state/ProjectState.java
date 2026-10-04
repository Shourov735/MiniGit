package state;

import strategy.Strategy;

public abstract class ProjectState {
    public abstract boolean isConflict();
    public abstract String resolve(Strategy strategy, String sourceContent, String destinationContent);
}
