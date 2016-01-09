/**
 * Basic building sudoku board building block.
 * <p> 
 * SudokuSquare is the class of objects that will make up a sudoku board.
 * Each square will be able to hold a single value as well as multiple
 * penciled values.
 * 
 * @version 1.0
 */
public class SudokuSquare {
  private boolean[] pencils;
  private int value;

/**
 * Constructs an empty SudokuSquare.
 * Pencils set to false, representing no penciled values.
 */
  public SudokuSquare() {
    pencils = new boolean[9];
    for (boolean pencil : pencils) {
      pencil = false;
    }
    this.value = 0;
  }

/**
 * Constructs a SudokuSquare with the passed value.
 * @param value value to create the square with
 */
  public SudokuSquare(int value) {
    pencils = new boolean[9];
    for (boolean pencil : pencils) {
      pencil = false;
    }
      this.value = value;
  }

/**
 * Sets the square value.
 * Value must be between 1 and 9 inclusive.
 * @param value desired value of square
 */
  public void setValue(int value) {
    validateValue(value);
    this.value = value;
  }

/**
 * Returns the square value. 
 */
  public int getValue() {
    return value;
  }

/**
 * Sets a penciled value.
 * @param value value to pencil in
 */
  public void setPencil(int value) {
    validateValue(value);
    pencils[value - 1] = true;
  }

/**
 * Removes a penciled value.
 * @param value value to unpencil
 */
  public void unPencil(int value) {
    validateValue(value);
    pencils[value - 1] = false;
  }

/**
 * Returns an array of the pencils at the square.
 */
  public boolean[] getPencils() {
    return pencils;
  }

/**
 * Removes all penciled values.
 */
  public void clearPencils() {
    for (boolean pencil : pencils) {
      pencil = false;
    }
  }

/**
 * Returns (empty/full) state of square.
 */
  public boolean isFull() {
    if (value != 0) { 
      return true;
    } else {
      return false;
    }
  }

/**
 * Throws exceptions for a bad input.
 * @param value value to check
 */
  private void validateValue(int value) {
    if (value < 1 || value > 9) {
      throw new IllegalArgumentException("Value must be between 1 and 9.");
    }
  }
}
