/*************************************************************************
 * Names: Connor Adams and Samantha Bagwell
 *
 * Description: This is the board class for an 8 Puzzle, although it can
 * support much larger puzzles than 8. They are labeled 1 - N where
 * N the side length squared, leaving one last value of 0 as the blank.
 *
 * Use with the solver to implement into min priority queues and solve
 * with A* or similar algorithms
 *
*************************************************************************/

 import edu.princeton.cs.algs4.StdOut;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Board {

    // The 2D array of values for the puzzle
    private int[][] boardTiles;
    // The hamming value of the board. Cached for efficiency
    private int hammingValue;
    // the manhattan value of the board. Cached for efficiency
    private int manhattanValue;
    // Tracking where 0 was found in the different fitness functions for efficient swapping
    private BoardVector zeroLoc;

    /**
     * Helper class. Just contains the x, y of a board position
     */
    private class BoardVector {
        public int xVal;
        public int yVal;
        /**
         * Constructor for the helper class
         * @param val1 x pos
         * @param val2 y pos
         */
        public BoardVector(int val1, int val2)
        {
            xVal = val1;
            yVal = val2;
        }
    }

    /**
     * The iteration class
     */
    private class BoardIterable implements Iterable<Board> {

        private List<Board> boardList;
        private int index;

        /**
         * Iterable constructor
         * @param boards The list to build off of
         */
        public BoardIterable(List<Board> boards)
        {
            boardList = boards;
            index = 0;
        }

        /**
         * Check if there is another board after this
         * @return True if there is another, false if not
         */
        public boolean hasNext()
        {
            return (index != boardList.size() - 1);
        }

        /**
         * Gets the next element
         * @return The next board
         */
        public Board next()
        {
            index++;
            return boardList.get(index);
        }

        /**
         * Returns the class iterator
         * @return the iterator object
         */
        public Iterator<Board> iterator()
        {
            return boardList.iterator();
        }

    }

    /**
     * Base constructor
     * @param tiles 2D array of tile values
     */
    public Board(int[][] tiles)
    {
        //zeroLoc = new BoardVector(-1, -1);
        boardTiles = tiles;
        manhattanValue = -1;
        hammingValue = -1;
    }

    /**
     * String of the current board and size
     * @return string of the current board
     */
    public String toString()
    {
        StringBuilder returnVal = new StringBuilder();
        returnVal.append(boardTiles.length);
        returnVal.append("\n");
        for (int i = 0; i < boardTiles.length; i++)
        {
            for (int j = 0; j < boardTiles.length; j++)
            {
                returnVal.append((" " + boardTiles[i][j] + " "));
            }
            returnVal.append("\n");
        }
        return returnVal.toString();
    }

    /**
     * Returns the tile value at a given location
     * @param row the x value
     * @param col the y value
     * @return returns the value if there is a tile there, 0 if not
     */
    public int tileAt(int row, int col)
    {
        if (row < 0 || row >= boardTiles.length || col < 0 || col >= boardTiles.length)
            throw new IllegalArgumentException("Arguments must be between 0 and n-1");
        return boardTiles[row][col];
    }

    /**
     * The size of the board, length * width
     * @return int size of the board
     */
    public int size()
    {
        return boardTiles.length;
    }

    /**
     * Hamming value of the board. Returns the number of pieces that are out of place
     * @return int of the number of values out of place
     */
    public int hamming()
    {
        if (hammingValue == -1)
            return computeHamming();
        return hammingValue;
    }

    /**
     * Helper function for Hamming value. This calculates the actual value and caches it
     * @return the hamming value
     */
    private int computeHamming()
    {
        int hammingVal = 0;
        int expected = 1;
        for (int i = 0; i < boardTiles.length; i++)
        {
            for (int j = 0; j < boardTiles[0].length; j++)
            {
                if (boardTiles[i][j] == 0)
                    zeroLoc = new BoardVector(i, j);
                // Skip the last tile
                if (i == boardTiles.length - 1 && j == boardTiles.length - 1)
                    continue;
                if (boardTiles[i][j] != expected)
                    hammingVal ++;
                expected++;
            }
        }
        return hammingVal;
    }

    /**
     * Manhattan value of the board. Calculated by the summation of the number of moves every piece is from its
     * goal position
     * @return int manhattan value
     */
    public int manhattan()
    {
        if (manhattanValue == -1)
            manhattanValue = computeManhattan();
        return manhattanValue;
    }

    /**
     * Helper function for computing the Manhattan value. This actually calculates the value and caches it for later
     * @return the int manhattan value
     */
    private int computeManhattan()
    {
        int manhattanVal = 0;
        int xOffset = 0, yOffset = 0;
        for (int i = 0; i < boardTiles.length; i++)
        {
            for (int j = 0; j < boardTiles.length; j++)
            {
                if (boardTiles[i][j] == 0)
                {
                    zeroLoc = new BoardVector(i, j); // Recording this for other functions
                    continue;
                }
                if (boardTiles[i][j] != (i * boardTiles.length) + j + 1) // Wrong value in slot
                {
                    xOffset = (int)Math.floor((double)(boardTiles[i][j] - 1) / (double)boardTiles.length);
                    yOffset = (boardTiles[i][j] - 1) % boardTiles.length;
                    //StdOut.println("Adding: " + (Math.abs(i - xOffset ) + Math.abs(j - yOffset)));
                    manhattanVal += (Math.abs(i - xOffset ) + Math.abs(j - yOffset));
                    //StdOut.println("M: " + manhattanVal + " xOffset: " + xOffset + ", yOffset: " + yOffset + ", total: " + total + " at: " + i +", " + j);
                }
            }
        }
        return manhattanVal;
    }

    /**
     * Returns a bool if the board is solved.
     * @return bool if solved
     */
    public boolean isGoal()
    {
        if (manhattanValue == -1)
            computeManhattan();
        return (manhattanValue == 0);
    }

    /**
     * Checks if an object is equal to this board
     * @param y the object to compare against
     * @return bool if equal
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
        if (testBoard.size() != size())
            return false;
        // Check for tile equality
        return Arrays.deepEquals(boardTiles, testBoard.boardTiles);
    }

    /**
     * Helper fucntion to copy and array and not just make another reference
     * @param arrayToCopy 2D int array to copy
     * @return The copied 2D int array
     */
    private int[][] copyArray(int[][] arrayToCopy)
    {
        int[][] copy = new int[arrayToCopy.length][arrayToCopy[0].length];
        for (int i = 0; i < arrayToCopy.length; i++)
        {
            copy[i] = arrayToCopy[i].clone();
        }
        return copy;
    }

    /**
     * Iterable that returns all board possibilities from the current
     * @return Iterable of neighboring boards
     */
    public Iterable<Board> neighbors()
    {
        List<Board> neighborList = new ArrayList<>();
        if (zeroLoc.xVal == -1)
            computeHamming();

        int[][] setupHolder; // Duplicate current setup
        if (zeroLoc.xVal != boardTiles.length - 1) // Right value
        {
            setupHolder = copyArray(boardTiles);
            setupHolder[zeroLoc.xVal][zeroLoc.yVal] = setupHolder[zeroLoc.xVal + 1][zeroLoc.yVal];
            setupHolder[zeroLoc.xVal + 1][zeroLoc.yVal] = 0;
            neighborList.add(new Board(setupHolder));
        }
        if (zeroLoc.yVal != boardTiles[0].length - 1) // Bottom value
        {
            setupHolder = copyArray(boardTiles);
            setupHolder[zeroLoc.xVal][zeroLoc.yVal] = setupHolder[zeroLoc.xVal][zeroLoc.yVal + 1];
            setupHolder[zeroLoc.xVal][zeroLoc.yVal + 1] = 0;
            neighborList.add(new Board(setupHolder));
        }
        if (zeroLoc.xVal != 0) // Left value
        {
            setupHolder = copyArray(boardTiles);
            setupHolder[zeroLoc.xVal][zeroLoc.yVal] = setupHolder[zeroLoc.xVal - 1][zeroLoc.yVal];
            setupHolder[zeroLoc.xVal - 1][zeroLoc.yVal] = 0;
            neighborList.add(new Board(setupHolder));
        }
        if (zeroLoc.yVal != 0) // Top value
        {
            setupHolder = copyArray(boardTiles);
            setupHolder[zeroLoc.xVal][zeroLoc.yVal] = setupHolder[zeroLoc.xVal][zeroLoc.yVal - 1];
            setupHolder[zeroLoc.xVal][zeroLoc.yVal - 1] = 0;
            neighborList.add(new Board(setupHolder));
        }
        return new BoardIterable(neighborList);
    }

    /**
     * Returns if a board is solvable.
     * A board with odd lengths is unsolvable if the number of inversions is odd.
     * In an even board it is solvable if the number of inversions + the row num of the blank square is an odd value
     * @return Bool, true if solvable and false if not
     */
    public boolean isSolvable()
    {
        if (isGoal()) // Possible for it to be solved but fail
            return true;
        int inversions = 0;
        int row0 = -1;
        List<Integer> foundValues = new ArrayList<>();
        for (int i = 0; i < boardTiles.length; i++)
        {
            for (int j = 0; j < boardTiles[0].length; j++)
            {
                if ( boardTiles[i][j] == 0)
                {
                    row0 = i;
                    continue;
                }
                for (int recorded : foundValues)
                    if (boardTiles[i][j] < recorded)
                        inversions++;
                foundValues.add(boardTiles[i][j]);
            }
        }
        // Even board size
        if (boardTiles.length % 2 == 0)
        {
            //StdOut.println(inversions + " " + row0);
            if ((inversions + row0) % 2 == 0 ) // inversions + row with blank is even
                return false;
            else // Is odd
                return  true;
        }
        else // Odd board
        {
            if (inversions % 2 == 0) // Inversions is even
                return true;
            else // Inversions is odd
                return false;
        }
    }

    /**
     * Unit testing client
     * @param args None needed
     */
    public static void main(String[] args)
    {
        int[][] testVals = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board testBoard = new Board(testVals);
        StdOut.println(testBoard.toString());
        StdOut.println("Goal? " + testBoard.isGoal());
        StdOut.println("Solvable: " + testBoard.isSolvable());
        StdOut.println("Hamming: " + testBoard.hamming() + " Manhattan: " + testBoard.manhattan());
        int[][] testVals2 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board testBoard2 = new Board(testVals2);
        if (testBoard.equals(testBoard2))
            StdOut.println(testBoard.toString() + " EQUALS " + testBoard2.toString());
        Iterable<Board> iBoards = testBoard.neighbors();
        for (Board curr : iBoards) {
            StdOut.println(curr.toString());
        }
        StdOut.println("Print every slot by tileAt");
        for (int i = 0; i < Math.sqrt(testBoard.size()); i++)
            for(int j = 0; j <  Math.sqrt(testBoard.size()); j++)
                StdOut.print(testBoard.tileAt(i, j) + " ");

        int[][] anotherTest = { {5,  6,  2},
                {1,  8,  4},
                {7,  3,  0}};
        Board anotherBoard = new Board(anotherTest);
        StdOut.println("Goal? " + anotherBoard.isGoal());
        StdOut.println("Solvable: " + anotherBoard.isSolvable());
        StdOut.println("Hamming: " + anotherBoard.hamming() + " Manhattan: " + anotherBoard.manhattan());

    }


}
