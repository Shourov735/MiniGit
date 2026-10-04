package operations;

import composite.File;
import composite.Folder;

public class FileCountOperation implements HierarchyOperation {
    @Override
    public Object execute(Folder folder) {
        int count = 0;
        for (File file : folder.getFiles()) {
            count++;
        }
        for (Folder child : folder.getFolders()) {
            count += (Integer) execute(child);
        }
        return count;
    }
}
