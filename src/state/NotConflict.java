package state;

import strategy.Strategy;

public class NotConflict extends ProjectState {
    @Override
    public boolean isConflict() {
        return false;
    }

    @Override
    public String resolve(Strategy strategy, String sourceContent, String destinationContent) {
        return destinationContent != null ? destinationContent : sourceContent;
    }

    public void save() {
        System.out.println("No conflict. Change can be saved normally.");
    }
}
