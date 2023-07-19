package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int tablesize;
    private int size;
    private double loadFactor;
    private Set<K> hashkey;

    /** Constructors */
    public MyHashMap() {
        tablesize = 16;
        size = 0;
        loadFactor = 0.75;
        hashkey = new HashSet<>();
        buckets = createTable(tablesize);
        for (int i = 0; i < tablesize; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        tablesize = initialSize;
        size = 0;
        loadFactor = 0.75;
        hashkey = new HashSet<>();
        buckets = createTable(tablesize);
        for (int i = 0; i < tablesize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        tablesize = initialSize;
        size = 0;
        loadFactor = maxLoad;
        hashkey = new HashSet<>();
        buckets = createTable(tablesize);
        for (int i = 0; i < tablesize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        buckets = null;
        tablesize = 0;
        size = 0;
        hashkey.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return hashkey.contains(key);
    }

    @Override
    public V get(K key) {
        if (buckets == null) {
            return null;
        }

        int hashnum = key.hashCode() % tablesize;
        if (hashnum < 0) {
            hashnum += tablesize;
        }

            for (Node i: buckets[hashnum]) {
                if (i.key.equals(key)) {
                    return i.value;
                }
            }

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int hashnum = key.hashCode() % tablesize;
        if (hashnum < 0) {
            hashnum += tablesize;
        }

        if (hashkey.contains(key)) {
            for (Node i: buckets[hashnum]) {
                if (i.key.equals(key)) {
                    i.value = value;
                    break;
                }
            }
        } else {
            Node n = createNode(key, value);
            buckets[hashnum].add(n);
            hashkey.add(key);
            size++;
        }
    }

    @Override
    public Set<K> keySet() {
        return hashkey;
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

    private class iterator<K> implements Iterator<K> {
        int pos;
        int tablepos;
        int bucketpos;

        @Override
        public boolean hasNext() {
            return (pos < size);
        }

        @Override
        public K next() {
            pos++;
            int x = 0;
            Node terget = null;
            for (Collection<Node> i: buckets) {
                x += i.size();
                if (x > pos) {
                    x -= pos;
                    for (int j = 0; j < x; j++) {
                        terget = i.iterator().next();
                    }
                    return (K) terget.key;
                }
            }

            return null;
        }

        public iterator() {
            pos = 0;
            tablepos = 0;
            bucketpos = 0;
        }
    }
}
