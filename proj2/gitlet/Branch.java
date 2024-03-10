package gitlet;

import java.io.Serializable;
import java.io.File;
import java.util.Date;

public class Branch implements Serializable {
    private String name;
    private String endCommit;

    public Branch(String name) {
        this.name = name;
        endCommit = null;
    }

    public void setEndCommit(Commit c) {
        endCommit = c.getID();
    }

    public String getEndCommit() {
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

    public static String getHead() {
        return Utils.readObject(Repository.head, String.class);
    }

    public static void changeHead(String branchname) {
        Utils.writeObject(Repository.head, branchname);
    }

    public static void main(String[] args) {
        Commit startcommit = new Commit("auto generated", new Date(0), "");
        Branch defultbranch = new Branch("master");
        defultbranch.setEndCommit(startcommit);
        Utils.serialize(defultbranch);
    }
}
