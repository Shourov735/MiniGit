import composite.File;
import merge.Change;
import merge.MergeResult;
import repository.Commit;
import repository.RepositoryManager;
import strategy.ConflictMarkerStrategy;
import strategy.DestinationPreferredStrategy;
import strategy.SourcePreferredStrategy;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RepositoryManager manager = new RepositoryManager();

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("          MINI GIT SYSTEM");
        System.out.println("====================================");

        String repositoryName = read("Enter repository name: ");
        if (repositoryName.isBlank()) {
            repositoryName = "MiniGitProject";
        }
        manager.createRepository(repositoryName);
        System.out.println("Repository created successfully.");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = read("Enter choice: ");
            System.out.println();

            try {
                switch (choice) {
                    case "1" -> createFile();
                    case "2" -> createFolder();
                    case "3" -> modifyFile();
                    case "4" -> rename();
                    case "5" -> move();
                    case "6" -> delete();
                    case "7" -> manager.printProject();
                    case "8" -> commit();
                    case "9" -> manager.printHistory();
                    case "10" -> restore();
                    case "11" -> createBranch();
                    case "12" -> createBranchFromCommit();
                    case "13" -> manager.printBranches();
                    case "14" -> switchBranch();
                    case "15" -> compareCommits();
                    case "16" -> merge();
                    case "17" -> chooseDefaultMergeStrategy();
                    case "18" -> countFiles();
                    case "19" -> totalSize();
                    case "20" -> searchFiles();
                    case "21" -> listByExtension();
                    case "22" -> manager.undo();
                    case "23" -> manager.redo();
                    case "24" -> manager.printActivity();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }

        System.out.println("Thank you for using Mini Git.");
    }

    private static void printMenu() {
        System.out.println("\n====================================");
        System.out.println(" Repository: " + manager.getRepository().getName());
        System.out.println(" Branch:     " + manager.getActiveBranch().getCode());
        System.out.println("====================================");
        System.out.println("1.  Create File");
        System.out.println("2.  Create Folder");
        System.out.println("3.  Modify File");
        System.out.println("4.  Rename File/Folder");
        System.out.println("5.  Move File/Folder");
        System.out.println("6.  Delete File/Folder");
        System.out.println("7.  Show Project Structure");
        System.out.println("8.  Commit Changes");
        System.out.println("9.  View Commit History");
        System.out.println("10. Restore Previous Commit");
        System.out.println("11. Create Branch");
        System.out.println("12. Create Branch From Commit");
        System.out.println("13. View Branches");
        System.out.println("14. Switch Branch");
        System.out.println("15. Compare Two Commits");
        System.out.println("16. Merge Branch");
        System.out.println("17. Set Default Merge Strategy");
        System.out.println("18. Count Files");
        System.out.println("19. Calculate Total File Size");
        System.out.println("20. Search Files");
        System.out.println("21. List Files By Extension");
        System.out.println("22. Undo");
        System.out.println("23. Redo");
        System.out.println("24. View Activity History");
        System.out.println("0.  Exit");
        System.out.println("====================================");
    }

    private static void createFile() {
        String parent = read("Parent folder path (blank for root): ");
        String name = read("File name: ");
        String content = read("File content: ");
        manager.createFile(parent, name, content);
        System.out.println("File created.");
    }

    private static void createFolder() {
        String parent = read("Parent folder path (blank for root): ");
        String name = read("Folder name: ");
        manager.createFolder(parent, name);
        System.out.println("Folder created.");
    }

    private static void modifyFile() {
        String path = read("File path: ");
        String content = read("New content: ");
        manager.modify(path, content);
        System.out.println("File modified.");
    }

    private static void rename() {
        String path = read("File/folder path: ");
        String newName = read("New name: ");
        manager.rename(path, newName);
        System.out.println("Rename completed.");
    }

    private static void move() {
        String source = read("File/folder path to move: ");
        String destination = read("Destination folder path (blank for root): ");
        manager.move(source, destination);
        System.out.println("Move completed.");
    }

    private static void delete() {
        String path = read("File/folder path to delete: ");
        manager.delete(path);
        System.out.println("Delete completed.");
    }

    private static void commit() {
        String message = read("Commit message: ");
        String author = read("Author: ");
        Commit commit = manager.commit(message, author);
        System.out.println("Created commit: " + commit.getId());
    }

    private static void restore() {
        String commitId = read("Commit ID to restore: ");
        manager.restore(commitId);
        System.out.println("Restore completed.");
    }

    private static void createBranch() {
        String name = read("New branch name: ");
        String source = read("Source branch name (blank for active '" + manager.getActiveBranch().getCode() + "'): ");
        if (source.isBlank()) {
            manager.createBranch(name);
        } else {
            manager.createBranchFromBranch(name, source);
        }
        System.out.println("Branch created.");
    }

    private static void createBranchFromCommit() {
        String name = read("New branch name: ");
        String commitId = read("Commit ID: ");
        manager.createBranchFromCommit(name, commitId);
        System.out.println("Branch created from commit.");
    }

    private static void switchBranch() {
        String name = read("Branch name: ");
        manager.switchBranch(name);
        System.out.println("Switched to branch " + name + ".");
    }

    private static void compareCommits() {
        String older = read("Older commit ID: ");
        String newer = read("Newer commit ID: ");
        Change[] changes = manager.compareCommits(older, newer);
        System.out.println("\n===== COMMIT COMPARISON =====");
        for (Change change : changes) {
            System.out.println(change);
        }
        System.out.println("=============================");
    }

    private static void merge() {
        String source = read("Source branch to merge: ");
        System.out.println("1. Use default strategy");
        System.out.println("2. Source Preferred");
        System.out.println("3. Destination Preferred");
        System.out.println("4. Conflict Marker (Git Style)");
        String choice = read("Choose strategy: ");

        MergeResult result;
        if ("2".equals(choice)) {
            result = manager.merge(source, new SourcePreferredStrategy());
        } else if ("3".equals(choice)) {
            result = manager.merge(source, new DestinationPreferredStrategy());
        } else if ("4".equals(choice)) {
            result = manager.merge(source, new ConflictMarkerStrategy());
        } else {
            result = manager.merge(source);
        }
        result.printResult();
    }

    private static void chooseDefaultMergeStrategy() {
        System.out.println("1. Source Preferred");
        System.out.println("2. Destination Preferred");
        System.out.println("3. Conflict Marker (Git Style)");
        String choice = read("Choose default strategy: ");
        if ("1".equals(choice)) {
            manager.setDefaultSourcePreferred();
            System.out.println("Default strategy set to Source Preferred.");
        } else if ("2".equals(choice)) {
            manager.setDefaultDestinationPreferred();
            System.out.println("Default strategy set to Destination Preferred.");
        } else if ("3".equals(choice)) {
            manager.setDefaultConflictMarker();
            System.out.println("Default strategy set to Conflict Marker.");
        } else {
            System.out.println("Invalid strategy.");
        }
    }

    private static void countFiles() {
        System.out.println("Total files: " + manager.countFiles());
    }

    private static void totalSize() {
        System.out.println("Total content size: " + manager.totalSize());
    }

    private static void searchFiles() {
        String condition = read("Enter file name/path condition: ");
        List<File> files = manager.searchFiles(condition);
        System.out.println("Search results:");
        if (files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            for (File file : files) {
                System.out.println("- " + file.getFilePath());
            }
        }
    }

    private static void listByExtension() {
        String extension = read("Enter extension (example: .java): ");
        List<String> files = manager.listByExtension(extension);
        System.out.println("Files:");
        if (files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            for (String path : files) {
                System.out.println("- " + path);
            }
        }
    }

    private static String read(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
