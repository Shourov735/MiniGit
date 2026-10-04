package repository;

import composite.Folder;
import memento.EditorSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public final class Commit {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String id;
    private final String message;
    private final String author;
    private final LocalDateTime timestamp;
    private final Commit parentCommit;
    private final EditorSnapshot snapshot;

    public Commit(String message, String author, Commit parentCommit, Folder project) {
        this.id = "C" + COUNTER.incrementAndGet();
        this.message = message;
        this.author = author;
        this.timestamp = LocalDateTime.now();
        this.parentCommit = parentCommit;
        this.snapshot = new EditorSnapshot(project);
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Commit getParentCommit() {
        return parentCommit;
    }

    public EditorSnapshot getSnapshot() {
        return snapshot;
    }

    public Folder getProjectSnapshot() {
        return snapshot.getProjectSnapshot();
    }

    public String getCode() {
        return id;
    }

    public void print() {
        System.out.println(id + " | " + message + " | " + author + " | " + timestamp.format(FORMATTER));
        if (parentCommit != null) {
            System.out.println("  Parent: " + parentCommit.getId());
        } else {
            System.out.println("  Parent: None");
        }
    }

    @Override
    public String toString() {
        return id + " - " + message + " (" + author + ", " + timestamp.format(FORMATTER) + ")";
    }
}
