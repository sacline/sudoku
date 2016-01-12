/**
 * Class containing methods for evaluating a SudokuBoard.
 * The SudokuSolver class contains algorithms to find the solution to an
 * incomplete SudokuBoard. A sudoku puzzle is solved once the values of its
 * squares satisfy the "One Rule": each row, column, and region must contain
 * the digits 1-9 exactly once.
 *
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;

public class SudokuSolver {

/**
 * Default constructor.
 */
  public SudokuSolver() {
  }

/**
 * Determines if the board is correctly solved.
 * This method checks rows, columns, and regions (collectively, units) to
 * ensure that each number is in each unit, and that it only appears once.
 *
 * @param board board to check
 * @return true if solved, false if not
 */
  public boolean isSolved(SudokuBoard board) {
    return (checkRows(board) && checkCols(board) && checkRegs(board));
  }

/**
 * Checks that all rows satisfy the One Rule.
 * 
 * @param board board to check
 @ @return true if rows are correct, false if not
 */
  public boolean checkRows(SudokuBoard board) {
    for (int index = 1; index < 10; index++) {
      if (checkUnit(board.getRow(index)) == false) {
          return false;
      }
    }
    return true;
  }

/**
 * Checks that all columns satisfy the One Rule.
 *
 * @param board board to check
 * @return true if columns are correct, false if not
 */
  public boolean checkCols(SudokuBoard board) {
    for (int index = 1; index < 10; index++) {
      if (checkUnit(board.getCol(index)) == false) {
          return false;
      }
    }
    return true;
  }

/**
 * Checks that all regions satisfy the One Rule.
 *
 * @param board board to check
 * @return true if regions are correct, false if not
 */
  public boolean checkRegs(SudokuBoard board) {
    for (int index = 1; index < 10; index++) {
      if (checkUnit(board.getReg(index)) == false) {
          return false;
      }
    }
    return true;
  }

/**
 * Checks that a generic unit satisfies the One Rule.
 * A unit can be a row, column, or region. The unit is passed as an array
 * of SudokuSquares.
 *
 * @param unit array of squares to check
 * @return true if the unit is complete and correct, false if not
 */
  private boolean checkUnit(SudokuSquare[] unit) {
    int[] target = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    int[] unitvalues = new int[9];
    for (int index = 0; index < 9; index++) {
      unitvalues[index] = unit[index].getValue();
    }
    Arrays.sort(unitvalues);
    return Arrays.equals(unitvalues, target);
  }

/** Method for simple tests. */
  public static void main(String[] args) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(args[0]));
      SudokuBoard board = new SudokuBoard(in.readLine());
      SudokuSolver solver = new SudokuSolver();
      System.out.println(solver.isSolved(board));
    } catch(FileNotFoundException e) {
      System.out.println("File not found.");
    }
      catch(IOException e) {
      System.out.println("IO Error.");
    }
  }
}
