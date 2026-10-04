package prototype;

import composite.Folder;
import repository.Commit;
import repository.Component;

public class Branch implements Prototype, Component {
    private String code;
    private Folder project;
    private Commit head;

    public Branch(String code, Folder project) {
        this(code, project, null);
    }

    public Branch(String code, Folder project, Commit head) {
        this.code = code;
        this.project = project == null ? new Folder("project") : project.deepCopy();
        this.head = head;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Folder getProject() {
        return project;
    }

    public void setProject(Folder project) {
        this.project = project.deepCopy();
        this.project.refreshPaths("");
    }

    public Commit getHead() {
        return head;
    }

    public void setHead(Commit head) {
        this.head = head;
    }

    @Override
    public Prototype clone() {
        return new Branch(code, project.deepCopy(), head);
    }

    public Branch cloneAs(String newCode) {
        Branch copy = new Branch(newCode, project.deepCopy(), head);
        return copy;
    }

    @Override
    public void methods() {
        System.out.println("Branch: " + code);
    }
}
