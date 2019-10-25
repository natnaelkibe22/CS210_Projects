import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private boolean[][] open;
    private int sitesOpen;
    private WeightedQuickUnionUF unionFind;
    private int source;
    private int sink;
 
    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        this.N = N;
        open = new boolean[this.N][this.N];
        unionFind = new WeightedQuickUnionUF(this.N * this.N + 2);
        sink = this.N * this.N + 1;
        source = 0;

        if (this.N <= 0) {
            throw new IllegalArgumentException();
        }
       
        for (int k = 0; k <= N; k++) {
            unionFind.union(encode(0, k), source);
            unionFind.union(encode(this.N - 1, k), sink);
        }
    }

    // Open site (i, j) if it is not open already.
    public void open(int i, int j) {
        if (i < 0 || i > this.N - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (j < 0 || j > this.N - 1) {
            throw new IndexOutOfBoundsException();
        }
         
        if (!isOpen(i, j)) {
            open[i][j] = true;
            sitesOpen += 1;
        }
        if (i + 1 < N && open[i + 1][j]) {
            unionFind.union(encode(i, j), encode(i + 1, j)); 
        }
        if (i - 1 >= 0 && open[i-1][j]) {
            unionFind.union(encode(i, j), encode(i - 1, j));
        }
        if (j + 1 < N && open[i][j + 1]) {
            unionFind.union(encode(i, j), encode(i, j + 1));
        }
        if (j - 1 >= 0 && open[i][j - 1]) {
            unionFind.union(encode(i, j), encode(i, j - 1));
        }
    }

    // Is site (i, j) open?
    public boolean isOpen(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1)
        {
            throw new IndexOutOfBoundsException();
        }
        if (open[i][j]) {
            return true;
        }
        return false;
    }

    // Is site (i, j) full?
    public boolean isFull(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1)
        {
            throw new IndexOutOfBoundsException();
        }
        if (open[i][j] && unionFind.connected(encode(i, j), source)) {
            return true;
        }
        return false;
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return sitesOpen;
    }

    // Does the system percolate?
    public boolean percolates() {
        if (unionFind.connected(source, sink)) {
            return true;
        }
        return false;
    }

    // An integer ID (1...N) for site (i, j).
    private int encode(int i, int j) {
        return (i * N + j + 1);
    }


    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }
        
        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}