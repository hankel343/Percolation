/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;                    //lattice (0 blocked, 1 open)
    private WeightedQuickUnionUF uf;            //Union-Find
    private int open_cnt = 0;                   //Track # open cells
    private int topSite, bottomSite;            //Virtual sites for Percolates()

    //Creates n-by-n grid, with all sites initially blocked
    //O(n^2)
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        grid = new boolean[n][n];
        topSite = n * n;
        bottomSite = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grid[i][j] = false;
    }

    //opens the site (row, col) if it is not open already
    //O(1)
    public void open(int row, int col) {
        if (row < 1 || row > grid.length) throw new IllegalArgumentException();
        else if (col < 1 || col > grid.length) throw new IllegalArgumentException();

        grid[row - 1][col - 1] = true;                 //Open cell
        open_cnt++;

        checkAdjacent(row - 1, col - 1);
    }

    // Check for adjacent open cells to merge into one set
    // O(1)
    private void checkAdjacent(int i, int j) {

        //ABOVE
        if (i != 0 && grid[i - 1][j]) {
            uf.union((i - 1) * grid.length + j, i * grid.length + j);
        }

        //TOP VIRTUAL
        if (i == 0 && grid[i][j]) {
            uf.union(topSite, j);
        }

        //BOTTOM
        if (i != (grid.length - 1) && grid[i + 1][j]) {
            uf.union((i + 1) * grid.length + j, i * grid.length + j);
        }

        //BOTTOM VIRTUAL
        if (i == grid.length - 1 && grid[i][j] && uf.find(bottomSite) == bottomSite) {
            uf.union(i * grid.length + j, bottomSite);
        }

        //LEFT
        if (j != 0 && grid[i][j - 1]) {
            uf.union(i * grid.length + j, i * grid.length + (j - 1));
        }

        //RIGHT
        if (j + 1 != grid.length && grid[i][j + 1]) {
            uf.union(i * grid.length + j, i * grid.length + (j + 1));
        }
    }

    //is the site (row, col) open?
    //O(1)
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > grid.length) throw new IllegalArgumentException();
        else if (col < 1 || col > grid.length) throw new IllegalArgumentException();

        return (grid[row - 1][col - 1]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > grid.length) throw new IllegalArgumentException();
        else if (col < 1 || col > grid.length) throw new IllegalArgumentException();

        return (uf.find((row - 1) * grid.length + (col - 1)) == topSite);
    }

    //returns the number of open sites
    public int numberOfOpenSites() {
        return open_cnt;
    }

    //Does the system percolate?
    //O(1)
    public boolean percolates() {
        return uf.find(bottomSite) == uf.find(topSite);
    }

    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]);
        int row, col;
        Percolation percolation = new Percolation(N);
        while (percolation.open_cnt != N * N) {
            row = StdRandom.uniform(1, N + 1);
            col = StdRandom.uniform(1, N + 1);

            if (percolation.isOpen(row, col)) continue;
            percolation.open(row, col);
        }
    }
}
