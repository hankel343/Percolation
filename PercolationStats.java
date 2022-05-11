/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int numTrials;
    private double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        numTrials = trials;
        thresholds = new double[trials];
        int row, col;
        Percolation percolation;
        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);

                if (percolation.isOpen(row, col)) continue;
                percolation.open(row, col);
            }
            thresholds[i] = ((double) percolation.numberOfOpenSites() / (n * n));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (thresholds.length == 1) return 0;
        else return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(numTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(numTrials);
    }

    // test client
    public static void main(String[] args) {
        PercolationStats PercStats = new PercolationStats(Integer.parseInt(args[0]),
                                                          Integer.parseInt(args[1]));

        StdOut.println("Mean\t = " + PercStats.mean());
        StdOut.println("stddev\t = " + PercStats.stddev());
        StdOut.println(
                "95% confidence interval = " + "[ " + PercStats.confidenceLo() + ", " + PercStats
                        .confidenceHi() + "]");
    }
}
