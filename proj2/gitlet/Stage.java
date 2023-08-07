package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Stage implements Serializable {
    private String branchName;
    private Map<String, String> addition;
    private List<String> removal;

    public Stage(String name) {
        branchName = name;
        addition = new HashMap<>();
        removal = new LinkedList<>();
    }

    public void AddToAddition(String filename, String blobID) {
        addition.put(filename, blobID);
    }

    public void RemoveFromAddition(String filename) {
        addition.remove(filename);
    }

    public void AddToRemoval(String filename) {
        removal.add(filename);
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }

    public Map<String, String> getAddition() {
        return addition;
    }

    public List<String> getRemoval() {
        return removal;
    }

    public void save() {
        File file = Utils.join(Repository.stages, branchName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeObject(file, this);
    }

    public static Stage load(String branchName) {
        File file = Utils.join(Repository.stages, branchName);
        return Utils.readObject(file, Stage.class);
    }

    public static void main(String[] args) {
        Stage stage = new Stage("master");
        Utils.serialize(stage);
    }
}
