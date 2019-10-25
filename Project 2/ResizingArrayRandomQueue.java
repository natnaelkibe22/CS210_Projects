import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
// Random queue implementation using a resizing array.
public class ResizingArrayRandomQueue<Item> implements Iterable<Item> {
    private  Item[] q;
    private  int N;
    // Construct an empty queue.
    public ResizingArrayRandomQueue() {
        q = (Item[]) new Object[2];
    }

    // Is the queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // The number of items on the queue.
    public int size() {
        return N;
    }

    // Add item to the queue.
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (N == q.length) { resize(2 * q.length); }
        q[N++] = item;
    }

    // Remove and return a random item from the queue.
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int random = StdRandom.uniform(0, N);
        Item item = q[random];
        q[random] = q[N - 1];
        q[N - 1] = null;
        if (N > 0 && N == q.length / 4) resize(q.length / 2);
        N--;
        return  item;
    }

    // Return a random item from the queue, but do not remove it.
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int random = StdRandom.uniform(0, N);
        Item item = q[random];
        return item;
    }

    // An independent iterator over items in the queue in random order.
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    
     // An iterator, doesn't implement remove() since it's optional.
    private class RandomQueueIterator implements Iterator<Item> {
        private Item[] items;
        private int current;
        
        RandomQueueIterator() {
            items = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) {
                items[i] = q[i];
            }
            StdRandom.shuffle(items);
            current = 0;
        }
        
        public boolean hasNext() { return current < items.length; }

        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) { throw new NoSuchElementException(); }
            return items[current++];
        }
    }

    // A string representation of the queue.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item + " ");
        }
        return s.toString().substring(0, s.length() - 1);
    }

    // Helper method for resizing the underlying array.
    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            if (q[i] != null) {
                temp[i] = q[i];
            }
        }
        q = temp;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        ResizingArrayRandomQueue<Integer> q = 
            new ResizingArrayRandomQueue<Integer>();
        while (!StdIn.isEmpty()) {
            q.enqueue(StdIn.readInt());
        }
        int sum1 = 0;
        for (int x : q) {
            sum1 += x;
        }
        int sum2 = sum1;
        for (int x : q) {
            sum2 -= x;
        }
        int sum3 = 0;
        while (q.size() > 0) {
            sum3 += q.dequeue();
        }
        StdOut.println(sum1);
        StdOut.println(sum2);
        StdOut.println(sum3);
        StdOut.println(q.isEmpty());
    }
}