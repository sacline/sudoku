#**Sudoku**

##Description

Java implementation of Sudoku puzzle game.

###Current features:

* Board with ability to keep square values, pencils, and a record of moves made.

* Solver with brute force recursive backtracking algorithm for solving of a board. Four deterministic solving algorithms implemented.

* GUI that allows the user to play Sudoku using a combination of keyboard and mouse inputs. Requires an input file with properly formatted sudoku boards, solutions, and difficulties.

###Planned for the future:

* Optimization of brute force algorithm using deterministic algorithms.

* Improvements to the UI appearance.

* Additional features to the game such as checking progress midway through a puzzle, etc.)

* More expansive tests.

##How to install
This program requires Java as well as the [JavaFX library.](http://docs.oracle.com/javase/8/javase-clienttechnologies.htm)

To run the game, compile the .java files and run SudokuGame. For example in a UNIX terminal, navigate to the directory with the files and run:
`javac *.java`
`java SudokuGame`
