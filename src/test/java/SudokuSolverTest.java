package com.github.sacline.sudoku;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for SudokuSolver.
 * This class tests the public methods within SudokuSolver.
 */
public class SudokuSolverTest {

  @Test
  public void checkRowsTest() {
    String solvedString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedString);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.checkRows(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.checkRows(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.checkRows(board));
  }

  @Test
  public void checkColsTest() {
    String solvedString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedString);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.checkCols(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.checkCols(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.checkCols(board));
  }

  @Test
  public void checkRegsTest() {
    String solvedString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedString);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.checkRegs(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.checkRegs(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.checkRegs(board));
  }

  @Test
  public void isSolvedTest() {
    String solvedString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedString);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.isSolved(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.isSolved(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.isSolved(board));
  }

  @Test
  public void validValueTest() {
    String boardString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardString);
    SudokuSolver solver = new SudokuSolver();
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        for (int val = 1; val < 10; val++) {
          assertFalse(solver.validValue(board, i, j, val));
        }
      }
    }
    board.setValue(1,1,0);
    assertTrue(solver.validValue(board, 1, 1, 3));
  }

  @Test
  public void generatePencilsTest() {
    String boardString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardString);
    SudokuSolver solver = new SudokuSolver();
    ArrayList<Integer> compareList = new ArrayList<Integer>();
    compareList.add(new Integer(3));
    board.setValue(1,1,0);
    solver.generatePencils(board, 1, 1);
    assertTrue(board.getPencils(1, 1).equals(compareList));
    compareList.add(new Integer(4));
    assertFalse(board.getPencils(1, 1).equals(compareList));
  }

  @Test
  public void bruteForceTest() {
    String boardString = "094000130000000000000076002080010000032000000000200060000050400000008007006304008";
    String solvedString = "794582136268931745315476982689715324432869571157243869821657493943128657576394218";
    SudokuBoard unsolvedBoard = new SudokuBoard(boardString);
    SudokuBoard solvedBoard = new SudokuBoard(solvedString);
    SudokuSolver solver = new SudokuSolver();
    SudokuBoard compareBoard = solver.bruteForceSolve(unsolvedBoard, false);
    assertTrue(solvedBoard.equals(compareBoard));
  }

  @Test
  public void solveTest() {
    ArrayList<String> puzzles = new ArrayList<String>();
    //Single position board
    puzzles.add("006030708030000001200000600100350006079040150500017004002000007600000080407060200");
    //Single candidate board
    puzzles.add("004006020007800910000000308018300200300789001009001060803000500045003600026500100");
    //Candidate lines board
    puzzles.add("001957063000806070769130805007261350312495786056378000108609507090710608674583000");
    //Double pairs board (solvable by multiple lines technique)
    puzzles.add("934060050006004923008900046800546007600010005500390062360401270470600500080000634");
    //Multiple lines board
    puzzles.add("009030600036014089100869035090000800010000090068090170601903002972640300003020900");

    SudokuSolver solver = new SudokuSolver();
    for (String puzzle : puzzles) {
      SudokuBoard board = new SudokuBoard(puzzle);
      SudokuBoard bruteForce = solver.bruteForceSolve(board, false);
      SudokuBoard nonBruteForce = solver.solve(board);
      assertTrue(bruteForce.toString().equals(nonBruteForce.toString()));
    }
  }
}
