package memento;

import java.util.Stack;

public class SnapshotHistory {
    private final Stack<EditorSnapshot> snapshots = new Stack<>();

    public void push(EditorSnapshot snapshot) {
        if (snapshot != null) {
            snapshots.push(snapshot);
        }
    }

    public EditorSnapshot pop() {
        return snapshots.isEmpty() ? null : snapshots.pop();
    }

    public EditorSnapshot peek() {
        return snapshots.isEmpty() ? null : snapshots.peek();
    }

    public boolean isEmpty() {
        return snapshots.isEmpty();
    }

    public int size() {
        return snapshots.size();
    }

    public void clear() {
        snapshots.clear();
    }
}
