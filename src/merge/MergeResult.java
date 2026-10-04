package merge;

import java.util.ArrayList;
import java.util.List;

public class MergeResult {
    private final List<Change> changes = new ArrayList<>();
    private final String strategyName;

    public MergeResult(String strategyName) {
        this.strategyName = strategyName;
    }

    public void addChange(Change change) {
        changes.add(change);
    }

    public List<Change> getChanges() {
        return new ArrayList<>(changes);
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void printResult() {
        System.out.println("\n===== MERGE RESULT =====");
        System.out.println("Strategy: " + strategyName);
        print(Change.Status.ADDED, "Added");
        print(Change.Status.DELETED, "Deleted");
        print(Change.Status.MODIFIED, "Modified");
        print(Change.Status.CONFLICT, "In Conflict");
        print(Change.Status.RESOLVED, "Resolved");
        print(Change.Status.UNCHANGED, "Unchanged");
        System.out.println("========================");
    }

    private void print(Change.Status status, String title) {
        System.out.println(title + ":");
        boolean found = false;
        for (Change change : changes) {
            if (change.getStatus() == status) {
                System.out.println("  - " + change.getPath());
                found = true;
            }
        }
        if (!found) {
            System.out.println("  None");
        }
    }
}
