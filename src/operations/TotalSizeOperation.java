package operations;

import composite.File;
import composite.Folder;

public class TotalSizeOperation implements HierarchyOperation {
    @Override
    public Object execute(Folder folder) {
        long size = 0;
        for (File file : folder.getFiles()) {
            size += file.getContent().length();
        }
        for (Folder child : folder.getFolders()) {
            size += (Long) execute(child);
        }
        return size;
    }
}
