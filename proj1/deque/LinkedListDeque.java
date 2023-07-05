package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        private Node prev;
        private T item;
        private Node next;

        Node(T x, Node p, Node n) {
            item = x;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        sentinel.next = new Node(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size++;
    }

    @Override
    public void addLast(T item) {
        sentinel.prev = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T first = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return first;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T last = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return last;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }

        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }

        return p.item;
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursive(0, index, sentinel.next);
    }

    private T getRecursive(int pos, int index, Node x) {
        if (pos == index) {
            return x.item;
        }
        return getRecursive(pos + 1, index, x.next);
    }

    private class LinkedListDequeIterator<T> implements Iterator<T> {
        private int pos;

        LinkedListDequeIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return (pos < size);
        }

        @Override
        public T next() {
            Node p = sentinel.next;
            for (int i = 0; i < pos; i++) {
                p = p.next;
            }

            T returnItem = (T) p.item;
            pos++;
            return returnItem;
        }
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    public void printDeque() {
        for (int i = 0; i < size - 1; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println(size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Deque)) {
            return false;
        }

        Deque<T> other = (Deque<T>) o;

        if (other.size() != size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }

        return true;
    }
}
