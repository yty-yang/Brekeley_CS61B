package gitlet;

import java.io.Serializable;
import java.io.File;
import java.util.Date;

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
        File file = Utils.join(Repository.branches, name);
        Utils.writeObject(file, this);
    }

    public static Branch load(String branchname) {
        File file = Utils.join(Repository.branches, branchname);
        return Utils.readObject(file, Branch.class);
    }

    public static String getCurrentBranchName() {
        return Utils.readObject(Repository.currentBranch, String.class);
    }

    public static void changeCurrentBranch(String branchname) {
        Utils.writeObject(Repository.currentBranch, branchname);
    }

    public static void main(String[] args) {
        Commit startcommit = new Commit("auto generated", new Date(0), "");
        Branch defultbranch = new Branch("master");
        defultbranch.setEndCommit(startcommit);
        Utils.serialize(defultbranch);
    }
}
