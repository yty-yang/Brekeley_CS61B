package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Branch implements Serializable {
    private String currentBranch;
    Map<String, CommitTree.Node> branchTOnode;

    public Branch() {
        currentBranch = "master";
        branchTOnode = new HashMap<>();
    }

    public void add(String branch, CommitTree.Node node) {
        branchTOnode.put(branch, node);
    }

    public void save() {
        Utils.writeObject(Repository.branches_file, this);
    }

    public static Branch load() {
        return Utils.readObject(Repository.branches_file, Branch.class);
    }

    public void changeBranch(String branch) {
        currentBranch = branch;
    }
}
