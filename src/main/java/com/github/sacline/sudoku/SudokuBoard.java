package com.github.sacline.sudoku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * Standard 9 x 9 sudoku board made from SudokuSquares.
 * SudokuBoard is made of SudokuSquare objects.
 *
 * <p>Cells on the board are specified with a (row, column) as follows:
 * (1,1) (1,2) ... (1,9)
 * (2,1) ...
 * .
 * .
 * .
 * (9,1) (9,2) ... (9,9)
 */
public class SudokuBoard {
  private SudokuSquare[][] cells;
  private ArrayDeque<Move> moves;

  /** Constructs an empty SudokuBoard. */
  public SudokuBoard() {
    this(null);
  }

  /**
   * Primary SudokuBoard constructor.
   * Input may be null, which will create an empty board.
   * It also handles a string representing a board.
   *
   * @param board 81-character string representing a sudoku board.
   */
  public SudokuBoard(String board) {
    cells = new SudokuSquare[9][9];
    moves = new ArrayDeque<Move>();
    if (board == null) {
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          cells[i][j] = new SudokuSquare();
        }
      }
    }
    if (board != null) {
      if (board.length() != 81) {
        throw new IllegalArgumentException("Bad input board");
      }
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          cells[i][j] = new SudokuSquare(
              Character.getNumericValue(board.charAt(i * 9 + j)));
        }
      }
    }
  }

  /**
   * Returns a new copy of the board.
   *
   * @return copy of this board
   */
  public SudokuBoard copyBoard() {
    SudokuBoard copy = new SudokuBoard(this.toString());
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        for (int pencil : getPencils(i, j)) {
          copy.addPencil(i, j, pencil);
        }
      }
    }
    for (Move move : this.moves) {
      copy.moves.add(new Move(move.row, move.col, move.value, move.change));
    }
    return copy;
  }


  /**
   * Returns the value of the cell at (row, column).
   *
   * @param row row of the desired cell
   * @param col column of the desired cell
   */
  public int getValue(int row, int col) {
    validateRowCol(row);
    validateRowCol(col);
    return cells[row - 1][col - 1].value;
  }

  /**
   * Sets the value of a cell.
   *
   * @param row row of the desired cell
   * @param col column of the desired cell
   * @param value value to set the cell
   */
  public void setValue(int row, int col, int value) {
    validateRowCol(row);
    validateRowCol(col);
    moves.addFirst(new Move(row, col, getValue(row, col), "+v"));
    cells[row - 1][col - 1].setValue(value);
  }

  /**
   * Returns the pencils of the cell at (row, column).
   *
   * @param row row of the desired cell
   * @param col column of the desired cell
   */
  public ArrayList<Integer> getPencils(int row, int col) {
    validateRowCol(row);
    validateRowCol(col);
    return cells[row - 1][col - 1].pencils;
  }

  /**
   * Adds a specified pencil to the cell at (row, column).
   *
   * @param row row of the desired cell
   * @param col column of the desired cell
   * @param value value to add to pencils
   */
  public void addPencil(int row, int col, int value) {
    validateRowCol(row);
    validateRowCol(col);
    moves.addFirst(new Move(row, col, value, "+p"));
    cells[row - 1][col - 1].addPencil(value);
  }

  /**
   * Removes a specified pencil to the cell at (row, column).
   *
   * @param row row of the desired cell
   * @param col column of the desired cell
   * @param value value to remove from pencils
   */
  public void removePencil(int row, int col, int value) {
    validateRowCol(row);
    validateRowCol(col);
    moves.addFirst(new Move(row, col, value, "-p"));
    cells[row - 1][col - 1].removePencil(value);
  }

  /**
   * Clear all pencils from the cell at (row, column).
   *
   * @param row row of the desired cell
   * @param col column of the desired cell
   */
  public void clearPencils(int row, int col) {
    validateRowCol(row);
    validateRowCol(col);
    for (int pencil : getPencils(row, col)) {
      moves.addFirst(new Move(row, col, pencil, "-p"));
    }
    cells[row - 1][col - 1].clearPencils();
  }

  /**
   * Reverts the most recent change to the board.
   */
  public void undoMove() {
    try {
      reverseMove(moves.removeFirst());
    } catch (NoSuchElementException e) {
      return;
    }
  }

  /**
   * Returns the original board after reverting all moves.
   *
   * @return a copy of the board at its starting point
   */
  public SudokuBoard originalBoard() {
    SudokuBoard copy = new SudokuBoard();
    copy = copyBoard();
    while (copy.moves.size() != 0) {
      copy.undoMove();
    }
    return copy;
  }

  /**
   * Examines a move and performs the reverse operation on the board.
   * The reversal creates an additional move on the board which is removed
   * after the reversal.
   */
  private void reverseMove(Move recentMove) {
    switch (recentMove.change) {
      case "+v":
        setValue(recentMove.row, recentMove.col, recentMove.value);
        moves.removeFirst();
        break;
      case "+p":
        removePencil(recentMove.row, recentMove.col, recentMove.value);
        moves.removeFirst();
        break;
      case "-p":
        addPencil(recentMove.row, recentMove.col, recentMove.value);
        moves.removeFirst();
        break;
      default:
        System.out.println("invalid move being removed");
    }
  }

  /**
   * Returns the board as represented by an 81-character string.
   *
   * @return the 81-char string
   */
  @Override
  public String toString() {
    String string = "";
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        string = string.concat(Integer.toString(cells[i][j].value));
      }
    }
    return string;
  }

  /**
   * Returns the board as a string, formatted into a 9x9 grid.
   *
   * @return the formatted string
   */
  public String toPrettyString() {
    String string = "";
    for (int i = 0; i < 9; i++) {
      if (i == 3 || i == 6) {
        string = string.concat("- - - - - - - - - - -\n");
      }
      for (int j = 0; j < 9; j++) {
        string = string.concat(
            Integer.toString(cells[i][j].value)).concat(" ");
        if (j == 2 || j == 5) {
          string = string.concat("| ");
        }
        if (j == 8) {
          string = string.concat("\n");
        }
      }
    }
    return string;
  }

  /**
   * Compares one SudokuBoard to another.
   *
   * @param obj board to compare with
   * @return true if the same, false if not
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof SudokuBoard)) {
      return false;
    }
    SudokuBoard compareboard = (SudokuBoard) obj;
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        if (this.getValue(i, j) == compareboard.getValue(i, j)
            && this.getPencils(i, j).equals(compareboard.getPencils(i,j))) {
          continue;
        } else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Generates a hashcode for the SudokuBoard.
   *
   * @return hashcode representing the board
   */
  @Override
  public int hashCode() {
    int result = 1;
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        result *= i;
        result *= j;
        result += Integer.valueOf(this.getValue(i, j)).hashCode();
        for (Integer pencil : this.getPencils(i, j)) {
          result *= 31;
          result += pencil.hashCode();
        }
        result += this.getPencils(i,j).hashCode();
      }
    }
    return result;
  }

  /**
   * Returns the specified row as an array of ints.
   *
   * @param row row number to return from 1-9
   * @return the array of square values in specified row
   */
  public int[] getRow(int row) {
    validateRowCol(row);
    int[] newRow = new int[9];
    for (int pos = 1; pos < 10; pos++) {
      newRow[pos - 1] = getValue(row, pos);
    }
    return newRow;
  }

  /**
   * Returns the specified column as an array of ints.
   *
   * @param col column number to return from 1-9
   * @return the array of square values in specified column
   */
  public int[] getCol(int col) {
    validateRowCol(col);
    int[] newCol = new int[9];
    for (int pos = 1; pos < 10; pos++) {
      newCol[pos - 1] = getValue(pos, col);
    }
    return newCol;
  }

  /**
   * Returns the region as an array of ints.
   * Regions are the nine 3x3 subgrids on the board numbered as follows:
   * 1 2 3
   * 4 5 6
   * 7 8 9
   *
   * @param row row of the cell
   * @param col column of the cell
   * @return the array of squares in specified region
   */
  public int[] getReg(int row, int col) {
    validateRowCol(row);
    validateRowCol(col);
    int region = findReg(row, col);
    int[] newReg = new int[9];
    //starting rows and cols for each region
    int[] startingRow = {1, 1, 1, 4, 4, 4, 7, 7, 7};
    int[] startingCol = {1, 4, 7, 1, 4, 7, 1, 4, 7};
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        newReg[i * 3 + j] =
            getValue(startingRow[region - 1] + i, startingCol[region - 1] + j);
      }
    }
    return newReg;
  }

  /**
   * Returns the region as an array of ints.
   * This method allows specifying the reg (1-9) as opposed to
   * a row and column specification.
   *
   * @param reg the region (1-9) to return
   */
  public int[] getReg(int reg) {
    int[] startingRow = {1, 1, 1, 4, 4, 4, 7, 7, 7};
    int[] startingCol = {1, 4, 7, 1, 4, 7, 1, 4, 7};
    return getReg(startingRow[reg - 1], startingCol[reg - 1]);
  }

  /**
   * Returns the region number for the cell at (row, col).
   *
   * @param row row of specified cell
   * @param col column of specified cell
   * @return the region number corresponding to the cell
   */
  public static int findReg(int row, int col) {
    if (row < 4 && col < 4) {
      return 1;
    }
    if (row < 4 && col < 7) {
      return 2;
    }
    if (row < 4) {
      return 3;
    }
    if (row < 7 && col < 4) {
      return 4;
    }
    if (row < 7 && col < 7) {
      return 5;
    }
    if (row < 7) {
      return 6;
    }
    if (col < 4) {
      return 7;
    }
    if (col < 7) {
      return 8;
    }
    return 9;
  }

  /**
   * Throws exceptions for a bad row or column input.
   *
   * @param index row or column to check
   */
  private void validateRowCol(int index) {
    if (index < 1 || index > 9) {
      throw new IllegalArgumentException(
          "Rows and columns must be from 1 to 9.");
    }
  }

  /**
   * Object representing a change made to the board.
   * Each move will contain information on a change made to the board.
   * Moves will mainly be used in keeping a record of how a board
   * transitioned from one point to another (from unsolved to solved).
   */
  class Move {
    private int row;
    private int col;
    private int value;
    private String change;

    private Move(int row, int col, int value, String change) {
      this.row = row;
      this.col = col;
      this.value = value;
      this.change = change;
    }
  }

  /**
   * Basic sudoku board building block.
   * SudokuSquare is the class of objects that will make up a sudoku board.
   * Each square will be able to hold a single value as well as multiple
   * penciled values.
   */
  class SudokuSquare {
    private ArrayList<Integer> pencils;
    private int value;

    /**
     * Constructs an empty SudokuSquare.
     * Value set to 0 by default.
     */
    private SudokuSquare() {
      this(0);
    }

    /**
     * Constructs a SudokuSquare with the specified value.
     *
     * @param value value to create the square with
     */
    private SudokuSquare(int value) {
      pencils = new ArrayList<Integer>();
      validateValue(value);
      this.value = value;
    }

    /**
     * Sets the square value.
     * Value must be between 1 and 9 inclusive.
     *
     * @param value desired value of square
     */
    private void setValue(int value) {
      validateValue(value);
      this.value = value;
    }

    /**
     * Adds a penciled value.
     *
     * @param value value to pencil in
     */
    private void addPencil(int value) {
      validateValue(value);
      pencils.add(value);
      Collections.sort(pencils);
    }

    /**
     * Removes a penciled value.
     *
     * @param value value to unpencil
     */
    private void removePencil(int value) {
      validateValue(value);
      pencils.remove(new Integer(value));
    }

    /** Removes all penciled values. */
    private void clearPencils() {
      pencils.clear();
    }

    /**
     * Returns (empty/full) state of square.
     *
     * @return true if a value other than 0, false if 0.
     */
    private boolean isFull() {
      return (value != 0);
    }

    /**
     * Allows SudokuSquares to be printable with println.
     *
     * @return the string representation of the square's value
     */
    @Override
    public String toString() {
      return Integer.toString(value);
    }

    /**
     * Throws exceptions for a bad input.
     *
     * @param value value to check
     */
    private void validateValue(int value) {
      if (value < 0 || value > 9) {
        throw new IllegalArgumentException("Value must be between 0 and 9.");
      }
    }
  }

  /** Method for quick testing. */
  public static void main(String[] args) {
    try {
      //accepts a board string as a command line argument
      BufferedReader in = new BufferedReader(new FileReader(args[0]));
      String boardString = in.readLine();
      SudokuBoard sb = new SudokuBoard(boardString);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("IO Exception.");
    }
  }
}
