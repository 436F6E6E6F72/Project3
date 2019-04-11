Name: Connor Adams

Partner Name:  Samantha Bagwell



  Explain briefly how you represented the Board data type.
 **************************************************************************** */

The Board class contains a 2D int array that contains the value of each tile on the board. 


/* *****************************************************************************
 *  Explain briefly how you represented a search node
 *  (board + number of moves + previous search node).
 **************************************************************************** */


Each search node in mine is a class called ‘Game’. Each node contains a board at a specific game state, the number of moves it took to get there, the priority of the node, and the parent that it came from. For the searching algorithm these were then stored in a min priority queue. As a result, these also had to support the compareTo function.  





/* *****************************************************************************
 *  Explain briefly how you detected unsolvable puzzles.
 *
 *  What is the order of growth of the running time of your isSolvable()
 *  method in the worst case as function of the board size n? Recall that with
 *  order-of-growth notation, you should discard leading coefficients and
 *  lower-order terms, e.g., n log n or n^3.
 **************************************************************************** */

Description:

Unsolvable puzzles are detected based upon the length of each size of the puzzle, the number of inversion, and (depending upon length) the row that the open space (0) is on.

The algorithm loops through every spot in the 2D integer array of values, it records the row that the open space is on when it finds it, and for every value it compares the current value to a list of all values it has found so far. For every value in the list that the current value is less than it increments the inversion counter. When it is done with that it adds the current value to the list and moves on to the next.  

After it has the count of the number of inversions if the side length of the puzzle is odd it returns true if the number of inversions is even, false if not. If the length is even then it takes the number of inversions and adds the row that the open space is on. If that final value is even then it returns false, true otherwise. 



Order of growth of running time:

N^2 

The function uses two nested for loops that grow at a rate of sqrt(N) where N is the number of total slots. Within the inner most loop there is another for loop that will iterate the number of spaces that the function has evaluated to that point. 





/* *****************************************************************************
 *  For each of the following instances, give the minimum number of moves to
 *  solve the instance (as reported by your program). Also, give the amount
 *  of time your program takes with both the Hamming and Manhattan priority
 *  functions. If your program can't solve the instance in a reasonable
 *  amount of time (say, 5 minutes) or memory, indicate that instead. Note
 *  that your program may be able to solve puzzle[xx].txt even if it can't
 *  solve puzzle[yy].txt and xx > yy.
 **************************************************************************** */


                 min number          seconds
     instance     of moves     Hamming     Manhattan
   ------------  ----------   ----------   ----------
   puzzle28.txt 		28				1.68
   puzzle30.txt 		30				6.63
   puzzle32.txt 		
   puzzle34.txt 
   puzzle36.txt 
   puzzle38.txt 
   puzzle40.txt 
   puzzle42.txt 



/* *****************************************************************************
 *  If you wanted to solve random 4-by-4 or 5-by-5 puzzles, which
 *  would you prefer: a faster computer (say, 2x as fast), more memory
 *  (say 2x as much), a better priority queue (say, 2x as fast),
 *  or a better priority function (say, one on the order of improvement
 *  from Hamming to Manhattan)? Why?
 **************************************************************************** */
DO THAT PART



/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

The bigger the puzzle and more steps that it takes the longer the algorithm will take to solve it. It is only as good as the A* algorithm is.


/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
**************************************************************************** */

The only help we used was from the textbook, documentation for the Algs4 library, and the Wikipedia article on A*.


/* *****************************************************************************
 *  Describe any serious problems you encountered.                    
 **************************************************************************** */

I made some math errors on the Manhattan score and the solvable function. This caused some errors and serious slow-downs in my code. 

/* *****************************************************************************
 *  If you worked with a partner, give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */


Connor worked the most on the logic for the puzzle where Samantha focused on the A* algorithm, but no part was exclusively one person. We only worked on the project together, neither of us worked independently so the project was as a whole an entirely collaborative effort.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **************************************************************************** 

