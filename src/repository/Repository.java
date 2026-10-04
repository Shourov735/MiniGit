package repository;

import command.Command;
import command.CreateCommand;
import command.DeleteCommand;
import command.ModifyCommand;
import command.MoveCommand;
import command.RenameCommand;
import composite.File;
import composite.Folder;
import events.ActivityLogger;
import events.BranchEventListener;
import events.CommitEventListener;
import events.DiffUpdateEventListener;
import events.MergeEventListener;
import events.ProjectViewRefreshListener;
import events.RepositoryEvent;
import events.RepositoryEventListener;
import merge.Change;
import merge.MergeResult;
import memento.EditorSnapshot;
import operations.ExtensionSearchOperation;
import operations.FileCollectorOperation;
import operations.FileCountOperation;
import operations.TotalSizeOperation;
import prototype.Branch;
import prototype.Prototype;
import prototype.PrototypeRegistry;
import search.SearchFile;
import strategy.ConcreteStrategy;
import strategy.DestinationPreferredStrategy;
import strategy.SourcePreferredStrategy;
import strategy.Strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Repository implements RepositoryManagement {
    private final String name;
    private final Git git;
    private final Map<String, Branch> branches = new LinkedHashMap<>();
    private final PrototypeRegistry prototypeRegistry = new PrototypeRegistry();
    private final List<RepositoryEventListener> listeners = new ArrayList<>();
    private final ActivityLogger activityLogger = new ActivityLogger();
    private Strategy defaultStrategy = new ConcreteStrategy(new SourcePreferredStrategy());
    private Branch activeBranch;

    public Repository(String name) {
        this.name = name;
        Folder root = new Folder("project");
        this.git = new Git(root);
        Branch main = new Branch("main", root);
        branches.put("main", main);
        prototypeRegistry.addCode("main", main);
        activeBranch = main;

        listeners.add(new CommitEventListener());
        listeners.add(new BranchEventListener());
        listeners.add(new MergeEventListener());
        listeners.add(new DiffUpdateEventListener());
        listeners.add(new ProjectViewRefreshListener());
        listeners.add(activityLogger);
    }

    public String getName() {
        return name;
    }

    public Branch getActiveBranch() {
        return activeBranch;
    }

    public Git getGit() {
        return git;
    }

    @Override
    public Repository repository() {
        return this;
    }

    public void addListener(RepositoryEventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void notifyEvent(String type, String message) {
        RepositoryEvent event = new RepositoryEvent(type, message);
        for (RepositoryEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void createFile(String parentPath, String name, String content) {
        execute(new CreateCommand(git, parentPath, name, false, content));
        syncActiveBranch();
        notifyEvent("MODIFY", "File created: " + name);
    }

    public void createFolder(String parentPath, String name) {
        execute(new CreateCommand(git, parentPath, name, true, ""));
        syncActiveBranch();
        notifyEvent("MODIFY", "Folder created: " + name);
    }

    public void modify(String filePath, String content) {
        execute(new ModifyCommand(git, filePath, content));
        syncActiveBranch();
        notifyEvent("MODIFY", "File modified: " + filePath);
    }

    public void rename(String path, String newName) {
        execute(new RenameCommand(git, path, newName));
        syncActiveBranch();
        notifyEvent("MODIFY", "Renamed: " + path + " -> " + newName);
    }

    public void move(String sourcePath, String destinationFolderPath) {
        execute(new MoveCommand(git, sourcePath, destinationFolderPath));
        syncActiveBranch();
        notifyEvent("MODIFY", "Moved: " + sourcePath + " -> " + destinationFolderPath);
    }

    public void delete(String path) {
        execute(new DeleteCommand(git, path));
        syncActiveBranch();
        notifyEvent("MODIFY", "Deleted: " + path);
    }

    private void execute(Command command) {
        git.execute(command);
    }

    public Commit commit(String message, String author) {
        Commit parent = activeBranch.getHead();
        Commit commit = new Commit(message, author, parent, git.getWorkingFolder());
        activeBranch.setHead(commit);
        activeBranch.setProject(git.getWorkingFolder());
        notifyEvent("COMMIT", "Commit " + commit.getId() + " created: " + message);
        return commit;
    }

    public List<Commit> getCommitHistory() {
        List<Commit> commits = new ArrayList<>();
        Commit current = activeBranch.getHead();
        while (current != null) {
            commits.add(current);
            current = current.getParentCommit();
        }
        return commits;
    }

    public Commit findCommit(String id) {
        for (Branch branch : branches.values()) {
            Commit current = branch.getHead();
            while (current != null) {
                if (current.getId().equalsIgnoreCase(id)) {
                    return current;
                }
                current = current.getParentCommit();
            }
        }
        return null;
    }

    public void restore(String commitId) {
        Commit commit = findCommit(commitId);
        if (commit == null) {
            throw new IllegalArgumentException("Commit not found: " + commitId);
        }
        EditorSnapshot snapshot = commit.getSnapshot();
        git.setWorkingFolder(snapshot.getProjectSnapshot());
        syncActiveBranch();
        notifyEvent("RESTORE", "Project restored to commit " + commitId);
    }

    public Change[] compareCommits(String olderId, String newerId) {
        Commit older = findCommit(olderId);
        Commit newer = findCommit(newerId);
        if (older == null || newer == null) {
            throw new IllegalArgumentException("Both commits must exist.");
        }

        Map<String, String> oldFiles = FileCollectorOperation.collect(older.getProjectSnapshot());
        Map<String, String> newFiles = FileCollectorOperation.collect(newer.getProjectSnapshot());
        Map<String, Change> changes = new LinkedHashMap<>();

        for (String path : union(oldFiles, newFiles)) {
            boolean oldExists = oldFiles.containsKey(path);
            boolean newExists = newFiles.containsKey(path);
            if (!oldExists) {
                changes.put(path, new Change(path, Change.Status.ADDED, null, newFiles.get(path)));
            } else if (!newExists) {
                changes.put(path, new Change(path, Change.Status.DELETED, oldFiles.get(path), null));
            } else if (!oldFiles.get(path).equals(newFiles.get(path))) {
                changes.put(path, new Change(path, Change.Status.MODIFIED, oldFiles.get(path), newFiles.get(path)));
            } else {
                changes.put(path, new Change(path, Change.Status.UNCHANGED, oldFiles.get(path), newFiles.get(path)));
            }
        }
        return changes.values().toArray(new Change[0]);
    }

    private List<String> union(Map<String, String> first, Map<String, String> second) {
        List<String> paths = new ArrayList<>();
        paths.addAll(first.keySet());
        for (String path : second.keySet()) {
            if (!paths.contains(path)) {
                paths.add(path);
            }
        }
        return paths;
    }

    public void createBranch(String branchName) {
        createBranchFromBranch(branchName, activeBranch.getCode());
    }

    public void createBranchFromBranch(String branchName, String sourceBranchName) {
        if (branches.containsKey(branchName)) {
            throw new IllegalArgumentException("Branch already exists: " + branchName);
        }
        Branch source = branches.get(sourceBranchName);
        if (source == null) {
            throw new IllegalArgumentException("Source branch not found: " + sourceBranchName);
        }
        Prototype clonedPrototype = source.clone();
        Branch clone = ((Branch) clonedPrototype).cloneAs(branchName);
        branches.put(branchName, clone);
        prototypeRegistry.addCode(branchName, clone);
        notifyEvent("BRANCH_CREATED", "Branch created: " + branchName + " from " + sourceBranchName);
    }

    public void createBranchFromCommit(String branchName, String commitId) {
        if (branches.containsKey(branchName)) {
            throw new IllegalArgumentException("Branch already exists: " + branchName);
        }
        Commit commit = findCommit(commitId);
        if (commit == null) {
            throw new IllegalArgumentException("Commit not found: " + commitId);
        }
        Branch branch = new Branch(branchName, commit.getProjectSnapshot(), commit);
        branches.put(branchName, branch);
        prototypeRegistry.addCode(branchName, branch);
        notifyEvent("BRANCH_CREATED", "Branch created: " + branchName + " from " + commitId);
    }

    public void switchBranch(String branchName) {
        Branch target = branches.get(branchName);
        if (target == null) {
            throw new IllegalArgumentException("Branch not found: " + branchName);
        }
        activeBranch.setProject(git.getWorkingFolder());
        activeBranch = target;
        git.setWorkingFolder(target.getProject().deepCopy());
        git.clearCommandHistory();
        notifyEvent("BRANCH_SWITCHED", "Switched to branch: " + branchName);
    }

    public List<String> listBranches() {
        List<String> result = new ArrayList<>();
        for (Branch branch : branches.values()) {
            String marker = branch == activeBranch ? "* " : "  ";
            result.add(marker + branch.getCode());
        }
        return result;
    }

    public MergeResult merge(String sourceBranchName) {
        return merge(sourceBranchName, defaultStrategy);
    }

    public MergeResult merge(String sourceBranchName, Strategy strategy) {
        Branch source = branches.get(sourceBranchName);
        if (source == null) {
            throw new IllegalArgumentException("Source branch not found: " + sourceBranchName);
        }
        if (source == activeBranch) {
            throw new IllegalArgumentException("Cannot merge a branch into itself.");
        }
        Strategy chosen = strategy == null ? defaultStrategy : strategy;
        merge.Merge merge = new merge.Merge(new ConcreteStrategy(chosen));
        merge.submit(source, activeBranch);
        MergeResult result = merge.approve();
        git.setWorkingFolder(activeBranch.getProject().deepCopy());
        syncActiveBranch();
        notifyEvent("MERGE", "Merged branch " + sourceBranchName + " into " + activeBranch.getCode()
                + " using " + chosen.getClass().getSimpleName());
        return result;
    }

    public void setDefaultStrategy(Strategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null.");
        }
        this.defaultStrategy = strategy;
    }

    public Strategy getDefaultStrategy() {
        return defaultStrategy;
    }

    public int countFiles() {
        return (Integer) new FileCountOperation().execute(git.getWorkingFolder());
    }

    public long totalSize() {
        return (Long) new TotalSizeOperation().execute(git.getWorkingFolder());
    }

    public List<String> listByExtension(String extension) {
        return Collections.unmodifiableList(new ExtensionSearchOperation(extension).search(git.getWorkingFolder()));
    }

    public List<File> searchFiles(String condition) {
        return new SearchFile().search(git.getWorkingFolder(), condition);
    }

    public void printProject() {
        System.out.println("\n===== PROJECT: " + name + " | BRANCH: " + activeBranch.getCode() + " =====");
        git.getWorkingFolder().printTree("");
        System.out.println("==========================================");
    }

    public void printCommitHistory() {
        System.out.println("\n===== COMMIT HISTORY: " + activeBranch.getCode() + " =====");
        List<Commit> commits = getCommitHistory();
        if (commits.isEmpty()) {
            System.out.println("No commits yet.");
        } else {
            for (Commit commit : commits) {
                commit.print();
            }
        }
        System.out.println("================================");
    }

    public ActivityLogger getActivityLogger() {
        return activityLogger;
    }

    public PrototypeRegistry getPrototypeRegistry() {
        return prototypeRegistry;
    }

    private void syncActiveBranch() {
        activeBranch.setProject(git.getWorkingFolder());
    }
}
