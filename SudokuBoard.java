/**
 * Standard 9 x 9 sudoku board made from SudokuSquares.
 * SudokuBoard is made of SudokuSquare objects. 
 * <p>
 * Cells on the board are specified with a (row, column) in the following way:
 * (1,1) (1,2) ... (1,9)
 * (2,1) ...
 * .
 * .
 * . 
 * (9,1) (9,2) ... (9,9)
 * <p>
 * 
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;

public class SudokuBoard {

  private SudokuSquare[][] cells;

/**
 * Constructs an empty SudokuBoard.
 */
  public SudokuBoard() {
    cells = new SudokuSquare[9][9];
  }

/**
 * Constructs a SudokuBoard populated with the input.
 * @param board 81-character string representing a sudoku board.
 */
  public SudokuBoard(String board) {
    if (board.length() != 81) {
      throw new IllegalArgumentException("bad input board");
    }
    cells = new SudokuSquare[9][9];
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        cells[i][j] = new SudokuSquare(
            Character.getNumericValue(board.charAt(i * 9 + j)));
      }
    }
  }

/**
 * Returns the value of the cell at (row, column).
 * @param row row of the desired cell
 * @param col column of the desired cell
 */
  public int getValue(int row, int col) {
    return cells[row - 1][col - 1].getValue();
  }

/**
 * Returns the pencils of the cell at (row, column).
 * @param row row of the desired cell
 * @param col column of the desired cell
 */
  public ArrayList<Integer> getPencils(int row, int col) {
    return cells[row - 1][col - 1].getPencils();
  }

/**
 * Returns the board as represented by an 81-character string.
 */
  public String toString() {
    String string = "";
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        string = string.concat(Integer.toString(cells[i][j].getValue()));
      }
    }
    return string;
  }

/**
 * Returns the board as a string, formatted into a 9x9 grid.
 * @param pretty formats the string if true, uses toString() if false
 */
  public String toPrettyString() {
    String string = "";
    for (int i = 0; i < 9; i++) {
      if (i == 3 || i == 6) {
        string = string.concat("- - - - - - - - - - -\n");
      }
      for (int j = 0; j < 9; j++) {
        string = string.concat(
            Integer.toString(cells[i][j].getValue())).concat(" ");
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
 * Returns the specified row as an array of SudokuSquares.
 * @param row row number to return from 1-9
 */
  public SudokuSquare[] getRow(int row) {
    if (row < 1 || row > 9) {
      throw new IllegalArgumentException("Row must be between 1 and 9.");
    }
    SudokuSquare[] newrow = new SudokuSquare[9];
    for (int pos = 0; pos < 9; pos++) {
      newrow[pos] = cells[row - 1][pos];
    }
    return newrow;
  }

/**
 * Returns the specified column as an array of SudokuSquares.
 * @param col column number to return from 1-9
 */
  public SudokuSquare[] getCol(int col) {
    if (col < 1 || col > 9) {
      throw new IllegalArgumentException("Column must be between 1 and 9.");
    }
    SudokuSquare[] newcol = new SudokuSquare[9];
    for (int pos = 0; pos < 9; pos++) {
      newcol[pos] = cells[pos][col - 1];
    }
    return newcol;
  }

/**
 * Method for quick testing.
 */
  public static void main(String[] args) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(args[0]));
      String boardstring = in.readLine();
      //System.out.println(boardstring);
      SudokuBoard sb = new SudokuBoard(boardstring);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      }
      catch (IOException e) {
      System.out.println("IO Exception.");
      }
  }
}
