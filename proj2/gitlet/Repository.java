package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.join;
import static gitlet.Utils.readContents;

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
      tree headCommit stage nameTOblob
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

    /*file*/
    public static final File stage_file = join(GITLET_DIR, "stage"); //'adding' can put files into stage
    public static final File structure_file = join(commits, "structure"); //save the relationship of commits
    public static final File workingdirectory_file = join(GITLET_DIR, "workingdirectory");
    public static final File branches_file = join(GITLET_DIR, "branches");

    /*variables*/
    static private CommitTree tree;
    static private Commit headCommit;
    static private Stage stage;
    static private Map<String, String> nameTOblob;

    /* fill in the rest of this class. */
    public static void init() {
        File f;
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();

            blobs.mkdir();
            f = join(blobs, "blobs"); // a file to store IDs of all the blob
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Blobs newblobs = new Blobs();
            newblobs.save();

            try {
                stage_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage newstage = new Stage();
            newstage.save();

            try {
                workingdirectory_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Status newdir = Status.load();
            newdir.save();

            commits.mkdir();
            f = join(commits, "commits");
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Commits newcommits = new Commits();
            newcommits.save();

            try {
                structure_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Commit first = new Commit("auto", new Date(0), null);
            CommitTree tree = new CommitTree(first);
            tree.save();

            try {
                branches_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Branch newbranch = new Branch();
            newbranch.add("master", tree.getHead());
            newbranch.save();
        } else {
            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
        }
    }

    public static void IintCheck() {
        if (!GITLET_DIR.exists()) {
            throw new GitletException("Not in an initialized Gitlet directory.");
        }
    }

    public static void ADD(String f) {
        File file = join(CWD, f);
        if (!file.exists()) {
            throw new GitletException("File does not exist.");
        }

        Status workingDirectory = Status.load();
        workingDirectory.add(f);
        workingDirectory.save();

        Blob blob = new Blob(readContents(file));
        blob.addtoBlobs();
        String ID = blob.getID();

        SetOrigin();

        if (nameTOblob.containsKey(f)) {
            String blobID = nameTOblob.get(f);
            if (blobID.equals(ID)) {
                stage.remove(f);
                stage.save();
                return;
            }
        }

        Map<String, String> addedMap = stage.getMap();

        if (addedMap.containsKey(f)) {
            stage.replace(f, ID);
            stage.save();
            return;
        }

        stage.add(f, ID);
        stage.save();
    }

    public static void COMMIT(String message) {
        checkMessage(message);

        SetOrigin();

        Commit c = new Commit(message, new Date(), headCommit.getID());

        setNameBlobMap();

        c.addBlob(nameTOblob);
        c.addtoCommits();

        stage.clear();
        stage.addALL(nameTOblob);
        stage.save();

        tree.addCommit(c);
        tree.save();
    }

    public static void RM(String f) {
        Status workingDirectory = Status.load();

        if (! workingDirectory.contains(f)) {
            throw new GitletException("No reason to remove the file.");
        }

        workingDirectory.remove(f);
        workingDirectory.save();
    }

    public static void LOG() {
        tree = CommitTree.load();
        CommitTree.Node head = tree.getHead();

        while (head.parent1 != null) {
            headCommit = head.commit;
            System.out.println(headCommit.toString());
            head = head.parent1;
        }
    }

    public static void GlobalLOG() {
        Commits commits = Commits.load();
        Set<String> allcommits = commits.get();
        Commit currentcommit;
        File f;

        for (String i: allcommits) {
            f = Utils.join(Repository.commits, i);
            currentcommit = Utils.readObject(f, Commit.class);

            System.out.println(currentcommit.toString());
        }
    }

    public static void FIND(String message) {
        Commits commits = Commits.load();
        Set<String> allcommits = commits.get();
        Commit currentcommit;
        File f;
        Boolean finded = false;

        for (String i: allcommits) {
            f = Utils.join(Repository.commits, i);
            currentcommit = Utils.readObject(f, Commit.class);

            if (currentcommit.getMessage().equals(message)) {
                finded = true;
                System.out.println(currentcommit.getID());
            }
        }

        if (!finded) {
            throw new GitletException("Found no commit with that message.");
        }
    }

    public static void STATUS() {

    }

    private static void SetOrigin() {
        tree = CommitTree.load();
        headCommit = tree.getHeadCommit();
        stage = Stage.load();
        nameTOblob = headCommit.getName_blob();
    }

    private static void checkMessage(String message) {
        if (message.isBlank()) {
            throw new GitletException("Please enter a commit message.");
        }
    }

    private static void setNameBlobMap() {
        Map<String, String> previous_nameTOblob = headCommit.getName_blob();
        Boolean changed = false;

        Status workingDirectory = Status.load();

        for (String i: previous_nameTOblob.keySet()) {
            if (nameTOblob.containsKey(i)) {
                if (! previous_nameTOblob.get(i).equals(nameTOblob.get(i))) {
                    changed = true;
                }
            } else {
                if (workingDirectory.contains(i)) {
                    nameTOblob.put(i, previous_nameTOblob.get(i));
                }

                changed = true;
            }
        }

        if (! changed) {
            throw new GitletException("No changes added to the commit.");
        }
    }
}
