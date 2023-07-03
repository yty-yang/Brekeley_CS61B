package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] array;
    private int size;
    private int front;
    private int end;
    private int arraysize;

    public ArrayDeque() {
        array = (T[]) new Object[8];
        arraysize = 8;
        size = 0;
        front = -1;
        end = -1;
    }

    public ArrayDeque(ArrayDeque other) {
        array = (T[]) new Object[other.size];
        arraysize = other.size;
        size = other.size;
        front = 0;
        end = size - 1;

        int index = 0;

        if (other.front <= other.end) {
            for (int i = other.front; i <= other.end; i++) {
                array[index] = (T) other.get(i);
                index++;
            }
        } else {
            for (int i = front; i < arraysize; i++) {
                array[index] = (T) other.get(i);
                index++;
            }
            for (int i = 0; i <= end; i++) {
                array[index] = (T) other.get(i);
                index++;
            }
        }
    }

    @Override
    public void addFirst(T item) {
        size++;
        resizing();

        if (front == 0) {
            front = arraysize - 1;
        } else if (front == -1) {
            front = 0;
            end = 0;
        } else {
            front--;
        }

        array[front] = item;
    }

    @Override
    public void addLast(T item) {
        size++;
        resizing();

        if (end == arraysize - 1) {
            end = 0;
        } else if (end == -1) {
            end = 0;
            front = 0;
        } else {
            end++;
        }
        array[end] = item;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (front <= end) {
            for (int i = front; i < end; i++) {
                System.out.print(array[i] + " ");
            }
            System.out.println(array[end]);
        } else {
            for (int i = front; i < arraysize; i++) {
                System.out.print(array[i] + " ");
            }
            for (int i = 0; i < end; i++) {
                System.out.print(array[i] + " ");
            }
            System.out.println(array[end]);
        }
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        size--;
        resizing();

        T temp = array[front];
        array[front] = null;
        if (front == arraysize - 1) {
            front = 0;
        } else {
            front++;
        }
        return temp;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        size--;
        resizing();

        T temp = array[end];
        array[end] = null;
        if (end == 0) {
            end = arraysize - 1;
        } else {
            end--;
        }
        return temp;
    }

    private void resizing() {
        T[] temp;
        int index = 0;

        if (arraysize < size) {
            temp = (T[]) new Object[arraysize * 2];

            if (front <= end) {
                for (int i = front; i <= end; i++) {
                    temp[index] = array[i];
                    index++;
                }
            } else {
                for (int i = front; i < arraysize; i++) {
                    temp[index] = array[i];
                    index++;
                }
                for (int i = 0; i <= end; i++) {
                    temp[index] = array[i];
                    index++;
                }
            }

            array = temp;
            front = 0;
            end = size - 2;
            arraysize = array.length;
        }

        if (arraysize > size * 4 && arraysize >= 16) {
            temp = (T[]) new Object[arraysize / 2];

            if (front <= end) {
                for (int i = front; i <= end; i++) {
                    temp[index] = array[i];
                    index++;
                }
            } else {
                for (int i = front; i < arraysize; i++) {
                    temp[index] = array[i];
                    index++;
                }
                for (int i = 0; i <= end; i++) {
                    temp[index] = array[i];
                    index++;
                }
            }

            array = temp;
            front = 0;
            end = size;
            arraysize = array.length;
        }
    }

    @Override
    public T get(int index) {
        if (front + index < arraysize) {
            return array[front + index];
        } else {
            return array[front + index - arraysize];
        }
    }

    class iterator implements Iterator<T> {
        private int pos;

        @Override
        public boolean hasNext() {
            return (pos < size);
        }

        @Override
        public T next() {
            T returnItem = array[front + pos];
            pos++;
            return returnItem;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new iterator();
    }
//
//    public boolean equals(Object o) {
//
//    }
}
