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
 *
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;

public class SudokuBoard {

  private SudokuSquare[][] cells;

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
 * Returns the value of the cell at (row, column).
 *
 * @param row row of the desired cell
 * @param col column of the desired cell
 */
  public int getValue(int row, int col) {
    return cells[row - 1][col - 1].getValue();
  }

/**
 * Returns the pencils of the cell at (row, column).
 *
 * @param row row of the desired cell
 * @param col column of the desired cell
 */
  public ArrayList<Integer> getPencils(int row, int col) {
    return cells[row - 1][col - 1].getPencils();
  }

/**
 * Returns the board as represented by an 81-character string.
 *
 * @return the 81-char string
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
 *
 * @param row row number to return from 1-9
 * @return the array of squares in specified row
 */
  public SudokuSquare[] getRow(int row) {
    if (row < 1 || row > 9) {
      throw new IllegalArgumentException("Row must be from 1 to 9.");
    }
    SudokuSquare[] newrow = new SudokuSquare[9];
    for (int pos = 0; pos < 9; pos++) {
      newrow[pos] = cells[row - 1][pos];
    }
    return newrow;
  }

/**
 * Returns the specified column as an array of SudokuSquares.
 *
 * @param col column number to return from 1-9
 * @return the array of squares in specified column
 */
  public SudokuSquare[] getCol(int col) {
    if (col < 1 || col > 9) {
      throw new IllegalArgumentException("Column must be from 1 to 9.");
    }
    SudokuSquare[] newcol = new SudokuSquare[9];
    for (int pos = 0; pos < 9; pos++) {
      newcol[pos] = cells[pos][col - 1];
    }
    return newcol;
  }

/**
 * Returns the region as an array of SudokuSquares.
 * Regions are the nine 3x3 subgrids on the board numbered as follows:
 * 1 2 3
 * 4 5 6
 * 7 8 9
 *
 * @param region region number to return (1-9)
 * @return the array of squares in specified region
 */
  public SudokuSquare[] getReg(int region) {
    if (region < 1 || region > 9) {
      throw new IllegalArgumentException("Region must be from 1 to 9");
    }
    SudokuSquare[] newreg = new SudokuSquare[9];
    //starting rows and cols for each region
    int[] startingrow = {0, 0, 0, 3, 3, 3, 6, 6, 6};
    int[] startingcol = {0, 3, 6, 0, 3, 6, 0, 3, 6};
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        newreg[row * 3 + col] =
            cells[startingrow[region - 1] + row][startingcol[region - 1] + col];
      }
    }
    return newreg;
  }

/** Method for quick testing. */
  public static void main(String[] args) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(args[0]));
      String boardstring = in.readLine();
      SudokuBoard sb = new SudokuBoard(boardstring);
      //System.out.println(sb.toPrettyString());
      //System.out.println(Arrays.toString(sb.getReg(5)));
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      }
      catch (IOException e) {
      System.out.println("IO Exception.");
      }
  }
}
