import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private int T;
    private double[] p;
    private Percolation pr;
    // Perform T independent experiments (Monte Carlo simulations) on an 
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        this.T = T;
        p = new double[this.T];
        
        if (this.T <= 0 || N <= 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < this.T; i++) {
            pr = new Percolation(N);
            double openedSites = 0;
            while (!pr.percolates()) {
                int a = StdRandom.uniform(0, N);
                int b = StdRandom.uniform(0, N);
                if (!pr.isOpen(a, b)) {
                    pr.open(a, b);
                    openedSites++;
                }
            }
            p[i] = openedSites/(double) (N*N);
        }
    }
    
    // Sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(p);
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(p);
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return (mean() - (1.96*stddev() / Math.sqrt(T)));
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return (mean() + (1.96*stddev() / Math.sqrt(T)));
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}