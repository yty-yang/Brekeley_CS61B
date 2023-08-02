package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static gitlet.Utils.join;

public class Commits implements Serializable {
    private Set<String> commits;

    public Commits() {
        commits = new HashSet<>();
    }

    static public Commits load() {
        File f = join(Repository.commits, "commits");
        return Utils.readObject(f, Commits.class);
    }

    public void save() {
        File f = join(Repository.commits, "commits");
        Utils.writeObject(f, this);
    }

    public Set<String> get() {
        return commits;
    }

    public void add(String commitID) {
        commits.add(commitID);
    }
}
