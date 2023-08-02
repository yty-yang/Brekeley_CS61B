package gitlet;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CommitTree implements Serializable {
    private Node node;
    private Node head;

    public class Node {
        Commit commit;
        List<Node> next;
        Node parent1;
        Node parent2;

        public Node() {
            commit = null;
            next = new LinkedList<>();
            parent1 = null;
            parent2 = null;
        }

        public Node(Commit c) {
            commit = c;
            next = new LinkedList<>();
            parent1 = null;
            parent2 = null;
        }

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

    public CommitTree(Commit c) {
        node = new Node(c);
        head = node;
    }

    public void addCommit(Commit c) {
        Node newnode = new Node(c, head);
        node.next.add(newnode);
        head = newnode;
    }

    public void save() {
        Utils.writeObject(Repository.structure_file, this);
    }

    static public CommitTree load() {
        return Utils.readObject(Repository.structure_file, CommitTree.class);
    }

    public Commit getHeadCommit() {
        return head.commit;
    }

    public Node getHead() {
        return head;
    }
}
