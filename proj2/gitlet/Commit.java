package gitlet;

// any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date; // You'll likely use this in this class
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a gitlet commit object.
 * It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author Young
 */
public class Commit implements Serializable {
    /**
     * add instance variables here.
     * author, date, commit message, parentid
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;

    /*fill in the rest of this class. */
    private Date date;
    private String ID;
    private String parentID1;
    private String parentID2;
    private Map<String, String> fileversion;

    public Commit(String m, Date d, String parentID) {
        message = m;
        date = d;
        parentID1 = parentID;
        parentID2 = "";
        fileversion = new HashMap<>();

        this.ID = Utils.sha1(message) + Utils.sha1(date.toString()) + Utils.sha1(parentID1) + Utils.sha1(parentID2);
    }

    public Commit(String m, Date d, String ID1, String ID2) {
        message = m;
        date = d;
        parentID1 = ID1;
        parentID2 = ID2;
        fileversion = new HashMap<>();

        this.ID = Utils.sha1(message) + Utils.sha1(date) + Utils.sha1(parentID1) + Utils.sha1(parentID2);
    }

    public Map<String, String> getFileversion() {
        return fileversion;
    }

    public String getID() {
        return ID;
    }

    public String getMessage() {
        return message;
    }

    public void addBlob(Map<String, String> m) {
        fileversion = m;
    }

    public void save() {
        File file = Utils.join(Repository.commits, ID);
        Utils.writeObject(file, this);
    }

    public static Commit load(String commitID) {
        File file = Utils.join(Repository.commits, commitID);
        return Utils.readObject(file, Commit.class);
    }

    public String toString() {
        if (parentID2 == null) {
            String dummyString = "===\n";
            String commitString = String.format("commit %s\n", ID);

            String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
            String date_formed = simpleDateFormat.format(date);
            String dateString = String.format("Date: %s\n", date_formed);

            String messageString = String.format("%s\n", message);
            return dummyString + commitString + dateString + messageString;
        } else {
            String dummyString = "===\n";
            String commitString = String.format("commit %s\n", ID);

            String parentID1_7 = parentID1.substring(0, 7);
            String parentID2_7 = parentID2.substring(0, 7);
            String mergeString = String.format("Merge: %s %s\n", parentID1_7, parentID2_7);

            String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
            String date_formed = simpleDateFormat.format(date);
            String dateString = String.format("Date: %s\n", date_formed);

            String messageString = String.format("%s\n", message);
            return dummyString + commitString + mergeString + dateString + messageString;
        }
    }

    public static void main(String[] args) {
        Commit c = new Commit("aaa", new Date(), "bbb");
        System.out.println(c.getID());
    }
}
