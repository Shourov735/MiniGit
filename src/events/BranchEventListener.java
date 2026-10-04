package events;

public class BranchEventListener implements RepositoryEventListener {
    @Override
    public void onEvent(RepositoryEvent event) {
        if ("BRANCH_CREATED".equals(event.getType()) || "BRANCH_SWITCHED".equals(event.getType())) {
            System.out.println("[Branch Reaction] Branch information refreshed.");
        }
    }
}
