package command;

import composite.File;
import repository.Git;

public class ModifyCommand implements Command {
    private final Git git;
    private final String filePath;
    private final String newContent;
    private String oldContent;

    public ModifyCommand(Git git, String filePath, String newContent) {
        this.git = git;
        this.filePath = filePath;
        this.newContent = newContent == null ? "" : newContent;
    }

    @Override
    public void execute() {
        File file = git.getWorkingFolder().findFile(filePath);
        if (file == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        oldContent = file.getContent();
        file.setContent(newContent);
    }

    @Override
    public void undo() {
        File file = git.getWorkingFolder().findFile(filePath);
        if (file != null && oldContent != null) {
            file.setContent(oldContent);
        }
    }
}
