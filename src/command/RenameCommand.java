package command;

import composite.File;
import composite.Folder;
import repository.Git;

public class RenameCommand implements Command {
    private final Git git;
    private final String path;
    private final String newName;
    private String oldName;
    private boolean file;

    public RenameCommand(Git git, String path, String newName) {
        this.git = git;
        this.path = path;
        this.newName = newName;
    }

    @Override
    public void execute() {
        File targetFile = git.getWorkingFolder().findFile(path);
        if (targetFile != null) {
            oldName = targetFile.getName();
            file = true;
            targetFile.setName(newName);
            git.getWorkingFolder().refreshPaths("");
            return;
        }

        Folder targetFolder = git.getWorkingFolder().findFolder(path);
        if (targetFolder == null || targetFolder == git.getWorkingFolder()) {
            throw new IllegalArgumentException("File or folder not found: " + path);
        }
        oldName = targetFolder.getName();
        file = false;
        targetFolder.setName(newName);
        git.getWorkingFolder().refreshPaths("");
    }

    @Override
    public void undo() {
        if (oldName == null) {
            return;
        }
        if (file) {
            File targetFile = git.getWorkingFolder().findFile(pathAfterRename());
            if (targetFile != null) {
                targetFile.setName(oldName);
            }
        } else {
            Folder targetFolder = git.getWorkingFolder().findFolder(pathAfterRename());
            if (targetFolder != null) {
                targetFolder.setName(oldName);
            }
        }
        git.getWorkingFolder().refreshPaths("");
    }

    private String pathAfterRename() {
        int slash = path.lastIndexOf('/');
        String parent = slash < 0 ? "" : path.substring(0, slash);
        return parent.isEmpty() ? newName : parent + "/" + newName;
    }
}
