package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K, V> {
    private Node node;
    private int size;

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        boolean containsKey(K key) {
            if (this.key == null) {
                return false;
            }

            if (key.equals(this.key)) {
                return true;
            } else if (key.compareTo(this.key) < 0) {
                return left.containsKey(key);
            } else {
                return right.containsKey(key);
            }
        }

        V get(K key) {
            if (this.key == null) {
                return null;
            }

            if (key.equals(this.key)) {
                return this.value;
            } else if (key.compareTo(this.key) < 0) {
                return left.get(key);
            } else {
                return right.get(key);
            }
        }

        void put(K key, V value) {
            if (this.key == null) {
                this.key = key;
                this.value = value;
                this.left = new Node(null, null);
                this.right = new Node(null, null);
                size++;
            } else if (key.equals(this.key)) {
                this.value = value;
            } else if (key.compareTo(this.key) < 0) {
                this.left.put(key, value);
            } else {
                this.right.put(key, value);
            }
        }
    }

    public BSTMap() {
        node = new Node(null, null);
    }

    @Override
    public void clear() {
        node = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (node == null) {
            return false;
        }

        return node.containsKey(key);
    }

    @Override
    public V get(K key) {
        if (node == null) {
            return null;
        }

        return node.get(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        node.put(key, value);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new iterator();
    }

    private class iterator implements Iterator<K> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public K next() {
            return null;
        }
    }

    public void printInOrder() {
        for (K i: this) {
            System.out.println(i);
        }
    }
}
