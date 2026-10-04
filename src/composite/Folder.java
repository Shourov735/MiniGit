package composite;

import java.util.ArrayList;
import java.util.List;

public class Folder implements FileSystemComponent {
    private String name;
    private String folderPath;
    private final List<File> files;
    private final List<Folder> folders;

    public Folder(String name) {
        this(name, "");
    }

    public Folder(String name, String folderPath) {
        this.name = name;
        this.folderPath = folderPath == null ? "" : normalize(folderPath);
        this.files = new ArrayList<>();
        this.folders = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getFolderPath() {
        return folderPath;
    }

    @Override
    public String getPath() {
        return folderPath;
    }

    public List<FileSystemComponent> getChildren() {
        List<FileSystemComponent> children = new ArrayList<>();
        children.addAll(files);
        children.addAll(folders);
        return children;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void add(FileSystemComponent component) {
        if (component instanceof File) {
            addFile((File) component);
        } else if (component instanceof Folder) {
            addFolder((Folder) component);
        }
    }

    public boolean remove(FileSystemComponent component) {
        if (component instanceof File) {
            return removeFile((File) component);
        } else if (component instanceof Folder) {
            return removeFolder((Folder) component);
        }
        return false;
    }

    public void addFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        files.add(file);
        refreshPaths(folderPath);
    }

    public void addFolder(Folder folder) {
        if (folder == null) {
            throw new IllegalArgumentException("Folder cannot be null.");
        }
        folders.add(folder);
        refreshPaths(folderPath);
    }

    public boolean removeFile(File file) {
        return files.remove(file);
    }

    public boolean removeFolder(Folder folder) {
        return folders.remove(folder);
    }

    public void addFileAtPath(String path, String content) {
        String target = normalize(path);
        int slash = target.lastIndexOf('/');
        String parentPath = slash < 0 ? "" : target.substring(0, slash);
        String fileName = slash < 0 ? target : target.substring(slash + 1);
        Folder parent = ensureFolderPath(parentPath);
        parent.addFile(new File(fileName, content));
        refreshPaths("");
    }

    public Folder ensureFolderPath(String path) {
        String target = normalize(path);
        if (target.isEmpty()) {
            return this;
        }
        Folder existing = findFolder(target);
        if (existing != null) {
            return existing;
        }
        String[] parts = target.split("/");
        Folder current = this;
        String currentPath = "";
        for (String part : parts) {
            currentPath = currentPath.isEmpty() ? part : currentPath + "/" + part;
            Folder next = findFolder(currentPath);
            if (next == null) {
                next = new Folder(part);
                current.addFolder(next);
            }
            current = next;
        }
        refreshPaths("");
        return current;
    }

    public void updateOrAddFile(String path, String content) {
        File file = findFile(path);
        if (file != null) {
            file.setContent(content);
            return;
        }
        addFileAtPath(path, content);
    }

    public File findFile(String path) {
        String target = normalize(path);
        for (File file : files) {
            if (normalize(file.getFilePath()).equals(target)) {
                return file;
            }
        }
        for (Folder folder : folders) {
            File found = folder.findFile(target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public Folder findFolder(String path) {
        String target = normalize(path);
        if (target.isEmpty()) {
            return this;
        }
        for (Folder folder : folders) {
            if (normalize(folder.getFolderPath()).equals(target)) {
                return folder;
            }
            Folder found = folder.findFolder(target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public Folder findParentFolder(String path) {
        String target = normalize(path);
        int lastSlash = target.lastIndexOf('/');
        String parentPath = lastSlash < 0 ? "" : target.substring(0, lastSlash);
        return findFolder(parentPath);
    }

    public File detachFile(String path) {
        String target = normalize(path);
        for (int i = 0; i < files.size(); i++) {
            if (normalize(files.get(i).getFilePath()).equals(target)) {
                return files.remove(i);
            }
        }
        for (Folder folder : folders) {
            File detached = folder.detachFile(target);
            if (detached != null) {
                return detached;
            }
        }
        return null;
    }

    public Folder detachFolder(String path) {
        String target = normalize(path);
        for (int i = 0; i < folders.size(); i++) {
            Folder folder = folders.get(i);
            if (normalize(folder.getFolderPath()).equals(target)) {
                return folders.remove(i);
            }
        }
        for (Folder folder : folders) {
            Folder detached = folder.detachFolder(target);
            if (detached != null) {
                return detached;
            }
        }
        return null;
    }

    public void refreshPaths(String parentPath) {
        this.folderPath = normalize(parentPath);

        String currentPath = folderPath;
        for (Folder folder : folders) {
            String childPath = currentPath.isEmpty() ? folder.getName() : currentPath + "/" + folder.getName();
            folder.refreshPaths(childPath);
        }

        for (File file : files) {
            String filePath = currentPath.isEmpty() ? file.getName() : currentPath + "/" + file.getName();
            file.setFilePath(filePath);
        }
    }

    @Override
    public Folder deepCopy() {
        Folder copy = new Folder(name, folderPath);
        for (File file : files) {
            copy.files.add(file.deepCopy());
        }
        for (Folder folder : folders) {
            copy.folders.add(folder.deepCopy());
        }
        copy.refreshPaths(copy.folderPath);
        return copy;
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "+ " + name + "/");
        String childIndent = indent + "  ";
        for (File file : files) {
            file.printTree(childIndent);
        }
        for (Folder folder : folders) {
            folder.printTree(childIndent);
        }
    }

    private static String normalize(String path) {
        if (path == null) {
            return "";
        }
        String value = path.trim().replace('\\', '/');
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        while (value.endsWith("/") && !value.isEmpty()) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }
}
