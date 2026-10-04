package composite;

public class File implements FileSystemComponent {
    private String name;
    private String content;
    private String filePath;

    public File(String name, String content) {
        this.name = name;
        this.content = content;
        this.filePath = name;
    }

    public File(String name, String content, String filePath) {
        this.name = name;
        this.content = content;
        this.filePath = filePath;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getPath() {
        return filePath;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public File deepCopy() {
        return new File(name, content, filePath);
    }

    @Override
    public void printTree(String indent) {
        System.out.println(indent + "- " + name + " : " + content);
    }

    @Override
    public String toString() {
        return filePath + " (" + content.length() + " bytes)";
    }
}
