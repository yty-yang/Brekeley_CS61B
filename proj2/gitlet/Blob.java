package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Blob implements Serializable {
    private byte[] content;
    private String ID;

    public Blob(byte[] c) {
        this.content = c;
        this.ID = Utils.sha1(c);
    }

    public String getID() {
        return ID;
    }

    public void addtoBlobs() {
        Blobs blobs = Blobs.load();
        blobs.add(ID);
        blobs.save();

        File f = Utils.join(Repository.blobs, ID);
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeObject(f, this);
    }
}
