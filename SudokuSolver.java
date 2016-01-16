/**
 * Class containing methods for evaluating a SudokuBoard.
 * The SudokuSolver class contains algorithms to find the solution to an
 * incomplete SudokuBoard. A sudoku puzzle is solved once the values of its
 * squares satisfy the "One Rule": each row, column, and region must contain
 * the digits 1-9 exactly once.
 *
 * @version 1.1
 */

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

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
 * @return true if rows are correct, false if not
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
    for (int row = 1; row < 10; row +=3) {
      for (int col = 1; col < 10; col += 3) {
        if (checkUnit(board.getReg(row, col)) == false) {
          return false;
        }
      }
    }
    return true;
  }

/**
 * Checks that a generic unit satisfies the One Rule.
 * A unit can be a row, column, or region. The unit is passed as an array
 * of ints.
 *
 * @param unit array of square values to check
 * @return true if the unit is complete and correct, false if not
 */
  private boolean checkUnit(int[] unit) {
    int[] target = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    int[] unitvalues = new int[9];
    for (int index = 0; index < 9; index++) {
      unitvalues[index] = unit[index];
    }
    Arrays.sort(unitvalues);
    return Arrays.equals(unitvalues, target);
  }

/**
 * Checks if the value violates the One Rule
 *
 * @param board board to check
 * @param row row of cell
 * @param col column of cell
 * @param value value to check
 */
  public boolean validValue(SudokuBoard board, int row, int col, int value) {
    for (int cellvalue : board.getRow(row)) {
      if (cellvalue == value) {
        return false;
      }
    }
    for (int cellvalue : board.getCol(col)) {
      if (cellvalue == value) {
        return false;
      }
    }
    for (int cellvalue : board.getReg(row, col)) {
      if (cellvalue == value) {
        return false;
      }
    }
    return true;
  }

/**
 * Finds the valid pencils of a cell and adds them.
 * This is used as a starting point, as it only removes
 * pencils based on values in the cell's row, column, and region.
 *
 * @param row row of the cell
 * @param col column of the cell
 */
  public void generatePencils(SudokuBoard board, int row, int col) {
    if (board.getValue(row, col) != 0) {
      return;
    }
    ArrayList<Integer> poss = new ArrayList<Integer>();
    for (int number = 1; number < 10; number++) {
      poss.add(number);
    }
    for (int value : board.getRow(row)) {
      poss.remove(Integer.valueOf(value));
    }
    for (int value : board.getCol(col)) {
      poss.remove(Integer.valueOf(value));
    }
    for (int value : board.getReg(row, col)) {
      poss.remove(Integer.valueOf(value));
    }
    board.clearPencils(row, col);
    for (Integer pencil : poss) {
      board.addPencil(row, col, pencil);
    }
  }

/**
 * Uses a brute force solution to solve the puzzle.
 * Backtracking method uses guess-and check brute force solution.
 *
 * @param board board to solve
 * @return a solved copy of the board
 */
  public SudokuBoard bruteForce(SudokuBoard board) {
    SudokuBoard newboard = new SudokuBoard();
    ArrayList<Integer> unsolved = new ArrayList<Integer>();
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        newboard.setValue(i, j, board.getValue(i,j));
        generatePencils(newboard, i, j);
        if (board.getValue(i, j) == 0) {
          //cells in the board numbered 1-81
          unsolved.add(Integer.valueOf((i - 1) * 9 + j));
        }
      }
    }
    int index = 0; 
    bruteForceTest(newboard, unsolved, index);
    return newboard;
  }

  private void bruteForceTest(
      SudokuBoard board, ArrayList<Integer> unsolved, int index) {
    if (isSolved(board)) {
      return;
    }
    //for loop checks all possible pencils at the index
    int j = ((unsolved.get(index) - 1) % 9) + 1;
    int i = (unsolved.get(index) - j) / 9 + 1;
    for (Integer pencil : board.getPencils(i, j)) {
      if (validValue(board, i, j, pencil)) {
        board.setValue(i, j, pencil);
        bruteForceTest(board, unsolved, index + 1);
        if (!isSolved(board)) {
          board.setValue(i, j, 0);
        }
      }
    }
  }

/** Method for simple tests. */
  public static void main(String[] args) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(args[0]));
      String puzzle;
      SudokuSolver solver = new SudokuSolver();
      while ((puzzle = in.readLine()) != null) {
        SudokuBoard board = new SudokuBoard(puzzle);
        SudokuBoard solvedboard = new SudokuBoard();
        solvedboard = solver.bruteForce(board);
        //System.out.println(board.toPrettyString());
        //System.out.println(solvedboard.toPrettyString());
      }
    } catch(FileNotFoundException e) {
      System.out.println("File not found.");
    }
      catch(IOException e) {
      System.out.println("IO Error.");
    }
  }
}
