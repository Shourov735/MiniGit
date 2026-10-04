package repository;

public class CommitSection {
    public String message;
    public String code;
    public String author;

    public CommitSection(String message, String code, String author) {
        this.message = message;
        this.code = code;
        this.author = author;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
