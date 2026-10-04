package operations;

import composite.File;
import composite.Folder;

import java.util.LinkedHashMap;
import java.util.Map;

public class FileCollectorOperation implements HierarchyOperation {
    @Override
    public Object execute(Folder folder) {
        return collect(folder);
    }

    public static Map<String, String> collect(Folder folder) {
        Map<String, String> result = new LinkedHashMap<>();
        collect(folder, result);
        return result;
    }

    private static void collect(Folder folder, Map<String, String> result) {
        for (File file : folder.getFiles()) {
            result.put(file.getFilePath(), file.getContent());
        }
        for (Folder child : folder.getFolders()) {
            collect(child, result);
        }
    }
}
