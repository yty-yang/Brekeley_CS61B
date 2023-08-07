package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {
    private String ID;
    private byte[] content;

    public Blob(byte[] content) {
        this.content = content;
        ID = Utils.sha1(content);
    }

    public String getID() {
        return ID;
    }

    public byte[] getContent() {
        return content;
    }

    public void save() {
        File file = Utils.join(Repository.blobs, ID);
        Utils.writeObject(file, this);
    }

    public static void main(String[] args) {
        byte[] message = new byte[2];
        message[0] = 'a';
        message[1] = 'b';
        Blob test = new Blob(message);
        System.out.println(test.getID());
        System.out.println(test.content);
    }
}
