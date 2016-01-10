/**
 * Basic building sudoku board building block.
 * <p> 
 * SudokuSquare is the class of objects that will make up a sudoku board.
 * Each square will be able to hold a single value as well as multiple
 * penciled values.
 * 
 * @version 1.0
 */

import java.util.Collections;
import java.util.ArrayList;

public class SudokuSquare {
  private ArrayList<Integer> pencils;
  private int value;

/**
 * Constructs an empty SudokuSquare.
 * Pencils set to false, representing no penciled values.
 */
  public SudokuSquare() {
    pencils = new ArrayList<Integer>();
    this.value = 0;
  }

/**
 * Constructs a SudokuSquare with the passed value.
 * @param value value to create the square with
 */
  public SudokuSquare(int value) {
    pencils = new ArrayList<Integer>();
    validateValue(value);
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
 * Adds a penciled value.
 * @param value value to pencil in
 */
  public void addPencil(int value) {
    validateValue(value);
    pencils.add(value);
    Collections.sort(pencils);
  }

/**
 * Removes a penciled value.
 * @param value value to unpencil
 */
  public void removePencil(int value) {
    validateValue(value);
    pencils.remove(new Integer(value));
  }

/**
 * Returns the list of pencils at the square.
 */
  public ArrayList<Integer> getPencils() {
    return pencils;
  }

/**
 * Removes all penciled values.
 */
  public void clearPencils() {
    pencils.clear();
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

/**
 * Method for basic testing.
 */
  public static void main(String[] args) {
    SudokuSquare ss = new SudokuSquare();
    //ss.setValue(5);
    //System.out.println(ss.getValue());
    System.out.println(ss.getPencils());
  }
}
