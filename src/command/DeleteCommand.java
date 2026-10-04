package command;

import composite.File;
import composite.Folder;
import repository.Git;

public class DeleteCommand implements Command {
    private final Git git;
    private final String path;
    private int pos;
    private Object deleted;
    private String parentPath;
    private boolean file;

    public DeleteCommand(Git git, String path) {
        this.git = git;
        this.path = path;
    }

    @Override
    public void execute() {
        File targetFile = git.getWorkingFolder().findFile(path);
        if (targetFile != null) {
            parentPath = parentPath(path);
            Folder parent = git.getWorkingFolder().findFolder(parentPath);
            pos = parent.getFiles().indexOf(targetFile);
            deleted = targetFile.deepCopy();
            parent.removeFile(targetFile);
            file = true;
            return;
        }

        Folder targetFolder = git.getWorkingFolder().findFolder(path);
        if (targetFolder == null || targetFolder == git.getWorkingFolder()) {
            throw new IllegalArgumentException("File or folder not found: " + path);
        }
        parentPath = parentPath(path);
        Folder parent = git.getWorkingFolder().findFolder(parentPath);
        pos = parent.getFolders().indexOf(targetFolder);
        deleted = targetFolder.deepCopy();
        parent.removeFolder(targetFolder);
        file = false;
        git.getWorkingFolder().refreshPaths("");
    }

    @Override
    public void undo() {
        Folder parent = git.getWorkingFolder().findFolder(parentPath);
        if (parent == null || deleted == null) {
            return;
        }

        if (file) {
            File copy = ((File) deleted).deepCopy();
            if (pos < 0 || pos > parent.getFiles().size()) {
                parent.addFile(copy);
            } else {
                parent.getFiles().add(pos, copy);
            }
        } else {
            Folder copy = ((Folder) deleted).deepCopy();
            if (pos < 0 || pos > parent.getFolders().size()) {
                parent.addFolder(copy);
            } else {
                parent.getFolders().add(pos, copy);
            }
        }
        git.getWorkingFolder().refreshPaths("");
    }

    private String parentPath(String value) {
        String normalized = value == null ? "" : value.replace('\\', '/');
        int slash = normalized.lastIndexOf('/');
        return slash < 0 ? "" : normalized.substring(0, slash);
    }
}
