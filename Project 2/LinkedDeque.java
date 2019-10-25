import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Deque implementation using a linked list.
public class LinkedDeque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int N;

    // Helper doubly-linked list class.
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // Construct an empty deque.
    public LinkedDeque() {
        first = null;
        last = null;
        N = 0;
    }

    // Is the dequeue empty?
    public boolean isEmpty() {
        return first == null;
    }

    // The number of items on the deque.
    public int size() {
        return N;
    }

    // Add item to the front of the deque.
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        if (oldfirst == null) {
            first.next = null;
        }
        else {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
        if (last == null) {
            last = first;
        }
        N++;
    }

    // Add item to the end of the deque.
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node oldlast = last;
        last = new Node();
        if (oldlast == null) {
            last.prev = null;
        }
        else {
            last.prev = oldlast;
            oldlast.next = last;
        }
        last.next = null;
        last.item = item;
        if (first == null) {
            first = last;
        }
        N++;
    }

    // Remove and return item from the front of the deque.
    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        if (first != null && first.prev != null) {
            first.prev = null;
        }
        if (first == null) {
            last = null;
        }
        N--;
        return item;
    }

    // Remove and return item from the end of the deque.
    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        Item item = last.item;
        last = last.prev;
        if (last != null && last.next != null) {
            last.next = null;
        }
        if (last == null) {
            first = null;
        }
        N--;
        return item;
    }

    // An iterator over items in the queue in order from front to end.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    // An iterator, doesn't implement remove() since it's optional.
    private class DequeIterator implements Iterator<Item> {
        private Node current;
        
        DequeIterator() {
            current = first;
        }
        public boolean hasNext()  { 
            return current != null;
        }

        public void remove() { 
            throw new UnsupportedOperationException(); 
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            else {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }
    }

    // A string representation of the deque.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item + " ");
        }
        return s.toString().substring(0, s.length() - 1);
    }
    
    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        LinkedDeque<Character> deque = new LinkedDeque<Character>();
        String quote = "There is grandeur in this view of life, with its " 
            + "several powers, having been originally breathed into a few " 
            + "forms or into one; and that, whilst this planet has gone " 
            + "cycling on according to the fixed law of gravity, from so " 
            + "simple a beginning endless forms most beautiful and most " 
            + "wonderful have been, and are being, evolved. ~ " 
            + "Charles Darwin, The Origin of Species";
        int r = StdRandom.uniform(0, quote.length());
        for (int i = quote.substring(0, r).length() - 1; i >= 0; i--) {
            deque.addFirst(quote.charAt(i));
        }
        for (int i = 0; i < quote.substring(r).length(); i++) {
            deque.addLast(quote.charAt(r + i));
        }
        StdOut.println(deque.isEmpty());
        StdOut.printf("(%d characters) ", deque.size());
        for (char c : deque) {
            StdOut.print(c);
        }
        StdOut.println();
        double s = StdRandom.uniform();
        for (int i = 0; i < quote.length(); i++) {
            if (StdRandom.bernoulli(s)) {
                deque.removeFirst();
            }
            else {
                deque.removeLast();
            }
        }
        StdOut.println(deque.isEmpty());
    }
}