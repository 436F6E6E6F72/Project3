import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.List;

public class Solver {

    private class BoardIterable implements Iterable<Board> {

        private List<Board> boardList;
        private int index;

        public BoardIterable(List<Board> boards)
        {
            boardList = boards;
            index = 0;
        }

        public boolean hasNext()
        {
            return (index != boardList.size() - 1);
        }

        public Board next()
        {
            index++;
            return boardList.get(index);
        }

        public Iterator<Board> iterator()
        {
            return boardList.iterator();
        }

    }

    /**
     * Helper class, contains each board at each step, the step number, and the parent
     */
    private class Game
    {
        int numMoves;
        int priority;
        Board board;
        Game parent;


        public Game(Board curr, int move, Game parentNode)
        {
            board = curr;
            numMoves = move;
            parent = parentNode;
            priority = numMoves + board.manhattan();
        }
    }

    Board startBoard;

    /**
     * Base constructor
     * @param initial starting board to solve from
     */
    public Solver(Board initial)
    {
        if (initial == null)
            throw new IllegalArgumentException("Board cannot be null");
        startBoard = initial;
        if (!startBoard.isSolvable())
            throw new IllegalArgumentException("Board is unsolvable");
        solveAStar();
    }

    /**
     * A* Solving algorithm
     * @param initial starting board to solve from
     */
    private void solveAStar(Board initial)
    {
        MinPQ<Game> closedSet = new MinPQ<Game>(); // Already evaluated games
        MinPQ<Game> openSet = new MinPQ<Game>(); // Discovered games
        Game parentGame; // The parent of the current open set
        Game current = null;
        int gScore;

        boolean solved = false;
        while (!solved)
        {
            // If the current nod is null then we need to init it
            if (current == null)
                current = new Game(initial, 0, null);
            else if (current.board.isGoal())
            {
                // TODO: WHAT DO WHEN DONE
                solved = true;
            }
            else // Else find the lowest in the open set
            {
                Game lowest = null;
                for(Game game : openSet)
                {
                    if (lowest == null || lowest.priority > game.priority)
                    {
                        lowest = game;
                    }
                }
            }

            openSet.delMin(); // I THINK THIS IS WRONG
            closedSet.insert(current);

            // Iterate through all neighbors, remove duplicates, find best
            for(Board neightbot: current.board.neighbors())
            {

            }

        }

    }

    /**
     * The number of moves the start board took to solve
     * @return int number of moves
     */
    public int moves()
    {
        return -1;
    }

    /**
     * Sequence of boards in the shortest solution
     * @return Iterable of boards
     */
    public Iterable<Board> solution()
    {
        return null;
    }

    public static void main(String[] args)
    {

    }

}
