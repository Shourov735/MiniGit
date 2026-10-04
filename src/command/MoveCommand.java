package command;

import composite.File;
import composite.Folder;
import repository.Git;

public class MoveCommand implements Command {
    private final Git git;
    private final String sourcePath;
    private final String destinationFolderPath;
    private String originalPath;
    private boolean file;

    public MoveCommand(Git git, String sourcePath, String destinationFolderPath) {
        this.git = git;
        this.sourcePath = sourcePath;
        this.destinationFolderPath = destinationFolderPath == null ? "" : destinationFolderPath;
    }

    @Override
    public void execute() {
        Folder destination = git.getWorkingFolder().findFolder(destinationFolderPath);
        if (destination == null) {
            throw new IllegalArgumentException("Destination folder not found: " + destinationFolderPath);
        }

        File targetFile = git.getWorkingFolder().findFile(sourcePath);
        if (targetFile != null) {
            originalPath = targetFile.getFilePath();
            File detached = git.getWorkingFolder().detachFile(sourcePath);
            destination.addFile(detached);
            file = true;
            git.getWorkingFolder().refreshPaths("");
            return;
        }

        Folder targetFolder = git.getWorkingFolder().findFolder(sourcePath);
        if (targetFolder == null || targetFolder == git.getWorkingFolder()) {
            throw new IllegalArgumentException("File or folder not found: " + sourcePath);
        }
        if (isInside(targetFolder, destination)) {
            throw new IllegalArgumentException("Cannot move a folder inside itself.");
        }

        originalPath = targetFolder.getFolderPath();
        Folder detached = git.getWorkingFolder().detachFolder(sourcePath);
        destination.addFolder(detached);
        file = false;
        git.getWorkingFolder().refreshPaths("");
    }

    @Override
    public void undo() {
        if (originalPath == null) {
            return;
        }

        int slash = originalPath.lastIndexOf('/');
        String parentPath = slash < 0 ? "" : originalPath.substring(0, slash);
        Folder originalParent = git.getWorkingFolder().findFolder(parentPath);
        if (originalParent == null) {
            return;
        }

        if (file) {
            File moved = git.getWorkingFolder().findFile(currentFilePath());
            if (moved != null) {
                File detached = git.getWorkingFolder().detachFile(currentFilePath());
                originalParent.addFile(detached);
            }
        } else {
            Folder moved = git.getWorkingFolder().findFolder(currentFolderPath());
            if (moved != null) {
                Folder detached = git.getWorkingFolder().detachFolder(currentFolderPath());
                originalParent.addFolder(detached);
            }
        }
        git.getWorkingFolder().refreshPaths("");
    }

    private String currentFilePath() {
        return appendDestinationPath(getNameFromPath(originalPath));
    }

    private String currentFolderPath() {
        return appendDestinationPath(getNameFromPath(originalPath));
    }

    private String appendDestinationPath(String name) {
        String dest = destinationFolderPath == null ? "" : destinationFolderPath.trim().replace('\\', '/');
        while (dest.startsWith("/")) dest = dest.substring(1);
        while (dest.endsWith("/")) dest = dest.substring(0, dest.length() - 1);
        return dest.isEmpty() ? name : dest + "/" + name;
    }

    private String getNameFromPath(String path) {
        int slash = path.lastIndexOf('/');
        return slash < 0 ? path : path.substring(slash + 1);
    }

    private boolean isInside(Folder parent, Folder candidate) {
        String parentPath = parent.getFolderPath();
        String candidatePath = candidate.getFolderPath();
        return !parentPath.isEmpty()
                && (candidatePath.equals(parentPath) || candidatePath.startsWith(parentPath + "/"));
    }
}
