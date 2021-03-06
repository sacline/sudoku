package com.github.sacline.sudoku;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Runs the classes for testing the sudoku board, and its solver.
 */
public class SudokuTestRunner {

  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(
        SudokuBoardTest.class, SudokuSolverTest.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
    if (result.wasSuccessful()) {
      System.out.println("All tests passed");
    }
  }
}
