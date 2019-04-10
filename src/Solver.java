import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import java.util.*;

public class Solver {

    /**
     * Iterable class for the final set of boards
     */
    private class BoardIterable implements Iterable<Board> {

        private List<Board> boardList;
        private int index;

        /**
         * Constructor for the class
         * @param boards A list of boards to build off of
         */
        public BoardIterable(List<Board> boards)
        {
            boardList = new ArrayList<>();
            for(int i = boards.size() - 1; i > -1; i--)
                boardList.add(boards.get(i));
            index = 0;
        }

        /**
         * Is there an next board?
         * @return true if yes, false if no
         */
        public boolean hasNext()
        {
            return (index != boardList.size() - 1);
        }

        /**
         * Returns the next board in the list
         * @return Board that is next
         */
        public Board next()
        {
            index++;
            return boardList.get(index);
        }

        /**
         * Returns the iterator for the class
         * @return iterator for the class
         */
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
        // Number of moved to get here
        int numMoves;
        // Priority of the game, board Manhattan + numMoves
        int priority;
        // Board being wrapped
        Board board;
        // Parent game containing board's parent board
        Game parent;

        /**
         * Cnstructor for the helper class
         * @param curr Board for the class
         * @param move Move # that reached the board
         * @param parentNode Parent game containing the parent board
         */
        public Game(Board curr, int move, Game parentNode)
        {
            board = curr;
            numMoves = move;
            parent = parentNode;
            priority = numMoves + board.manhattan();
        }

        /**
         * Override function for equality. Works the same as the board equality override
         * @param y The object (Game) to compare against
         * @return True if same, false if not
         */
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
            return board.equals(testBoard);
        }

        /**
         * Override function for compareTo for the minPQ class to find out what has lowest priority
         * @param y The object (Game) to compare against
         * @return -1 if lower, 0 if same, 1 if greater
         */
        @Override
        public int compareTo(Object y)
        {
            /*if (this == y) // Same ref
                return 0;
            if (y == null)
                return 0;*/
            Game testGame = (Game)y;
            if (this.priority < testGame.priority)
                return -1;
            else if (this.priority == testGame.priority)
                return 0;
            else
                return 1;
        }
    }

    // The iterable for the solved sequence
    private BoardIterable solvedSequence;
    // Local counter for the number of moves taken. Used to solve and return
    private int movesTaken;

    /**
     * Base constructor
     * @param initial starting board to solve from
     */
    public Solver(Board initial)
    {
        if (initial == null)
            throw new IllegalArgumentException("Board cannot be null");
        if (!initial.isSolvable())
            throw new IllegalArgumentException("Board is unsolvable");
        if (initial.isGoal())
        {
            movesTaken = 0;
            List<Board> solvedList = new ArrayList<>();
            solvedList.add(initial);
            solvedSequence = new BoardIterable(solvedList);
            return;
        }
        movesTaken = -1;
        solveAStar(initial);
    }

    /**
     * A* Solving algorithm
     * @param initial starting board to solve from
     */
    private void solveAStar(Board initial)
    {
        // Init local PQs
        MinPQ<Game> closedSet = new MinPQ<>();
        MinPQ<Game> openSet = new MinPQ<>();

        Game current = new Game(initial, 0, null);

        // The current is solved
        if (current.board.isGoal())
        {
            // Build list based off of parent path
            List<Board> solvedList = new ArrayList<>();
            while (current != null)
            {
                solvedList.add(current.board);
                current = current.parent;
                movesTaken++;
            }
            solvedSequence = new BoardIterable(solvedList);
            return;
        }

        openSet.insert(current);
        //int loopCount = 0;
        // Loop through all open set contents
        while (openSet.size() > 0)
        {
            current = openSet.delMin();
            closedSet.insert(current);

            // Check all neighbors
            for (Board neighbor : current.board.neighbors())
            {
                boolean alreadyStored = false;
                for(Game closed : closedSet) // Look for it in closed, if in closed skip
                    if (closed.board.equals(neighbor))
                    {
                        alreadyStored = true;
                        break;
                    }
                if (alreadyStored) // Skip
                    continue;
                for(Game open : openSet) // Loop for it in open set
                    if (open.board.equals(neighbor))
                    {
                        if (current.numMoves + 1 > open.numMoves)
                        {
                            openSet.insert(new Game(neighbor, current.numMoves + 1, current));
                        }
                        alreadyStored = true;
                        break;
                    }
                // If not in open set add it!
                if (!alreadyStored)
                    openSet.insert(new Game(neighbor, current.numMoves + 1, current)); // Discover it
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

    /**
     * Test client!
     * @param args Needs a text file containing the puzzle to solve
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            StdOut.println("Please supply an input file");
            return;
        }

        In readFile = new In(args[0]);
        int length = 0;
        length = readFile.readInt();
        int[][] puzzle = new int[length][length];
        for (int i = 0; i < length; i++)
            for (int j = 0; j < length; j++)
                puzzle[i][j] = readFile.readInt();

        Board testBoard = new Board(puzzle);
        //StdOut.println(testBoard.toString());
        if (!testBoard.isSolvable())
        {
            StdOut.println("Unsolvable puzzle");
            return;
        }
        //StdOut.println("Starting with: \n" + testBoard.toString());
        Solver testSolver = new Solver(testBoard);

        StdOut.println("Minimum number of moves = " + testSolver.moves());

        for(Board board: testSolver.solution())
        {
            StdOut.println(board.toString());
        }
    }

}
