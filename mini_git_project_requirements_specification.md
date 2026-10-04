# Software Engineering (BSSE) Final Examination Specification
**Institute:** Institute of Information Technology, University of Dhaka  
**Course:** SE 2215L: Design Patterns Lab  
**Project Title:** Mini Git: Simple Code Repository Management System  
**Marks:** 30 | **Duration:** 2.5 hours  

---

## 1. Project Scenario
The system will be a desktop-based code repository management application inspired by **Git**. Users can create repositories for software projects and manage source-code files and folders within each repository. A repository maintains its project structure, branches, commits, and version history. Users can add, modify, rename, move, and delete files and folders, view previous versions, compare changes, and restore an earlier version of the project. The application does not need to interact with the real operating-system file system.

The project structure can be represented using Java objects in memory. A file should contain information such as its name/path and text content, while a folder can contain files and other folders. Users can make changes to the project and create commits. Each commit should contain information such as the commit message, author, timestamp, parent commit, and a snapshot of the project at that point in time. A commit represents the complete state of the project when it was created and is immutable. Restoring a previous version replaces the current working project state with a copy of the selected commit's project snapshot. The restored state may then be committed as a new commit.

---

## 2. System Requirements & Functional Specifications

### 2.1 Project Hierarchy and Repository Operations
The system should allow users to:
- Create a repository.
- Create, rename, move, modify, and delete files and folders.
- Create commits with a message and author.
- Create branches from an existing branch or commit.
- Switch between branches.
- View the commit history of the active branch.
- Restore the project to a previous version.
- Merge one branch into another.
- View the changes associated between commits or project versions.
- Perform operations on the project hierarchy.
- Track operations history on a particular repository.

---

### 2.2 Comparing Two Commits
The system must provide a simple mechanism for comparing two commits. To calculate the difference between an older commit and a newer commit:

1. Collect every file path and its content from the older commit.
2. Collect every file path and its content from the newer commit.
3. Create a combined set of all file paths appearing in either commit.
4. For every path:
   - If it exists only in the newer commit, report **Added**.
   - If it exists only in the older commit, report **Deleted**.
   - If it exists in both commits but the contents are different, report **Modified**.
   - If it exists in both commits and the contents are identical, report **Unchanged**.
5. Display the resulting list of changes to the user.

---

### 2.3 Operations on the Project Structure
The application should provide several operations that work across the complete file/folder hierarchy. For example, the system should be able to (but not limited to):
- Count the total number of files.
- Calculate the total size of all file contents.
- List files having a particular extension.
- Search for files satisfying a condition.
- Collect file information required when comparing two commits.

*Note:* These operations should work recursively through folders of arbitrary depth. The design should make it possible to introduce another type of project-hierarchy operation without substantially modifying the existing file and folder classes.

---

### 2.4 Branching and Merging
A repository may contain multiple branches representing different lines of development. Each branch points to a project state from which users can continue making changes independently. For example, suppose the `main` branch contains a commit C2 with the following project:

```text
project/
├── src/
│   ├── Main.java
│   └── User.java
└── README.md
```

A user may create a new branch named `feature` from C2. The new branch should start with an independent copy of the project state stored in C2. Changes made to files in `feature` must not modify the project state of `main` or the historical snapshot stored in C2. The user may then modify `feature`, for example by adding `Admin.java` and changing `Main.java`. The `main` branch can continue to change independently.

Users should be able to merge one branch into another. For example, when merging `feature` into `main`, the system should compare the relevant project states and determine which files have been added, deleted, or modified. If both branches contain different contents for the same file, the file is considered to have a **conflict**. The system should support multiple approaches for handling such conflicts. For example:
- **Source-preferred:** Keep the version from the branch being merged into the destination.
- **Destination-preferred:** Keep the version already present in the destination branch.
- Students may implement another reasonable approach.

The repository should have a default merging approach that is used when a merge is performed. The user may optionally select a different approach for a particular merge. The merging mechanism should be designed so that a new approach can be added without substantially changing the repository, branch, commit, or user-interface code. The merge should produce an appropriate new project state that can subsequently be committed to the destination branch. After a merge, the system should report the result, including files that were:
- Added
- Deleted
- Modified
- In conflict
- Resolved using the selected merging approach

---

### 2.5 Repository Events
Important repository actions may require several parts of the application to react. For example, when a commit is created, the system may need to:
- Update the commit history.
- Update diff information.
- Refresh the project information shown to the user.
- Record the operation in the repository activity history.

Other actions, such as creating a branch or completing a merge, may also require multiple reactions. The design should allow a new reaction to be added later without substantially changing the code that performs the original repository operation.

---

### 2.6 High-Level Repository Operations
The operations may internally involve several components responsible for repository management, project structure, version history, branch management, comparison, conflict handling, and activity logging. The user interface should not need to directly coordinate all of these internal components. The user interface should provide simple high-level operations such as:
- Create repository.
- Commit changes.
- Create branch / clone repository state.
- Merge branches.
- Restore a previous version.

---

## 3. Student Requirements
Students are expected to analyze the requirements and design an appropriate object-oriented solution. They must be able to:
1. Design the project hierarchy for files and folders.
2. Implement repository, branch, commit, and version management.
3. Implement independent cloning of a repository/project state.
4. Implement commit comparison using the specified path-and-content comparison approach.
5. Implement at least three different operations that recursively process the project hierarchy.
6. Support branching and merging.
7. Support multiple interchangeable merging approaches.
8. Detect and appropriately handle merge conflicts.
9. Handle important repository events and their independent reactions.
10. Provide high-level operations that hide unnecessary internal complexity from the user interface.
11. Use appropriate design patterns where they solve genuine design problems.
12. Explain the design decisions and justify why the selected patterns are appropriate.
13. Identify reasonable alternative designs and explain their trade-offs.

> **Final Demonstration Note:** During the final demonstration, students should be prepared to explain their architecture and make a small design change requested by the instructor, such as introducing a new merging approach, adding another repository operation, adding another hierarchy analysis, or adding a new repository event reaction.

---

## 4. Marking Rubric (Total: 30 Marks)

### 4.1 Design — 12 Marks
The design marks will be distributed across the four major design areas:
- **Project structure and hierarchy (6 marks):** Covering the design of files, folders, hierarchy operations, commits, and version snapshots.
- **Branching and merging (2 marks):** Covering independent branch/project states, cloning, multiple merging approaches, and conflict handling.
- **Repository events (2 marks):** Covering the design of independent reactions to important repository operations.
- **High-level repository operations (2 marks):** Covering appropriate abstraction of operations such as commit, branch creation, merge, and restore, keeping the user interface independent of internal components.

### 4.2 Implementation — 8 Marks
- **Correct implementation of repository, files/folders, commits, and project hierarchy (3 marks).**
- **Branching, independent cloning, and version restoration (2 marks).**
- **Commit comparison and multiple merging approaches, including conflict handling (2 marks).**
- **Repository event handling and operations abstraction (2 marks).**

### 4.3 Prompting and Token Efficiency — 5 Marks
- **Clear and effective prompts with appropriate context (2 marks).**
- **Effective use of AI for design, implementation, debugging, and refinement (1 mark).**
- **Efficient use of prompts and tokens, avoiding unnecessary repetition and excessive generated content (2 marks).**

### 4.4 Q&A — 5 Marks
- **Understanding of the overall architecture and implementation (2 marks).**
- **Ability to explain and justify the selected design patterns and alternatives (1 mark).**
- **Understanding of their own code and ability to answer implementation questions (1 mark).**
- **Responding to a small design or change request during the viva (1 mark).**