import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Class containing methods for evaluating a SudokuBoard.
 * The SudokuSolver class contains algorithms to find the solution to an
 * incomplete SudokuBoard. A sudoku puzzle is solved once the values of its
 * squares satisfy the "One Rule": each row, column, and region must contain
 * the digits 1-9 exactly once.
 */
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
    for (int row = 1; row < 10; row += 3) {
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
    int[] unitValues = new int[9];
    for (int index = 0; index < 9; index++) {
      unitValues[index] = unit[index];
    }
    Arrays.sort(unitValues);
    return Arrays.equals(unitValues, target);
  }

  /**
   * Checks if the value violates the One Rule.
   *
   * @param board board to check
   * @param row row of cell
   * @param col column of cell
   * @param value value to check
   */
  public boolean validValue(SudokuBoard board, int row, int col, int value) {
    for (int cellValue : board.getRow(row)) {
      if (cellValue == value) {
        return false;
      }
    }
    for (int cellValue : board.getCol(col)) {
      if (cellValue == value) {
        return false;
      }
    }
    for (int cellValue : board.getReg(row, col)) {
      if (cellValue == value) {
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
   * <p>Existing pencils are overwritten completely in this process.
   *
   * @param row row of the cell
   * @param col column of the cell
   */
  public void generatePencils(SudokuBoard board, int row, int col) {
    if (board.getValue(row, col) != 0) {
      return;
    }
    ArrayList<Integer> possible = new ArrayList<Integer>();
    for (int number = 1; number < 10; number++) {
      possible.add(number);
    }
    for (int value : board.getRow(row)) {
      possible.remove(Integer.valueOf(value));
    }
    for (int value : board.getCol(col)) {
      possible.remove(Integer.valueOf(value));
    }
    for (int value : board.getReg(row, col)) {
      possible.remove(Integer.valueOf(value));
    }
    board.clearPencils(row, col);
    for (Integer pencil : possible) {
      board.addPencil(row, col, pencil);
    }
  }

  /**
   * Uses a brute force solution to solve the puzzle.
   * Backtracking method uses guess-and check brute force solution.
   *
   * @param board board to solve
   * @param optimize if true, optimizes with deterministic methods
   * @return a solved copy of the board
   */
  public SudokuBoard bruteForceSolve(SudokuBoard board, boolean optimize) {
    SudokuBoard newBoard = new SudokuBoard();
    ArrayList<Integer> unsolved = new ArrayList<Integer>();
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        newBoard.setValue(i, j, board.getValue(i,j));
        if (optimize) {
          optimize(newBoard);
        }
        generatePencils(newBoard, i, j);
        if (board.getValue(i, j) == 0) {
          //cells in the board numbered 1-81
          unsolved.add(Integer.valueOf((i - 1) * 9 + j));
        }
      }
    }
    int index = 0;
    bruteForce(newBoard, unsolved, index);
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        newBoard.clearPencils(i, j);
      }
    }
    return newBoard;
  }

  private void optimize(SudokuBoard board) {
    algorithmicSolve(board);
  }

  private void bruteForce(
      SudokuBoard board, ArrayList<Integer> unsolved, int index) {
    if (isSolved(board)) {
      return;
    }
    //for loop checks all possible pencils at the index
    //i = row, j = col
    int col = ((unsolved.get(index) - 1) % 9) + 1;
    int row = (unsolved.get(index) - col) / 9 + 1;
    for (Integer pencil : board.getPencils(row, col)) {
      if (validValue(board, row, col, pencil)) {
        board.setValue(row, col, pencil);
        bruteForce(board, unsolved, index + 1);
        if (!isSolved(board)) {
          board.setValue(row, col, 0);
        }
      }
    }
  }

  /**
   * Returns a solved version of the passed board.
   * It solves the board algorithmically, square-by-square.
   *
   * @param board the board to solve
   * @return solved version of board
   */
  public SudokuBoard solve(SudokuBoard board) {
    SudokuBoard solvedBoard = board.copyBoard();
    algorithmicSolve(solvedBoard);
    return solvedBoard;
  }

  /**
   * Attempts to solve the board with algorithms.
   *
   * @param board the board to solve
   */
  private void algorithmicSolve(SudokuBoard board) {
    final int MAXIMUM_ITERATIONS = 20;
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        generatePencils(board, i, j);
      }
    }
    //while (!(isSolved(board))) {
    for (int iter = 1; iter < MAXIMUM_ITERATIONS; iter++) {
      SudokuBoard initialBoard = board.copyBoard();
      //execute algorithms to solve it
      singlePosition(board);
      singleCandidate(board);
      candidateLine(board);
      multipleLines(board);
      //end the loop if board is unchanged
      if (board.equals(initialBoard)) {
        break;
      }
    }
  }

  /**
   * Rates puzzle difficulty based on methods reqiured to solve it.
   *
   * @param origboard the board to rate
   * @return the difficulty as a string-easy, medium, or hard
   */
  public String difficultyFinder(SudokuBoard origboard) {
    SudokuBoard board = origboard.copyBoard();
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        generatePencils(board, i, j);
      }
    }
    final int MAXIMUM_ITERATIONS = 50;
    String difficulty = "";
    if (isSolved(board)) {
      return difficulty;
    }
    for (int iter = 1; iter < MAXIMUM_ITERATIONS; iter++) {
      singlePosition(board);
      singleCandidate(board);
    }
    if (isSolved(board)) {
      difficulty = "easy";
      return difficulty;
    }
    for (int iter = 1; iter < MAXIMUM_ITERATIONS; iter++) {
      singlePosition(board);
      singleCandidate(board);
      candidateLine(board);
      multipleLines(board);
    }
    if (isSolved(board)) {
      difficulty = "medium";
      return difficulty;
    }
    difficulty = "hard";
    return difficulty;
  }

  /**
   * Solves "Single Position" squares on the board.
   * A Single Position occurs when a digit has only
   * one valid place it can be in its row, column, or region.
   *
   * @param board the board to check
   */
  private void singlePosition(SudokuBoard board) {
    //check each row
    for (int r = 1; r < 10; r++) {
      int[] penCount = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      int[] cols = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      for (int c = 1; c < 10; c++) {
        int val = board.getValue(r, c);
        if (val == 0) {
          for (Integer pen : board.getPencils(r, c)) {
            penCount[pen - 1] += 1;
            cols[pen - 1] = c;
          }
        }
      }
      for (int val = 1; val < 10; val++) {
        if (penCount[val - 1] == 1) {
          board.setValue(r, cols[val - 1], val);
          board.clearPencils(r, cols[val - 1]);
          removePencils(board, r, cols[val - 1], val);
        }
      }
    }
    //check each column
    for (int c = 1; c < 10; c++) {
      int[] penCount = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      int[] rows = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      for (int r = 1; r < 10; r++) {
        int val = board.getValue(r, c);
        if (val == 0) {
          for (Integer pen : board.getPencils(r, c)) {
            penCount[pen - 1] += 1;
            rows[pen - 1] = r;
          }
        }
      }
      for (int val = 1; val < 10; val++) {
        if (penCount[val - 1] == 1) {
          board.setValue(rows[val - 1], c, val);
          board.clearPencils(rows[val - 1], c);
          removePencils(board, rows[val - 1], c, val);
        }
      }
    }
    //check each region
    for (int reg = 1; reg < 10; reg++) {
      int[] startingRow = {1, 1, 1, 4, 4, 4, 7, 7, 7};
      int[] startingCol = {1, 4, 7, 1, 4, 7, 1, 4, 7};
      int[] penCount = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      int[] rows = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      int[] cols = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      for (int row = startingRow[reg - 1];
          row < startingRow[reg - 1] + 3; row++) {
        for (int col = startingCol[reg - 1];
            col < startingRow[reg - 1] + 3; col++) {
          int val = board.getValue(row, col);
          if (val == 0) {
            for (Integer pen : board.getPencils(row, col)) {
              penCount[pen - 1] += 1;
              rows[pen - 1] = row;
              cols[pen - 1] = col;
            }
          }
        }
      }
      for (int val = 1; val < 10; val++) {
        if (penCount[val - 1] == 1) {
          board.setValue(rows[val - 1], cols[val - 1], val);
          board.clearPencils(rows[val - 1], cols[val - 1]);
          removePencils(board, rows[val - 1], cols[val - 1], val);
        }
      }
    }
  }

  /**
   * Solves "Single Candidate" squares on the board.
   * A Single Candidate occurs when a square has only one possible
   * digit.
   *
   * @param board the board to check
   */
  private void singleCandidate(SudokuBoard board) {
    //if there is only one pencil in the square, that is the value.
    for (int row = 1; row < 10; row++) {
      for (int col = 1; col < 10; col++) {
        if (board.getPencils(row, col).size() == 1
            && board.getValue(row, col) == 0) {
          int val = board.getPencils(row, col).get(0);
          board.setValue(row, col, val);
          board.clearPencils(row, col);
          removePencils(board, row, col, val);
        }
      }
    }
  }

  /**
   * Searches for "Candidate Lines" and removes pencils accordingly.
   * Within a region, if a value is only a candidate in cells that
   * fall on a line, the value can be eliminated as a possibility
   * in other cells along the line (row/col) outside of that region.
   *
   * @param board the board to search
   */
  private void candidateLine(SudokuBoard board) {
    for (int reg = 1; reg < 10; reg++) {
      int[] startingRow = {1, 1, 1, 4, 4, 4, 7, 7, 7};
      int[] startingCol = {1, 4, 7, 1, 4, 7, 1, 4, 7};
      int[] pencilCount = {0, 0, 0, 0, 0, 0, 0, 0, 0};
      //make ArrayList of ArrayLists to hold pencil rows and cols
      ArrayList<ArrayList<Integer>> penRows =
          new ArrayList<ArrayList<Integer>>();
      ArrayList<ArrayList<Integer>> penCols =
          new ArrayList<ArrayList<Integer>>();
      for (int val = 1; val < 10; val++) {
        penRows.add(new ArrayList<Integer>());
        penCols.add(new ArrayList<Integer>());
      }
      for (int row = startingRow[reg - 1];
          row < startingRow[reg - 1] + 3; row++) {
        for (int col = startingCol[reg - 1];
            col < startingCol[reg - 1] + 3; col++) {
          //Add the row and column of each pencil to the lists
          for (Integer pen : board.getPencils(row, col)) {
            pencilCount[pen - 1]++;
            penRows.get(pen - 1).add((Integer)row);
            penCols.get(pen - 1).add((Integer)col);
          }
        }
      }
      for (int val = 1; val < 10; val++) {
        int penNumber = penRows.get(val - 1).size();
        if (penNumber == 2 || penNumber == 3) {
          if (areCollinear(penRows.get(val - 1))) {
            int row = penRows.get(val - 1).get(0);
            for (int col = 1; col < 10; col++) {
              if (col >= startingCol[reg - 1]
                  && col < startingCol[reg - 1] + 3) {
                continue;
              }
              board.removePencil(row, col, val);
            }
          }
          if (areCollinear(penCols.get(val - 1))) {
            int col = penCols.get(val - 1).get(0);
            for (int row = 1; row < 10; row++) {
              if (row >= startingRow[reg - 1]
                  && row < startingRow[reg - 1] + 3) {
                continue;
              }
              board.removePencil(row, col, val);
            }
          }
        }
      }
    }
  }

  /**
   * Tests an ArrayList of row or column numbers for collinearity.
   */
  private boolean areCollinear(ArrayList<Integer> points) {
    int first = points.get(0);
    for (Integer point : points) {
      if (!(point.equals(first))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Uses "Multiple Lines" strategy to remove pencils from the board.
   *
   * <p>For more information see:
   * https://www.sudokuoftheday.com/techniques/multiple-lines
   * @param board board to search
   */
  private void multipleLines(SudokuBoard board) {
    //loop through six times (3 times for rows, 3 times for columns)
    //in each loop, look at the 3 region-pairs.
    //for each region-pair, look at the values missing from both regions
    //if they exist as pencils in only 2 (row/cols), eliminate other pencils

    //region-pairs to check
    int[] pair1 = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 6, 7, 7, 8};
    int[] pair2 = {2, 3, 4, 7, 3, 5, 8, 6, 9, 5, 6, 7, 6, 8, 9, 8, 9, 9};
    int[] startingRow = {1, 1, 1, 4, 4, 4, 7, 7, 7};
    int[] startingCol = {1, 4, 7, 1, 4, 7, 1, 4, 7};

    for (int pair = 0; pair < 18; pair++) {
      int reg1 = pair1[pair];
      int reg2 = pair2[pair];

      //row comparison
      if ((reg2 - reg1) < 3) {
        ArrayList<Integer> reg1Remaining = regRemaining(board, reg1);
        ArrayList<Integer> reg2Remaining = regRemaining(board, reg2);
        //change reg1Remaining to the intersection of the lists
        reg1Remaining.retainAll(reg2Remaining);

        //for each remaining value, add pencil row to reg list
        for (Integer rem : reg1Remaining) {
          ArrayList<Integer> reg1rows = new ArrayList<Integer>();
          ArrayList<Integer> reg2rows = new ArrayList<Integer>();
          for (int row = startingRow[reg1 - 1];
              row < startingRow[reg1 - 1] + 3; row++) {
            //check reg1 for rows containing the number
            for (int col = startingCol[reg1 - 1];
                col < startingCol[reg1 - 1] + 3; col++) {
              if (board.getPencils(row, col).contains(rem)) {
                reg1rows.add((Integer) row);
              }
            }
            //check reg2 for rows containing the number
            for (int col = startingCol[reg2 - 1];
                col < startingCol[reg2 - 1] + 3; col++) {
              if (board.getPencils(row, col).contains(rem)) {
                reg2rows.add((Integer) row);
              }
            }
          }
          //check row lists to see if each list has the same two rows
          if (multipleLinesCheck(reg1rows, reg2rows)) {
            //take the two rows, and remove pencils from the third reg.
            for (Integer row : new HashSet<Integer>(reg1rows)) {
              for (int col = 1; col < 10; col++) {
                //ignore reg1
                if (col >= startingCol[reg1 - 1]
                    && col < startingCol[reg1 - 1] + 3) {
                  continue;
                }
                //ignore reg2
                if (col >= startingCol[reg2 - 1]
                    && col < startingCol[reg2 - 1] + 3) {
                  continue;
                }
                board.removePencil(row, col, rem);
              }
            }
          }
        }
      } else {
        ArrayList<Integer> reg1Remaining = regRemaining(board, reg1);
        ArrayList<Integer> reg2Remaining = regRemaining(board, reg2);
        //change reg1Remaining to the intersection of the lists
        reg1Remaining.retainAll(reg2Remaining);

        //for each remaining value, add pencil row to reg list
        for (Integer rem : reg1Remaining) {
          ArrayList<Integer> reg1cols = new ArrayList<Integer>();
          ArrayList<Integer> reg2cols = new ArrayList<Integer>();
          for (int col = startingCol[reg1 - 1];
              col < startingCol[reg1 - 1] + 3; col++) {

            //check reg1 for rows containing the number
            for (int row = startingRow[reg1 - 1];
                row < startingRow[reg1 - 1] + 3; row++) {
              if (board.getPencils(row, col).contains(rem)) {
                reg1cols.add((Integer) col);
              }
            }
            //check reg2 for rows containing the number
            for (int row = startingRow[reg2 - 1];
                row < startingRow[reg2 - 1] + 3; row++) {
              if (board.getPencils(row, col).contains(rem)) {
                reg2cols.add((Integer) col);
              }
            }
          }
          //check row lists to see if each list has the same two rows
          if (multipleLinesCheck(reg1cols, reg2cols)) {
            //take the two rows, and remove pencils from the third reg.
            for (Integer col : new HashSet<Integer>(reg1cols)) {
              for (int row = 1; row < 10; row++) {
                //ignore reg1
                if (row >= startingRow[reg1 - 1]
                    && row < startingRow[reg1 - 1] + 3) {
                  continue;
                }
                //ignore reg2
                if (row >= startingRow[reg2 - 1]
                    && row < startingRow[reg2 - 1] + 3) {
                  continue;
                }
                board.removePencil(row, col, rem);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Checks lists of row/columns to determine if Multiple Lines conditions
   * are met.
   */
  private boolean multipleLinesCheck(
      ArrayList<Integer> list1, ArrayList<Integer> list2) {
    HashSet<Integer> hs1 = new HashSet<Integer>(list1);
    HashSet<Integer> hs2 = new HashSet<Integer>(list2);
    if (hs1.equals(hs2) && hs1.size() == 2) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns an ArrayList of values not yet existing in the row.
   */
  private ArrayList<Integer> rowRemaining(SudokuBoard board, int row) {
    ArrayList<Integer> possible = new ArrayList<Integer>();
    for (int val = 1; val < 10; val++) {
      possible.add((Integer) val);
    }
    for (int col = 1; col < 10; col++) {
      possible.remove((Integer)board.getValue(row, col));
    }
    return possible;
  }

  /**
   * Returns an ArrayList of values not yet existing in the col.
   */
  private ArrayList<Integer> colRemaining(SudokuBoard board, int col) {
    ArrayList<Integer> possible = new ArrayList<Integer>();
    for (int val = 1; val < 10; val++) {
      possible.add((Integer) val);
    }
    for (int row = 1; row < 10; row++) {
      possible.remove((Integer)board.getValue(row, col));
    }
    return possible;
  }

  /**
   * Returns an ArrayList of values not yet existing in the reg.
   */
  private ArrayList<Integer> regRemaining(SudokuBoard board, int reg) {
    int[] startingRow = {1, 1, 1, 4, 4, 4, 7, 7, 7};
    int[] startingCol = {1, 4, 7, 1, 4, 7, 1, 4, 7};
    ArrayList<Integer> possible = new ArrayList<Integer>();
    for (int val = 1; val < 10; val++) {
      possible.add((Integer) val);
    }
    for (int row = startingRow[reg - 1];
        row < startingRow[reg - 1] + 3; row++) {
      for (int col = startingCol[reg - 1];
          col < startingCol[reg - 1] + 3; col++) {
        possible.remove((Integer)board.getValue(row, col));
      }
    }
    return possible;
  }

  /**
   * Removes a value from pencils in the cells' row, col, and region.
   *
   * @param board board to use
   * @param row row of the cell
   * @param col column of the cell
   * @param val value to remove from pencils
   */
  private void removePencils(SudokuBoard board, int row, int col, int val) {
    //remove pencil from col
    for (int r = 1; r < 10; r++) {
      board.removePencil(r, col, val);
    }
    //remove pencil from row
    for (int c = 1; c < 10; c++) {
      board.removePencil(row, c, val);
    }
    //remove pencil from region
    int region = board.findReg(row, col);
    int[] startingRow = {1, 1, 1, 4, 4, 4, 7, 7, 7};
    int[] startingCol = {1, 4, 7, 1, 4, 7, 1, 4, 7};
    for (int r = startingRow[region - 1];
        r < startingRow[region - 1] + 3; r++) {
      for (int c = startingCol[region - 1];
          c < startingCol[region - 1] + 3; c++) {
        board.removePencil(r, c, val);
      }
    }
  }

  /** Method for simple tests. */
  public static void main(String[] args) {
    try {
      int counter = 0;
      BufferedReader in = new BufferedReader(new FileReader(args[0]));
      String puzzle;
      SudokuSolver solver = new SudokuSolver();
      while ((puzzle = in.readLine()) != null) {
        SudokuBoard board = new SudokuBoard(puzzle);
        SudokuBoard solvedBoard = new SudokuBoard();
        solvedBoard = solver.bruteForceSolve(board, true);
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found.");
    } catch (IOException e) {
      System.out.println("IO Error.");
    }
  }
}
