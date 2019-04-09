import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
            if (y == null || y.getClass() != this.getClass()) // Null or different class
                return -1;
            Game testGame = (Game)y;
            // Check for size
            if (testGame.board.size() != board.size())
                return -1;
            // Check for tile equality
            int length = (int)Math.sqrt(testGame.board.size());
            for (int i = 0; i < length; i++)
            {
                for (int j = 0; j < length; j++)
                {
                    if (testGame.board.tileAt(i, j) != board.tileAt(i, j))
                        if (testGame.board.tileAt(i, j) > board.tileAt(i, j))
                            return -1;
                        else
                            return 1;
                }
            }
            return 0;
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
        MinPQ<Game> closedSet = new MinPQ<Game>(); // Already evaluated games
        MinPQ<Game> openSet = new MinPQ<Game>(); // Discovered games
        Game parentGame; // The parent of the current open set
        openSet.insert(new Game(initial, 0, null)); // Start off with the initial
        Game current = null;
        int gScore;

        while (openSet.size() != 0)
        {

            if (current != null && current.board.isGoal())
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

            // Get the lowest from the open set
            current = openSet.delMin();
            // Add it to the closed set
            closedSet.insert(current);

            StdOut.println(current.board.toString());

            // Iterate through all neighbors, remove duplicates, find best
            for(Board neighbor: current.board.neighbors())
            {
                if (closedSet.size() > 0)
                {
                    boolean equal = false;
                    for (Game closed: closedSet) // Check for neighbor in closed
                        if (closed.board.equals(neighbor))
                            equal = true;
                    if (equal)
                        continue;
                }
//                if (neighbor.manhattan() + 1 + current.numMoves > current.priority)
//                    continue;

                if (openSet.size() > 0)
                {
                    boolean equal = false;
                    for (Game open: openSet) // Check for neighbor in open
                        if (open.board.equals(neighbor))
                            equal = true;
                    if (!equal)
                        openSet.insert(new Game(neighbor, current.numMoves + 1, current));
                }
                else
                    openSet.insert(new Game(neighbor, current.numMoves + 1, current));
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
        int[][] testVals = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
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
