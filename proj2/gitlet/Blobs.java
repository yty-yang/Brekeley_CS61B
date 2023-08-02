package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static gitlet.Utils.join;

public class Blobs implements Serializable {
    private Set<String> blobs;

    public Blobs() {
        blobs = new HashSet<>();
    }

    static public Blobs load() {
        File f = join(Repository.blobs, "blobs");
        return Utils.readObject(f, Blobs.class);
    }

    public void save() {
        File f = join(Repository.blobs, "blobs");
        Utils.writeObject(f, this);
    }

    public Set<String> get() {
        return blobs;
    }

    public void add(String blobID) {
        blobs.add(blobID);
    }
}
