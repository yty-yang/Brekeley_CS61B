package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    comparator<T> comparator;

    public Comparator<T> getComparator() {
        return new comparator();
    }

    private class comparator<T> implements Comparator<T> {
        public int compare(T a, T b) {
            return 0;
        }
    }

    public MaxArrayDeque(comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        if (this.isEmpty()) {
            return null;
        }

        T max = this.get(0);

        for (int i = 0; i < this.size(); i++) {
            if (comparator.compare(max, this.get(i)) < 0) {
                max = this.get(i);
            }
        }
        return max;
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }

        T max = this.get(0);
        for (int i = 0; i < this.size(); i++) {
            if (c.compare(max, this.get(i)) < 0) {
                max = this.get(i);
            }
        }
        return max;
    }
}
