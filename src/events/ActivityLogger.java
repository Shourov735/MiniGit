package events;

import java.util.ArrayList;
import java.util.List;

public class ActivityLogger implements RepositoryEventListener {
    private final List<RepositoryEvent> history = new ArrayList<>();

    @Override
    public void onEvent(RepositoryEvent event) {
        history.add(event);
        System.out.println("[Activity] " + event.getMessage());
    }

    public List<RepositoryEvent> getHistory() {
        return new ArrayList<>(history);
    }

    public void printHistory() {
        System.out.println("\n===== ACTIVITY HISTORY =====");
        if (history.isEmpty()) {
            System.out.println("No repository activity yet.");
        } else {
            for (RepositoryEvent event : history) {
                System.out.println(event);
            }
        }
        System.out.println("============================");
    }
}
