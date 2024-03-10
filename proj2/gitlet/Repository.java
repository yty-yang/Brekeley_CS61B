package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

//any imports you need here

/**
 * Represents a gitlet repository.
 * It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author Young
 */
public class Repository {
    /**
     add instance variables here.

     List all instance variables of the Repository class here with a useful
     comment above them describing what that variable represents and how that
     variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File commits = join(GITLET_DIR, "commits");
    public static final File blobs = join(GITLET_DIR, "blobs");
    public static final File stages = join(GITLET_DIR, "stages");
    public static final File branches = join(GITLET_DIR, "branches");

    /*file*/
    public static final File head = join(GITLET_DIR, "currentBranch");


    /* fill in the rest of this class. */
    public static void INIT() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();

            blobs.mkdir();

            commits.mkdir();

            Commit startcommit = new Commit("initial commit", new Date(0), "");
            startcommit.save();

            branches.mkdir();
            Branch defultbranch = new Branch("master");
            defultbranch.setEndCommit(startcommit);
            defultbranch.save();

            Branch.changeHead("master");

            stages.mkdir();
            Stage stage = new Stage("master");
            stage.save();
        } else {
            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
        }
    }

    public static void InitCheck() {
        if (!GITLET_DIR.exists()) {
            throw new GitletException("Not in an initialized Gitlet directory.");
        }
    }

    public static void ADD(String filename) {
        File file = join(CWD, filename);
        if (!file.exists()) {
            throw new GitletException("File does not exist.");
        }

        Blob blob = new Blob(readContents(file));
        File blobfile = join(blobs, blob.getID());
        if (!blobfile.exists()) {
            blob.save();
        }

        Stage stage = Stage.load(Branch.getHead());
        stage.AddToAddition(filename, blob.getID());
        stage.save();
    }

    public static void COMMIT(String message) {
        Stage stage = Stage.load(Branch.getHead());
        Map<String, String> addition = stage.getAddition();
        if (addition.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        }

        if (message.isBlank()) {
            throw new GitletException("Please enter a commit message.");
        }

        Branch branch = Branch.load(Branch.getHead());

        Commit headcommit = readObject(join(commits, branch.getEndCommit()), Commit.class);
        Commit newcommit = new Commit(message, new Date(), headcommit.getID());

        Map<String, String> fileversion_previous = headcommit.getFileversion();
        Map<String, String> fileversion = fileversion_previous;

        for (String i : addition.keySet()) {
            fileversion.put(i, addition.get(i));
        }

        for (String i : stage.getRemoval()) {
            if (fileversion.keySet().contains(i)) {
                fileversion.remove(i);
            }
        }

        newcommit.addBlob(fileversion);
        newcommit.save();

        stage.clear();

        branch.setEndCommit(newcommit);
        branch.save();
    }

    public static void RM(String filename) {
        boolean removed = false;
        Stage stage = Stage.load(Branch.getHead());

        Map<String, String> addition = stage.getAddition();
        if (addition.containsKey(filename)) {
            stage.RemoveFromAddition(filename);
            String blobID = addition.get(filename);
            File blob = join(blobs, blobID);
            Utils.restrictedDelete(blob);
            removed = true;
        }

        Commit headcommit = readObject(join(commits, Branch.load(Branch.getHead()).getEndCommit()), Commit.class);
        if (headcommit.getFileversion().keySet().contains(filename)) {
            stage.AddToRemoval(filename);
            File file = join(CWD, filename);
            Utils.restrictedDelete(file);
            removed = true;
        }

        if (!removed) {
            throw new GitletException("No reason to remove the file.");
        }
    }

    public static void LOG() {
        Commit commit = readObject(join(commits, Branch.load(Branch.getHead()).getEndCommit()), Commit.class);

        while (!commit.getParentID1().equals("")) {
            System.out.println(commit);
            commit = readObject(join(commits, commit.getParentID1()), Commit.class);
        }
        System.out.println(commit);
    }

    public static void GlobalLOG() {
        List<String> commitNames = plainFilenamesIn(commits);

        File file;
        Commit commit;
        for (String i : commitNames) {
            file = join(commits, i);
            commit = readObject(file, Commit.class);
            System.out.println(commit.toString());
        }
    }

    public static void FIND(String message) {
        boolean finded = false;
        List<String> commitNames = plainFilenamesIn(commits);

        File file;
        Commit commit;
        for (String i : commitNames) {
            file = join(commits, i);
            commit = readObject(file, Commit.class);

            if (commit.getMessage().equals(message)) {
                System.out.println(commit.getID());
                finded = true;
            }
        }

        if (!finded) {
            throw new GitletException("Found no commit with that message.");
        }
    }

    public static void STATUS() {
        System.out.println("=== Branches ===");
        List<String> branchNames = plainFilenamesIn(branches);
        Collections.sort(branchNames);
        for (String i : branchNames) {
            if (i.equals(Branch.getHead())) {
                System.out.println('*' + i);
            } else {
                System.out.println(i);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        Stage stage = Stage.load(Branch.getHead());
        List<String> addition = new LinkedList<>();
        for (String i : stage.getAddition().keySet()) {
            addition.add(i);
        }
        Collections.sort(addition);
        for (String i : addition) {
            System.out.println(i);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        List<String> removal = stage.getRemoval();
        Collections.sort(removal);
        for (String i : removal) {
            System.out.println(i);
        }
        System.out.println();

        List<String> allfiles = plainFilenamesIn(CWD);
        Set<String> modified = new HashSet<>();
        Set<String> deleted = new HashSet<>();
        List<String> untracked = new LinkedList<>();

        Commit commit = readObject(join(commits, Branch.load(Branch.getHead()).getEndCommit()), Commit.class);
        Blob blob;
        boolean tracked;
        for (String i : allfiles) {
            blob = new Blob(readContents(join(CWD, i)));
            tracked = false;

            if (stage.getAddition().keySet().contains(i)) {
                tracked = true;
                if (!blob.getID().equals(stage.getAddition().get(i))) {
                    modified.add(i);
                }
            } else if (commit.getFileversion().keySet().contains(i)) {
                tracked = true;
                if (!blob.getID().equals(commit.getFileversion().get(i))) {
                    modified.add(i);
                }
            }

            if (!tracked) {
                untracked.add(i);
            }
        }

        for (String i : stage.getAddition().keySet()) {
            File file = join(CWD, i);
            if (!file.exists()) {
                deleted.add(i);
            }
        }

        for (String i : commit.getFileversion().keySet()) {
            if (!stage.getRemoval().contains(i)) {
                File file = join(CWD, i);
                if (!file.exists()) {
                    deleted.add(i);
                }
            }
        }

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String i : allfiles) {
            if (modified.contains(i)) {
                System.out.println(i + "(modified)");
            }

            if (deleted.contains(i)) {
                System.out.println(i + "(deleted)");
            }
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String i : untracked) {
            System.out.println(i);
        }
    }

    public static void CHECKOUT_branch(String branchname) {
        List<String> branchnames = plainFilenamesIn(branches);
        if (!branchnames.contains(branchname)) {
            throw new GitletException("No such branch exists.");
        }
        if (branchname.equals(Branch.getHead())) {
            throw new GitletException("No need to checkout the current branch.");
        }

        Branch newbranch = Branch.load(branchname);
        Commit currentcommit = readObject(join(commits, Branch.load(Branch.getHead()).getEndCommit()), Commit.class);
        Commit newcommit = readObject(join(commits, newbranch.getEndCommit()), Commit.class);
        Stage stage = Stage.load(Branch.getHead());
        Map<String, String> addition = stage.getAddition();
        List<String> filenames = plainFilenamesIn(CWD);
        for (String i : filenames) {
            if (!addition.containsKey(i) && !currentcommit.getFileversion().containsKey(i) && newcommit.getFileversion().containsKey(i)) {
                throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }


        for (String i : filenames) {
            restrictedDelete(join(CWD, i));
        }
        String blobID;
        File file;
        byte[] content;
        Map<String, String> fileversion = newcommit.getFileversion();
        for (String i : fileversion.keySet()) {
            blobID = fileversion.get(i);
            file = join(CWD, i);
            content = readObject(join(blobs, blobID), Blob.class).getContent();
            writeContents(file, content);
        }
        Branch.changeHead(branchname);
        stage.clear();
        stage.save();
    }

    public static void CHECKOUT_file(String name) {
        Commit commit = readObject(join(commits, Branch.load(Branch.getHead()).getEndCommit()), Commit.class);

        checkoutfile(name, commit);
    }

    public static void CHECKOUT_file(String ID, String name) {
        if (ID.length() < 40) {
            ID = findID(ID);
        }
        File commitfile = join(commits, ID);
        if (!commitfile.exists()) {
            throw new GitletException("No commit with that id exists.");
        }
        Commit commit = readObject(commitfile, Commit.class);

        checkoutfile(name, commit);
    }

    private static void checkoutfile(String name, Commit commit) {
        if (!commit.getFileversion().containsKey(name)) {
            throw new GitletException("File does not exist in that commit.");
        }

        String blobID = commit.getFileversion().get(name);
        File f = join(CWD, name);
        File blob = join(blobs, blobID);
        byte[] content = readObject(blob, Blob.class).getContent();
        writeContents(f, content);
    }

    private static String findID(String shortID) {
        List<String> IDs = plainFilenamesIn(commits);
        int length = shortID.length();
        for (String i : IDs) {
            if (shortID.equals(i.substring(0, length))) {
                return i;
            }
        }
        throw new GitletException("No commit with that id exists.");
    }

    public static void BRANCH(String name) {
        Branch newbranch = new Branch(name);
        Commit currentcommit = readObject(join(commits, Branch.load(Branch.getHead()).getEndCommit()), Commit.class);

        newbranch.setEndCommit(currentcommit);
        newbranch.save();
    }

    public static void RMBRANCH(String name) {
        File branch = join(branches, name);
        if (!branch.exists()) {
            throw new GitletException("A branch with that name does not exist.");
        }

        if (name.equals(Branch.getHead())) {
            throw new GitletException("Cannot remove the current branch.");
        }

        restrictedDelete(branch);
    }

    public static void RESET(String ID) {
        if (ID.length() < 40) {
            ID = findID(ID);
        }
        File commitfile = join(commits, ID);
        if (!commitfile.exists()) {
            throw new GitletException("No commit with that id exists.");
        }

        Commit commit = readObject(commitfile, Commit.class);
        List<String> filenames = plainFilenamesIn(CWD);
        for (String i : filenames) {
            restrictedDelete(join(CWD, i));
        }
        String blobID;
        File file;
        byte[] content;
        Map<String, String> fileversion = commit.getFileversion();
        for (String i : fileversion.keySet()) {
            blobID = fileversion.get(i);
            file = join(CWD, i);
            content = readObject(join(blobs, blobID), Blob.class).getContent();
            writeContents(file, content);
        }

        Stage stage = Stage.load(Branch.getHead());
        stage.clear();
        stage.save();

        Branch branch = Branch.load(Branch.getHead());
        branch.setEndCommit(commit);
        branch.save();
    }

    public static void MERGE(String branch) {
        File branchfile = join(branches, Branch.load(branch).getEndCommit());
        if (!branchfile.exists()) {
            throw new GitletException("A branch with that name does not exist.");
        }
        if (branch.equals(Branch.getHead())) {
            throw new GitletException("Cannot merge a branch with itself.");
        }

        Commit commit1 = readObject(join(branches, Branch.load(Branch.getHead()).getEndCommit()), Commit.class);
        Commit commit2 = readObject(branchfile, Commit.class);

        String splitpointID = getSplitPointCommitId(commit1.getID(), commit2.getID());

        //check if they are linear relationship
        if (splitpointID.equals(commit2.getID())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitpointID.equals(commit1.getID())) {
            CHECKOUT_branch(branch);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        Stage stage = Stage.load(Branch.getHead());
        if (!stage.getAddition().isEmpty() || !stage.getRemoval().isEmpty()) {
            throw new GitletException("You have uncommitted changes.");
        }

        Commit splitpoint = Commit.load(splitpointID);

        boolean conflict = processMerge(stage, splitpoint, commit1, commit2);

        stage.save();
        Commit commit = new Commit(String.format("Merged %s into %s.", branch, Branch.getHead()), new Date(), commit1.getID(), commit2.getID());
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static boolean processMerge(Stage stage, Commit split, Commit commit1, Commit commit2) {
        List<String> untrackedfiles = plainFilenamesIn(CWD);
        for (String i : stage.getAddition().keySet()) {
            untrackedfiles.remove(i);
        }
        for (String i : stage.getRemoval()) {
            untrackedfiles.remove(i);
        }

        Set<String> allfiles = new HashSet<>();
        allfiles.addAll(split.getFileversion().keySet());
        allfiles.addAll(commit1.getFileversion().keySet());
        allfiles.addAll(commit2.getFileversion().keySet());

        for (String i : allfiles) {

        }

        return false;
    }

    private static String getSplitPointCommitId(String currentCommitId, String mergedCommitId) {
        Set<String> commitSet = new HashSet<>();
        Queue<String> bfsQueue = new ArrayDeque<>();
        bfsQueue.add(currentCommitId);
        while (!bfsQueue.isEmpty()) {
            String commitId = bfsQueue.remove();
            Commit commit = Commit.load(commitId);
            commitSet.add(commitId);
            if (!commit.getParentID1().equals("")) {
                bfsQueue.add(commit.getParentID1());
            }
            if (!commit.getParentID2().equals("")) {
                bfsQueue.add(commit.getParentID2());
            }
        }

        bfsQueue.add(mergedCommitId);
        while (!bfsQueue.isEmpty()) {
            String commitId = bfsQueue.remove();
            Commit commit = Commit.load(commitId);
            if (commitSet.contains(commitId)) {
                return commitId;
            }
            if (!commit.getParentID1().equals("")) {
                bfsQueue.add(commit.getParentID1());
            }
            if (!commit.getParentID2().equals("")) {
                bfsQueue.add(commit.getParentID2());
            }
        }

        return null;
    }

    private static void createFile(String name, String blob1_name, String blob2_name) {
        String currentContents;
        String mergedContents;
        currentContents = readObject(join(blobs, blob1_name), Blob.class).getContent().toString();
        mergedContents = readObject(join(blobs, blob2_name), Blob.class).getContent().toString();
        writeContents(join(CWD, name), "<<<<<<< HEAD\n" + currentContents + "=======\n" + mergedContents + ">>>>>>>\n");
    }

    public static void main(String[] args) {
//        INIT();
//        ADD("gitlet-design.md");
//        COMMIT("added");
//        LOG();
    }
}
