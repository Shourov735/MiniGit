package operations;

import composite.File;
import composite.Folder;

import java.util.ArrayList;
import java.util.List;

public class ExtensionSearchOperation implements HierarchyOperation {
    private final String extension;

    public ExtensionSearchOperation(String extension) {
        this.extension = extension.startsWith(".") ? extension : "." + extension;
    }

    @Override
    public Object execute(Folder folder) {
        return search(folder);
    }

    public List<String> search(Folder folder) {
        List<String> result = new ArrayList<>();
        collect(folder, result);
        return result;
    }

    private void collect(Folder folder, List<String> result) {
        for (File file : folder.getFiles()) {
            if (file.getName().endsWith(extension)) {
                result.add(file.getFilePath());
            }
        }
        for (Folder child : folder.getFolders()) {
            collect(child, result);
        }
    }
}
