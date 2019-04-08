import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private class Game
    {
        int numMoves;

        Board board;
        Game parent;


        public Game(Board curr, int move)
        {
            board = curr;
            numMoves = move;
        }
    }

    Board startBoard;
    MinPQ<Game> pq;

    public Solver(Board initial)
    {
        if (initial == null)
            throw new IllegalArgumentException("Board cannot be null");
        startBoard = initial;
        if (!startBoard.isSolvable())
            throw new IllegalArgumentException("Board is unsolvable");
        pq = new MinPQ<Game>();
        pq.insert(new Game(startBoard, 0));
    }

    public int moves()
    {
        return -1;
    }

    public Iterable<Board> solution()
    {
        return null;
    }

    public static void main(String[] args)
    {

    }

}
