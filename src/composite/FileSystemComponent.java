package composite;

public interface FileSystemComponent {
    String getName();
    void setName(String name);
    String getPath();
    FileSystemComponent deepCopy();
    void printTree(String indent);
}
