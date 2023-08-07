package gitlet;

import java.io.Serializable;
import java.util.*;

public class Tree implements Serializable {
    private Node node;
    private Node currenthead;
    private Map<String, Node> headlist;

    public class Node implements Serializable{
        Commit commit;
        List<Node> next;
        Node parent1;
        Node parent2;

        public Node(Commit c, Node n) {
            commit = c;
            next = new LinkedList<>();
            parent1 = n;
            parent2 = null;
        }

        public Node(Commit c, Node n1, Node n2) {
            commit = c;
            next = new LinkedList<>();
            parent1 = n1;
            parent2 = n2;
        }
    }

    public Tree(Commit c) {
        node = new Node(c, null);
        currenthead = node;
        headlist = new HashMap<>();
        headlist.put("master", node);
    }

    public void addCommit(Commit c, String branch) {
        Node newnode = new Node(c, currenthead);
        node.next.add(newnode);
        currenthead = newnode;
        if (headlist.containsKey(branch)) {
            headlist.replace(branch, currenthead);
        } else {
            headlist.put(branch, currenthead);
        }
    }

    public void save() {
        Utils.writeObject(Repository.tree, this);
    }

    static public Tree load() {
        return Utils.readObject(Repository.tree, Tree.class);
    }

    public Node getCurrenthead() {
        return currenthead;
    }

    public void changeBranch(String branch) {
        currenthead = headlist.get(branch);
    }

    public static void main(String[] args) {
        Commit startcommit = new Commit("auto generated", new Date(0), "");
        Tree tree = new Tree(startcommit);
        gitlet.Utils.serialize(tree);
    }
}
