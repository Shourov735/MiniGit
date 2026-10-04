package events;

public class MergeEventListener implements RepositoryEventListener {
    @Override
    public void onEvent(RepositoryEvent event) {
        if ("MERGE".equals(event.getType())) {
            System.out.println("[Merge Reaction] Merge/diff information refreshed.");
        }
    }
}
