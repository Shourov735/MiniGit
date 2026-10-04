package events;

public class CommitEventListener implements RepositoryEventListener {
    @Override
    public void onEvent(RepositoryEvent event) {
        if ("COMMIT".equals(event.getType())) {
            System.out.println("[Commit Reaction] Commit history updated.");
        }
    }
}
