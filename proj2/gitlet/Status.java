package gitlet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Status implements Serializable {
    private Set<String> workingdirectory;
    private Set<String> removeddirectory;

    public Status() {
        workingdirectory = new HashSet<>();
    }

    public static Status load() {
        return Utils.readObject(Repository.workingdirectory_file, Status.class);
    }

    public void add(String file) {
        workingdirectory.add(file);
    }

    public void remove(String file) {
        workingdirectory.remove(file);
    }

    public void save() {
        Utils.writeObject(Repository.workingdirectory_file, this);
    }

    public Boolean contains(String i) {
        return workingdirectory.contains(i);
    }
}
