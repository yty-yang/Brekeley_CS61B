package gitlet;

import java.io.Serializable;
import java.io.File;

public class Branch implements Serializable {
    private String name;
    private Commit endCommit;

    public Branch(String name) {
        this.name = name;
        endCommit = null;
    }

    public void setEndCommit(Commit c) {
        endCommit = c;
    }

    public Commit getEndCommit() {
        return endCommit;
    }

    public void save() {
        File file = Utils.join(Repository.commits, name);
        Utils.writeObject(file, this);
    }

    public static Branch load(String branchname) {
        File file = Utils.join(Repository.commits, branchname);
        return Utils.readObject(file, Branch.class);
    }

    public static String getCurrentBranchName() {
        return Utils.readContentsAsString(Repository.currentBranch);
    }

    public static void changeCurrentBranch(String branchname) {
        Utils.writeObject(Repository.currentBranch, branchname);
    }
}
