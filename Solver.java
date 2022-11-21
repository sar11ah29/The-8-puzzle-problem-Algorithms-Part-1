/* *****************************************************************************
 *  Name:              Sarah Modhfar
 *  Coursera User ID:  f35521d656432242756e03e85802eb89
 *  Last modified:     November 21, 2022
 **************************************************************************** */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // the last SearchNode dequeued from the MinPQ
    private SearchNode lastNode;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode previous;  // trace of the solution

        // constructor for root node
        public SearchNode(Board board) {
            this.board = board;
            this.moves = 0;
            this.priority = moves + board.manhattan();
            this.previous = null;
        }

        // constructor for tree node
        public SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            this.moves = previous.moves + 1;
            this.priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board is null.");

        MinPQ<SearchNode> gameTree = new MinPQ<SearchNode>();
        gameTree.insert(new SearchNode(initial));

        MinPQ<SearchNode> twinGameTree = new MinPQ<SearchNode>();
        twinGameTree.insert(new SearchNode(initial.twin()));

        lastNode = gameTree.delMin();
        SearchNode twinLastNode = twinGameTree.delMin();
        while (!lastNode.board.isGoal() && !twinLastNode.board.isGoal()) {
            for (Board neigh : lastNode.board.neighbors()) {
                if ((lastNode.moves == 0) || !neigh.equals(lastNode.previous.board))
                    gameTree.insert(new SearchNode(neigh, lastNode));
            }
            for (Board neigh : twinLastNode.board.neighbors()) {
                if ((twinLastNode.moves == 0) || !neigh.equals(twinLastNode.previous.board))
                    twinGameTree.insert(new SearchNode(neigh, twinLastNode));
            }
            lastNode = gameTree.delMin();
            twinLastNode = twinGameTree.delMin();
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (lastNode.board.isGoal()) return true;
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return lastNode.moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        // need a stack to output the solution in a reversed order
        Stack<Board> solution = new Stack<Board>();
        SearchNode current = lastNode;
        while (current.previous != null) {
            solution.push(current.board);
            current = current.previous;
        }
        solution.push(current.board);
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
