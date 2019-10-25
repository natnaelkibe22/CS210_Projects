import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// Takes a command-line integer k; reads in a sequence of strings from 
// standard input; and prints out exactly k of them, uniformly at random. 
// Note that each item from the sequence is printed out at most once.
public class Subset {
    public static void main(String[] args) {
        int k = new Integer(args[0]);
        ResizingArrayRandomQueue<String> q;
        q = new ResizingArrayRandomQueue<String>();
        while (!StdIn.isEmpty()) {
        	q.enqueue(StdIn.readString());
        }
        //print k entries
        int i = 0;
        for (String s : q) {
        	if (i >= k) {
        		break;
        	}
        	StdOut.println(s);
        	q.dequeue();
        	i++;
        }
    }
}