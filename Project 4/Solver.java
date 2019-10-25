import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.
public class Solver {
    private LinkedStack<Board> solution;
    private int moves;

    // Helper search node class.
    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previous;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board =  board;
            this.moves = moves;
            this.previous = previous;
        }
    }
     
    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        if (!initial.isSolvable()) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        solution = new LinkedStack<Board>();
        pq.insert(new SearchNode(initial, 0, null));

        while (!pq.isEmpty()) {
            SearchNode sn = pq.delMin();
            if (sn.board.isGoal()) {

                this.moves = sn.moves;
                while (sn != null) {
                    solution.push(sn.board);
                    sn = sn.previous;
                }
                //return;
            }
            for (Board after: sn.board.neighbors()) {
                if (sn.previous == null || !(after.equals(sn.previous.board))) {
                    pq.insert(new SearchNode(after, sn.moves + 1, sn));
                }
            }
        }
    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return moves;
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        return solution;
    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int d = (a.board.hamming() + a.moves) - (b.board.hamming() + b.moves);    
            if (d != 0) {
                return d;
            }
            if (a.moves > b.moves) {
                return -1;
            }
            return 1;
        }
    }
    
    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int d = (a.board.manhattan() + a.moves) - (b.board.manhattan() + b.moves);
            if (d != 0) {
                return d;
            }
            if (a.moves > b.moves) {
                return -1;
            }
            return 1;
        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
        else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}