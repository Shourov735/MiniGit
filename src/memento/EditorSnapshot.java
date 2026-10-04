package memento;

import composite.Folder;

public final class EditorSnapshot {
    private final String content;
    private final Folder projectSnapshot;

    public EditorSnapshot(Folder projectSnapshot) {
        if (projectSnapshot == null) {
            throw new IllegalArgumentException("Project snapshot cannot be null.");
        }
        this.projectSnapshot = projectSnapshot.deepCopy();
        this.content = this.projectSnapshot.toString();
    }

    public EditorSnapshot(String content, Folder projectSnapshot) {
        this.content = content == null ? "" : content;
        this.projectSnapshot = projectSnapshot == null ? new Folder("project") : projectSnapshot.deepCopy();
    }

    public String getContent() {
        return content;
    }

    public Folder getProjectSnapshot() {
        return projectSnapshot.deepCopy();
    }
}
