package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Stage implements Serializable {
    private Map<String, String> blobMap;

    public Stage() {
        blobMap = new HashMap<>();
    }

    static public Stage load() {
        return Utils.readObject(Repository.stage_file, Stage.class);
    }

    public void save() {
        Utils.writeObject(Repository.stage_file, this);
    }

    public Map<String, String> getMap() {
        return blobMap;
    }

    public void add(String f, String b) {
        blobMap.put(f, b);
    }

    public void addALL(Map<String, String> map) {
        blobMap.putAll(map);
    }

    public void remove(String f) {
        blobMap.remove(f);
    }

    public void replace(String f, String b) {
        blobMap.replace(f, b);
    }

    public void clear() {
        blobMap.clear();
    }
}
