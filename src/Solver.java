import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.MinPQ;

import javax.crypto.MacSpi;
import java.util.*;

public class Solver {

    private class BoardIterable implements Iterable<Board> {

        private List<Board> boardList;
        private int index;

        public BoardIterable(List<Board> boards)
        {
            boardList = new ArrayList<>();
            for(int i = boards.size() - 1; i > -1; i--)
                boardList.add(boards.get(i));
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
    private class Game implements Comparable<Object>
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

        @Override
        public boolean equals(Object y)
        {
            if (this == y) // Same ref
                return true;
            if (y == null || y.getClass() != this.getClass()) // Null or different class
                return false;
            Board testBoard = (Board)y;
            // Check for size
            if (testBoard.size() != board.size())
                return false;
            // Check for tile equality
            for (int i = 0; i < board.size(); i++)
            {
                for (int j = 0; j < board.size(); j++)
                {
                    if (testBoard.tileAt(i, j) != board.tileAt(i, j))
                        return false;
                }
            }
            return true;
        }

        @Override
        public int compareTo(Object y)
        {
            if (this == y) // Same ref
                return 0;
            if (y == null)
                return -1;
            Game testGame = (Game)y;
            if (this.priority < testGame.priority)
                return -1;
            else if (this.priority == testGame.priority)
                return 0;
            else
                return 1;
        }
    }

    private Board startBoard;
    private BoardIterable solvedSequence;
    private int movesTaken;

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
        movesTaken = 0;
        solveAStar(initial);
    }

    /**
     * A* Solving algorithm
     * @param initial starting board to solve from
     */
    private void solveAStar(Board initial)
    {
        MinPQ<Game> closedSet = new MinPQ<>();
        MinPQ<Game> openSet = new MinPQ<>();

        Game current = new Game(initial, 0, null);

        openSet.insert(current);

        while (openSet.size() > 0)
        {
            current = openSet.delMin();
            closedSet.insert(current);

            if (current.board.isGoal())
            {
                List<Board> solvedList = new ArrayList<>();
                while (current != null)
                {
                    solvedList.add(current.board);
                    current = current.parent;
                    movesTaken++;
                }
                solvedSequence = new BoardIterable(solvedList);
                break;
            }
            for (Board neighbor : current.board.neighbors())
            {
                boolean alreadyStored = false;
                for(Game closed : closedSet)
                    if (closed.board.equals(neighbor))
                        alreadyStored = true;
                if (alreadyStored)
                    continue;
                for(Game open : openSet)
                    if (open.board.equals(neighbor))
                        alreadyStored = true;
                if (!alreadyStored)
                    openSet.insert(new Game(neighbor, current.numMoves + 1, current)); // Discover it
                else
                    for(Game open : openSet)
                        if (open.board.equals(neighbor) && current.numMoves + 1 < open.numMoves)
                            break;
                        else if (open.board.equals(neighbor) && current.numMoves + 1 > open.numMoves)
                        {
                            open.numMoves = current.numMoves + 1;
                            open.parent = current;
                            open.priority = open.numMoves + open.board.manhattan();
                        }

            }
        }
    }

    /**
     * The number of moves the start board took to solve
     * @return int number of moves
     */
    public int moves()
    {
        return movesTaken;
    }

    /**
     * Sequence of boards in the shortest solution
     * @return Iterable of boards
     */
    public Iterable<Board> solution()
    {
        return solvedSequence;
    }

    public static void main(String[] args)
    {
        //int[][] testVals = {{1, 2, 3}, {4, 5, 0}, {7, 8, 6}};
        //int[][] testVals = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] testVals = {{1, 2, 3, 4}, {5, 6, 0, 8}, {9, 10, 7, 11}, {13, 14, 15, 12}};
        Board testBoard = new Board(testVals);
        StdOut.println("Starting with: \n" + testBoard.toString());
        Solver testSolver = new Solver(testBoard);
        for(Board board: testSolver.solution())
        {
            StdOut.println(board.toString());
        }
        StdOut.println("Took " + testSolver.moves() + " moves to solve");

    }

}
