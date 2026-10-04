package search;

import composite.File;
import composite.Folder;
import operations.HierarchyOperation;

import java.util.ArrayList;
import java.util.List;

public class SearchFile implements HierarchyOperation {
    private String condition;

    public SearchFile() {
        this("");
    }

    public SearchFile(String condition) {
        this.condition = condition;
    }

    @Override
    public Object execute(Folder folder) {
        return search(folder, condition);
    }

    public List<File> search(Folder folder, String condition) {
        List<File> result = new ArrayList<>();
        String target = condition == null ? "" : condition.toLowerCase();
        collect(folder, target, result);
        return result;
    }

    private void collect(Folder folder, String target, List<File> result) {
        for (File file : folder.getFiles()) {
            if (file.getName().toLowerCase().contains(target)
                    || file.getFilePath().toLowerCase().contains(target)) {
                result.add(file);
            }
        }
        for (Folder child : folder.getFolders()) {
            collect(child, target, result);
        }
    }
}
