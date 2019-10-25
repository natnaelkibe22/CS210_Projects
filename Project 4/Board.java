import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private int[][] tiles;
    private int N;
    private int hamming;
    private int manhattan;
    private int bPos;
    private int bI;
    private int bJ;
    // Construct a board from an N-by-N array of tiles, where 
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank 
    // square.
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new NullPointerException();
        } 
        int a = 0;
        N = tiles.length;
        hamming = -1; manhattan = -1;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[j][j] == 0) {
                    bI = i;
                    bJ = j;
                    bPos = a;
                }
                a++;
            }
        }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }
    
    // Size of this board.
    public int size() {
        return N;
    }

    // Number of tiles out of place.
    public int hamming() {
        if (hamming != -1) {
            return hamming;
        }
        int hamming = 0;
        for (int i = 0; i < N; ++i){
            for (int j = 0; j < N; ++j) {
                if ((i == N - 1) && (j == N - 1)) {
                    continue;
                } 
                else if (tiles[i][j] != N * i + j + 1) {
                    ++hamming;
                }
            }
        }
        return hamming;
    }
    //changed
    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        if (manhattan != -1) {
            return manhattan;
        }
        int manhattan = 0;
        int a = 0;
        int b = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                a = tiles[i][j] % N;
                b = tiles[i][j] / N;
                if (a == 0) {
                    a = N - 1;
                    --b;
                } 
                else {
                    --a;
                }
                manhattan += Math.abs(b - i) + Math.abs(a - j);
            }
        }
        return manhattan;
       
    }
    //changed
    // Is this board the goal board?
    public boolean isGoal() {
       return (hamming == 0);
    }
    //changed

    // Is this board solvable?
    public boolean isSolvable() {
        int invert = 0;
        int tile[] = tiler();
        invert = inversions();

        boolean evenBoard = (N % 2) == 0;
        if (evenBoard) {
            invert += blankPos() / N;
        }
        boolean isEvenInvert = (invert % 2) == 0;
        return evenBoard != isEvenInvert;
    }
 
    // Does this board equal that?
    public boolean equals(Board that) {
        if (that == null)
            return false;
        if (!that.equals(this))
            return false;
        if (this.N != that.N)
            return false;
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j) {
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> n = new LinkedQueue<Board>();
        if (bPos / N != 0) {
            pushStack(n, -N);
        }
        if (bPos / N != N - 1) {
            pushStack(n, N);
        }
        if (bPos % N != 0) {
            pushStack(n, -1);
        }
        if (bPos % N != N - 1) {
            pushStack(n, 1);
        }
        return n;
    }

    //Helper method push Stack
    private void pushStack(LinkedQueue<Board> n, int displace) {
        int tile[] = tiler();
        int a = 0;
        int swap = tile[bPos];
        tile[bPos] = tile[bPos + displace];
        tile[bPos + displace] = swap;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = a;
                a++;
            }
        }
        n.enqueue(new Board(tiles));
        int swap2 = tile[bPos];
        tile[bPos] = tile[bPos + displace];
        tile[bPos + displace] = swap2;
    }

    //row major helper or changes tiles into one dimensional and easily working array
    private int[] tiler() {
        int a = 0;
        int tile[] = new int[N*N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tile[a++] = tiles[i][j];
            }
        }
        return tile;
    }

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

 
    private Board (int[][] tiles, int N, int bPos) {
        this.N = N;
        this.bPos = bPos;
        
    }
    // Helper method that returns the position (in row-major order) of the 
    // blank (zero) tile.
    private int blankPos() {
        int a = 0;
        int[] tile = tiler();
        for (int i = 0; i < N; i++) {
            if (tile[i] == 0) {
                a = i;
            }
        }

        
        return a;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int[] tile = {1, 0, 3, 4, 2, 5, 7, 8, 6};
        int inversions = 0;
        for (int i = 0; i < N; i++) {
            if (tile[i] == 0) {
                continue;
            }
            for (int j = i; j > 0; j--) {
                if (tile[j] < tile[i] && tile[j] != 0) {
                inversions++;
                }
            }
        }
        return inversions;
        }
    // Helper method that clones the tiles[][] array in this board and 
    // returns it.
    private int[][] cloneTiles() {
        int[][] cloned = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                cloned[i][j] = this.tiles[i][j];
            }
        }
        return cloned;
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
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        StdOut.println(board.inversions());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}