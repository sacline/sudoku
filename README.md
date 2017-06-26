# **Sudoku**

## Description

Java implementation of Sudoku puzzle game.

### Current features:

* Board with ability to keep square values, pencils, and a record of moves made.

* Solver with brute force recursive backtracking algorithm for solving of a board. Four deterministic solving algorithms implemented.

* GUI that allows the user to play Sudoku using a combination of keyboard and mouse inputs. Requires a properly formatted input file (included in this repo at src/main/resources).

### Planned for the future:

* Improvements to the UI appearance.

* Additional features to the game such as checking progress midway through a puzzle, etc.

* More expansive tests.

## How to install

This project can be built using gradle.

In the top-level directory, run `gradle build` to compile, then run
`java -jar build/libs/sudoku.jar` to open the game.

## GUI screenshot:

The screenshot below shows the board with the original puzzle values in black. Values input by the user are shown in red and penciled numbers are shown in grey.

![Example](https://github.com/sacline/sudoku/blob/master/screenshot.png)
