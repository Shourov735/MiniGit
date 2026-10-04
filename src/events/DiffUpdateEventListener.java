package events;

public class DiffUpdateEventListener implements RepositoryEventListener {
    @Override
    public void onEvent(RepositoryEvent event) {
        if ("COMMIT".equals(event.getType()) || "MERGE".equals(event.getType()) || "RESTORE".equals(event.getType())) {
            System.out.println("[Diff Reaction] Diff and comparison cache updated for " + event.getType() + ".");
        }
    }
}
