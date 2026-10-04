package events;

public class ProjectViewRefreshListener implements RepositoryEventListener {
    @Override
    public void onEvent(RepositoryEvent event) {
        if ("COMMIT".equals(event.getType()) || "RESTORE".equals(event.getType())
                || "BRANCH_SWITCHED".equals(event.getType()) || "MERGE".equals(event.getType())
                || "MODIFY".equals(event.getType())) {
            System.out.println("[View Reaction] Project display refreshed.");
        }
    }
}
