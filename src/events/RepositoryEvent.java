package events;

import java.time.LocalDateTime;

public class RepositoryEvent {
    private final String type;
    private final String message;
    private final LocalDateTime timestamp;

    public RepositoryEvent(String type, String message) {
        this.type = type;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp + " | " + type + " | " + message;
    }
}
