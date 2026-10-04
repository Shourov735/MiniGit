# MiniGit: Simple Code Repository Management System

An in-memory version control system inspired by **Git**, developed for **SE 2215L: Design Patterns Lab** at the **Institute of Information Technology (IIT), University of Dhaka**.

---

## 📌 Features

- **Virtual File System Hierarchy:** Manage nested files and folders entirely in memory without altering the host OS file system.
- **Commits & Immutable Snapshots:** Save complete project snapshots with commit IDs, timestamps, author metadata, and parent commit links.
- **Branching & Cloning:** Create independent branches from existing branches or specific historical commits.
- **Branch Merging & Conflict Resolution:** Merge branches with pluggable conflict resolution strategies.
- **Commit Comparison:** Compare two commits to identify added, deleted, modified, and unchanged files.
- **Recursive Hierarchy Operations:** File counting, total size calculation, extension filtering, condition search, and file collection.
- **Command Undo/Redo:** Full undo and redo capabilities for file/folder modifications.
- **Event Notification System:** Observer pattern to trigger reactions for commits, branches, merges, and activity tracking.
- **CLI Interface:** Interactive command-line menu for repository management.

---

## 🏗️ Architecture & Design Patterns

| Pattern | Package | Key Classes & Roles |
| :--- | :--- | :--- |
| **Composite** | `composite/` | `FileSystemComponent` (Component), `File` (Leaf), `Folder` (Composite) |
| **Memento** | `memento/` | `EditorSnapshot` (Memento), `SnapshotHistory` (Caretaker), `Git` (Originator) |
| **Command** | `command/` | `Command` (Interface), `CreateCommand`, `DeleteCommand`, `ModifyCommand`, `MoveCommand`, `RenameCommand` |
| **Prototype** | `prototype/` | `Prototype` (Interface), `Branch` (Concrete Prototype), `PrototypeRegistry` (Registry) |
| **Strategy** | `strategy/` | `Strategy` (Interface), `MergeContext`, `SourcePreferredStrategy`, `DestinationPreferredStrategy`, `ConflictMarkerStrategy` |
| **State** | `state/` | `ProjectState` (State), `Conflict`, `NotConflict` (Concrete States) |
| **Observer** | `events/` | `RepositoryEventListener` (Observer), `RepositoryEvent` (Event), `ActivityLogger`, `CommitEventListener`, `BranchEventListener`, `MergeEventListener`, `DiffUpdateEventListener`, `ProjectViewRefreshListener` |
| **Facade** | `repository/` | `RepositoryManager` (Facade hiding internal subsystems from `Main`) |

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher

### Compilation
```bash
javac -d bin $(find src -name "*.java")
```

### Running the Application
```bash
java -cp bin Main
```

---

## 📁 Project Structure

```
MiniGit/
├── src/
│   ├── Main.java
│   ├── command/
│   │   ├── Command.java
│   │   ├── CreateCommand.java
│   │   ├── DeleteCommand.java
│   │   ├── ModifyCommand.java
│   │   ├── MoveCommand.java
│   │   └── RenameCommand.java
│   ├── composite/
│   │   ├── FileSystemComponent.java
│   │   ├── File.java
│   │   └── Folder.java
│   ├── events/
│   │   ├── ActivityLogger.java
│   │   ├── BranchEventListener.java
│   │   ├── CommitEventListener.java
│   │   ├── DiffUpdateEventListener.java
│   │   ├── MergeEventListener.java
│   │   ├── ProjectViewRefreshListener.java
│   │   ├── RepositoryEvent.java
│   │   └── RepositoryEventListener.java
│   ├── memento/
│   │   ├── EditorSnapshot.java
│   │   └── SnapshotHistory.java
│   ├── merge/
│   │   ├── Change.java
│   │   ├── Merge.java
│   │   └── MergeResult.java
│   ├── operations/
│   │   ├── ExtensionSearchOperation.java
│   │   ├── FileCollectorOperation.java
│   │   ├── FileCountOperation.java
│   │   ├── HierarchyOperation.java
│   │   └── TotalSizeOperation.java
│   ├── prototype/
│   │   ├── Branch.java
│   │   ├── Prototype.java
│   │   └── PrototypeRegistry.java
│   ├── repository/
│   │   ├── Commit.java
│   │   ├── CommitSection.java
│   │   ├── Component.java
│   │   ├── Git.java
│   │   ├── Repository.java
│   │   ├── RepositoryManagement.java
│   │   └── RepositoryManager.java
│   ├── search/
│   │   └── SearchFile.java
│   ├── state/
│   │   ├── Conflict.java
│   │   ├── NotConflict.java
│   │   └── ProjectState.java
│   └── strategy/
│       ├── ConcreteStrategy.java
│       ├── ConflictMarkerStrategy.java
│       ├── DestinationPreferredStrategy.java
│       ├── MergeContext.java
│       ├── SourcePreferredStrategy.java
│       └── Strategy.java
├── mini_git_project_requirements_specification.md
├── .gitignore
└── README.md
```

---

## 📝 License
This project is developed for educational purposes under the BSSE curriculum at IIT, University of Dhaka.
