package command;

import composite.File;
import composite.Folder;
import repository.Git;

public class CreateCommand implements Command {
    private final Git git;
    private final String parentPath;
    private final String name;
    private final String content;
    private final boolean folder;
    private Object created;

    public CreateCommand(Git git, String parentPath, String name, boolean folder, String content) {
        this.git = git;
        this.parentPath = parentPath == null ? "" : parentPath;
        this.name = name;
        this.folder = folder;
        this.content = content == null ? "" : content;
    }

    @Override
    public void execute() {
        Folder parent = git.getWorkingFolder().findFolder(parentPath);
        if (parent == null) {
            throw new IllegalArgumentException("Parent folder not found: " + parentPath);
        }
        if (folder) {
            Folder newFolder = new Folder(name);
            parent.addFolder(newFolder);
            created = newFolder;
        } else {
            File newFile = new File(name, content);
            parent.addFile(newFile);
            created = newFile;
        }
        git.getWorkingFolder().refreshPaths("");
    }

    @Override
    public void undo() {
        if (created instanceof File) {
            git.getWorkingFolder().detachFile(((File) created).getFilePath());
        } else if (created instanceof Folder) {
            git.getWorkingFolder().detachFolder(((Folder) created).getFolderPath());
        }
        git.getWorkingFolder().refreshPaths("");
    }
}
