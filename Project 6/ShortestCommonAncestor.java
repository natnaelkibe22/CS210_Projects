import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

// An immutable data type for computing shortest common ancestors.
public class ShortestCommonAncestor {
    private Digraph G;

    // Construct a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) {
            throw new NullPointerException();
        }
        this.G = G;
    }

    // Length of the shortest ancestral path between v and w.
   public int length(int v, int w) {
        if (v < 0 || w < 0 || v > G.V() || w > G.V()) {
            throw new IndexOutOfBoundsException();
        }
        return shortest(v, w, true);
        }

    // Shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v > G.V() || w > G.V()) {
            throw new IndexOutOfBoundsException();
        }

        return shortest(v, w, false);
    }

    // Length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null || B == null) {
            throw new NullPointerException();
        }
        for (Integer x: A) {
            if (x == 0) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer x: B) {
            if (x == 0) {
                throw new IllegalArgumentException();
            }
        }
        return shortest(A, B, true);
    }

    // A shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null || B == null ) {
            throw new NullPointerException();
        }
        for (Integer x: A) {
            if (x == 0) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer x: B) {
            if (x == 0) {
                throw new IllegalArgumentException();
            }
        }
        return shortest(A, B, false);
    }
    //Helper for computing shortest ancestor and path
    private int shortest(int v, int w, boolean t) {
        int min = Integer.MAX_VALUE;
        int mina = -1;
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, w);
        LinkedQueue<Integer> que = new LinkedQueue<Integer>();
        Boolean[] marked = new Boolean[G.V()];
        int[] distTo = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            marked[i] = false;
            distTo[i] = Integer.MAX_VALUE;
        }
        que.enqueue(v);
        marked[v] = true;
        distTo[v] = 0;
        while (!que.isEmpty()) {
            int curr = que.dequeue();
            if (min < distTo[curr]) break;
            if (bfs.hasPathTo(curr) && (bfs.distTo(curr) + distTo[curr] < min)) {
                min = bfs.distTo(curr) + distTo[curr];
                mina = curr;
            }
            for (int x: G.adj(curr)) {
                if (!marked[x]) {
                    distTo[x] = distTo[curr] + 1;
                    marked[x] = true;
                    que.enqueue(x);
                }
            }
        }
        if (t) {
            if (min < Integer.MAX_VALUE) {
                return min;
            }
            return -1;
        }
        return mina;
    }
    private int shortest(Iterable<Integer> v, Iterable<Integer> w, boolean t) {
         BreadthFirstDirectedPaths dist2w = new BreadthFirstDirectedPaths(G, w);
        int min = Integer.MAX_VALUE;
        int mina = -1;
        Boolean[] marked = new Boolean[G.V()];
        int[] distTo = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            marked[i] = false;
            distTo[i] = Integer.MAX_VALUE;
        }
        LinkedQueue<Integer> que = new LinkedQueue<Integer>();
        for (int vertex: v) {
            marked[vertex] = true;
            distTo[vertex] = 0;
            que.enqueue(vertex);
        }
        while (!que.isEmpty()) {
            int curr = que.dequeue();
            if (min < distTo[curr]) break;
            if (dist2w.hasPathTo(curr) && (dist2w.distTo(curr) + distTo[curr] < min)) {
                min = dist2w.distTo(curr) + distTo[curr];
                mina = curr;
            }
            for (int x: G.adj(curr)) {
                if (!marked[x]) {
                    distTo[x] = distTo[curr] + 1;
                    marked[x] = true;
                    que.enqueue(x);
                }
            }
        }

        if (t) {
            if (min < Integer.MAX_VALUE) 
                return min;
            else return -1;
        }
        return mina;
    }
    // Helper: Return a map of vertices reachable from v and their 
    // respective shortest distances from v.
    /*private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> st = new SeparateChainingHashST<Integer, Integer>();
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, v);
       for (int i = 0; i < G.V(); i++) {
        if (i = v) {
            continue;
        }
        st.put(i, bfs.distTo(i));
       }   
    }*/

    // Helper: Return an array consisting of a shortest common ancestor a 
    // of vertex subsets A and B, and vertex v from A and vertex w from B 
    // such that the path v-a-w is the shortest ancestral path of A and B.
   /* private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        
    }*/

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}