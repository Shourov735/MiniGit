package repository;

import command.Command;
import composite.Folder;
import memento.EditorSnapshot;
import memento.SnapshotHistory;

import java.util.ArrayDeque;
import java.util.Deque;

public class Git {
    private Folder folder;
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();
    private final SnapshotHistory history = new SnapshotHistory();

    public Git(Folder folder) {
        this.folder = folder == null ? new Folder("project") : folder;
    }

    public Folder getWorkingFolder() {
        return folder;
    }

    public void setWorkingFolder(Folder folder) {
        this.folder = folder == null ? new Folder("project") : folder;
        undoStack.clear();
        redoStack.clear();
    }

    public void execute(Command command) {
        if (command == null) {
            return;
        }
        history.push(new EditorSnapshot(folder));
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);

        EditorSnapshot snapshot = history.pop();
        if (snapshot != null) {
            folder = snapshot.getProjectSnapshot();
        }
        System.out.println("Undo completed.");
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        history.push(new EditorSnapshot(folder));
        System.out.println("Redo completed.");
    }

    public void clearCommandHistory() {
        undoStack.clear();
        redoStack.clear();
        history.clear();
    }

    public void saveSnapshot() {
        history.push(new EditorSnapshot(folder));
    }

    public EditorSnapshot restoreSnapshot() {
        return history.pop();
    }

    public String execute() {
        return "Git is ready.";
    }

    public void undoStackInfo() {
        System.out.println("Undo operations available: " + undoStack.size());
        System.out.println("Redo operations available: " + redoStack.size());
    }

    public void commit() {
        saveSnapshot();
    }
}
