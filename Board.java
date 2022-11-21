/* *****************************************************************************
 *  Name:              Sarah Modhfar
 *  Coursera User ID:  f35521d656432242756e03e85802eb89
 *  Last modified:     November 21, 2022
 **************************************************************************** */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int n;
    private int zeroRow = 0, zeroCol = 0;
    private int hammingDist = 0, manhattanDist = 0;
    private int[] rows, cols;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[this.n][this.n];
        for (int row = 0; row < this.n; row++)
            this.tiles[row] = tiles[row].clone();

        // in order to reduce the num of loops, do calculation in the constructor
        for (int row = 0; row < this.n; row++)
            for (int col = 0; col < this.n; col++) {
                int ele = this.tiles[row][col];

                // calculate hamming distance
                if ((ele != row * n + col + 1) &&
                        !((row == this.n - 1) && (col == this.n - 1))) hammingDist++;

                // locate the blank square
                if (ele == 0) {
                    zeroRow = row;
                    zeroCol = col;
                    continue;
                }

                // calculate manhattan distance
                int realRow = (ele - 1) / this.n;
                int realCol = ele - realRow * this.n - 1;
                manhattanDist += Math.abs(realRow - row) + Math.abs(realCol - col);
            }

        // randomly choose a pair of tiles to generate a twin
        do {
            rows = StdRandom.permutation(this.n, 2);
            cols = StdRandom.permutation(this.n, 2);
        } while ((rows[0] == zeroRow && cols[0] == zeroCol) ||
                (rows[1] == zeroRow && cols[1] == zeroCol));
    }

    // string representation of this board
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(n + "\n");
        for (int[] row : this.tiles) {
            for (int ele : row)
                stringBuilder.append(String.format("%2d ", ele));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingDist == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (!(y.getClass().isInstance(this))) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        if (!Arrays.deepEquals(this.tiles, that.tiles)) return false;
        return true;
    }

    // swap the value of two specified location and returns a copy
    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] copyOfTiles = new int[this.n][this.n];
        for (int row = 0; row < this.n; row++)
            copyOfTiles[row] = this.tiles[row].clone();

        int tmp = copyOfTiles[row1][col1];
        copyOfTiles[row1][col1] = copyOfTiles[row2][col2];
        copyOfTiles[row2][col2] = tmp;

        return copyOfTiles;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neigh = new ArrayList<Board>();

        if (zeroRow > 0)
            neigh.add(new Board(swap(zeroRow, zeroCol, zeroRow - 1, zeroCol)));
        if (zeroRow < this.n - 1)
            neigh.add(new Board(swap(zeroRow, zeroCol, zeroRow + 1, zeroCol)));
        if (zeroCol > 0)
            neigh.add(new Board(swap(zeroRow, zeroCol, zeroRow, zeroCol - 1)));
        if (zeroCol < this.n - 1)
            neigh.add(new Board(swap(zeroRow, zeroCol, zeroRow, zeroCol + 1)));

        return neigh;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return new Board(swap(rows[0], cols[0], rows[1], cols[1]));
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board board = new Board(tiles);

        StdOut.println("The input board: ");
        StdOut.println(board.toString());

        StdOut.println("The dimension: " + board.dimension());
        StdOut.println("The Hamming distance: " + board.hamming());
        StdOut.println("The Manhattan distance: " + board.manhattan());
        StdOut.println("Is the goal board? " + board.isGoal());

        StdOut.println("Neighbours: ");
        for (Board b : board.neighbors())
            StdOut.println(b.toString());

        StdOut.println("A twin board: ");
        StdOut.println(board.twin().toString());
    }
}
