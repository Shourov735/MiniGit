package repository;

import merge.Change;
import merge.MergeResult;
import prototype.Branch;
import strategy.ConflictMarkerStrategy;
import strategy.DestinationPreferredStrategy;
import strategy.SourcePreferredStrategy;
import strategy.Strategy;

import java.util.List;

public class RepositoryManager {
    private Repository repository;

    public Repository createRepository(String name) {
        repository = new Repository(name);
        return repository;
    }

    public Repository getRepository() {
        if (repository == null) {
            throw new IllegalStateException("Repository has not been created.");
        }
        return repository;
    }

    public void createFile(String parentPath, String name, String content) {
        getRepository().createFile(parentPath, name, content);
    }

    public void createFolder(String parentPath, String name) {
        getRepository().createFolder(parentPath, name);
    }

    public void modify(String path, String content) {
        getRepository().modify(path, content);
    }

    public void rename(String path, String newName) {
        getRepository().rename(path, newName);
    }

    public void move(String path, String destination) {
        getRepository().move(path, destination);
    }

    public void delete(String path) {
        getRepository().delete(path);
    }

    public Commit commit(String message, String author) {
        return getRepository().commit(message, author);
    }

    public void createBranch(String name) {
        getRepository().createBranch(name);
    }

    public void createBranchFromBranch(String name, String source) {
        getRepository().createBranchFromBranch(name, source);
    }

    public void createBranchFromCommit(String name, String commitId) {
        getRepository().createBranchFromCommit(name, commitId);
    }

    public void switchBranch(String name) {
        getRepository().switchBranch(name);
    }

    public MergeResult merge(String sourceBranch) {
        return getRepository().merge(sourceBranch);
    }

    public MergeResult merge(String sourceBranch, Strategy strategy) {
        return getRepository().merge(sourceBranch, strategy);
    }

    public Change[] compareCommits(String older, String newer) {
        return getRepository().compareCommits(older, newer);
    }

    public void restore(String commitId) {
        getRepository().restore(commitId);
    }

    public void setDefaultSourcePreferred() {
        getRepository().setDefaultStrategy(new SourcePreferredStrategy());
    }

    public void setDefaultDestinationPreferred() {
        getRepository().setDefaultStrategy(new DestinationPreferredStrategy());
    }

    public void setDefaultConflictMarker() {
        getRepository().setDefaultStrategy(new ConflictMarkerStrategy());
    }

    public int countFiles() {
        return getRepository().countFiles();
    }

    public long totalSize() {
        return getRepository().totalSize();
    }

    public List<String> listByExtension(String extension) {
        return getRepository().listByExtension(extension);
    }

    public List<composite.File> searchFiles(String condition) {
        return getRepository().searchFiles(condition);
    }

    public void undo() {
        getRepository().getGit().undo();
        getRepository().getActiveBranch().setProject(getRepository().getGit().getWorkingFolder());
    }

    public void redo() {
        getRepository().getGit().redo();
        getRepository().getActiveBranch().setProject(getRepository().getGit().getWorkingFolder());
    }

    public void printProject() {
        getRepository().printProject();
    }

    public void printHistory() {
        getRepository().printCommitHistory();
    }

    public void printActivity() {
        getRepository().getActivityLogger().printHistory();
    }

    public void printBranches() {
        System.out.println("\n===== BRANCHES =====");
        for (String branch : getRepository().listBranches()) {
            System.out.println(branch);
        }
        System.out.println("====================");
    }

    public Branch getActiveBranch() {
        return getRepository().getActiveBranch();
    }
}
