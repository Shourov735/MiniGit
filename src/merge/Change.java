package merge;

public class Change {
    public enum Status {
        ADDED, DELETED, MODIFIED, UNCHANGED, CONFLICT, RESOLVED
    }

    private final String path;
    private final String oldContent;
    private final String newContent;
    private Status status;

    public Change(String path, Status status, String oldContent, String newContent) {
        this.path = path;
        this.status = status;
        this.oldContent = oldContent;
        this.newContent = newContent;
    }

    public String getPath() {
        return path;
    }

    public String getOldContent() {
        return oldContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status + " : " + path;
    }
}
