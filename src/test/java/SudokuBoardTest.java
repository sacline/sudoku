package com.github.sacline.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for SudokuBoard.
 * This class tests the public methods within SudokuBoard.
 * In general, simple getters and setters are not tested.
 */
public class SudokuBoardTest {

  @Test
  public void emptyBoardConstructorTest() {
    SudokuBoard emptyBoard = new SudokuBoard();
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        assertEquals(
            "Values of empty board must be 0", 0, emptyBoard.getValue(i, j));
      }
    }
  }

  @Test
  public void fullBoardConstructorTest() {
    final String solvedBoard = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedBoard);
    int index = 0;
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        assertEquals("Correct value in board",
            String.valueOf(solvedBoard.charAt(index++)),
            String.valueOf(board.getValue(i, j)));
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void boardLengthShortTest() {
    final String shortBoard = "12345";
    SudokuBoard board = new SudokuBoard(shortBoard);
  }

  @Test(expected = IllegalArgumentException.class)
  public void boardLengthLongTest() {
    final String longBoard = "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789";
    SudokuBoard board = new SudokuBoard(longBoard);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidCharacterBoardTest() {
    final String badBoard = "395+53&a959n39bo939129d$h1@h\nji@ILJ24BKL@K$KdjdjdkasBdk2k250sd90v80s3n3b353l%KJK#";
    SudokuBoard board = new SudokuBoard(badBoard);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidSmallRowTest() {
    int smallRow = -1;
    SudokuBoard board = new SudokuBoard();
    board.getValue(smallRow, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidLargeRowTest() {
    int largeRow = 10;
    SudokuBoard board = new SudokuBoard();
    board.getValue(largeRow, 5);
  }

  @Test
  public void copyBoardTest() {
    final String board = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard originalBoard = new SudokuBoard(board);
    SudokuBoard copy = originalBoard.copyBoard();
    assertEquals("Board copy test failed", originalBoard, copy);
    //modify original board and make sure the copy does not change
    originalBoard.setValue(1,1,2);
    assertFalse(originalBoard.equals(copy));
    copy = originalBoard.copyBoard();
    assertEquals("Board copy test failed", originalBoard, copy);
  }

  @Test
  public void boardEqualityTest() {
    String one = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    String two = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard boardOne = new SudokuBoard(one);
    SudokuBoard boardTwo = new SudokuBoard(two);
    assertEquals("Two of the same boards are not equal", boardOne, boardTwo);
    assertEquals("Two of the same boards are not equal",
        boardOne.hashCode(), boardTwo.hashCode());
    boardOne.setValue(1, 1, 2);
    boardTwo.setValue(1, 1, 2);
    assertEquals("Two of the same boards are not equal", boardOne, boardTwo);
    assertEquals("Two of the same boards are not equal",
        boardOne.hashCode(), boardTwo.hashCode());
    boardOne.addPencil(1, 1, 5);
    boardTwo.addPencil(1, 1, 5);
    assertEquals("Two of the same boards are not equal", boardOne, boardTwo);
    assertEquals("Two of the same boards are not equal",
        boardOne.hashCode(), boardTwo.hashCode());
  }

  @Test
  public void boardUndoMoveTest() {
    String boardString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardString);
    board.setValue(1, 1, 2);
    board.undoMove();
    assertEquals(board.toString(), boardString);
    board.addPencil(1, 1, 5);
    board.undoMove();
    assertTrue(board.getPencils(1, 1).size() == 0);
    board.addPencil(1, 1, 4);
    board.removePencil(1, 1, 4);
    assertTrue(board.getPencils(1, 1).size() == 0);
    board.undoMove();
    assertTrue(board.getPencils(1, 1).size() == 1);
    board.undoMove();
    assertTrue(board.getPencils(1, 1).size() == 0);
    //tests that the most recent move is undone
    board.setValue(1, 1, 5);
    board.setValue(1, 2, 4);
    board.setValue(1, 3, 3);
    board.undoMove();
    assertEquals(board.getValue(1, 3), 1);
    assertEquals(board.getValue(1, 2), 4);
    board.undoMove();
    assertEquals(board.getValue(1, 2), 9);
  }

  @Test
  public void originalBoardTest() {
    String boardString = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardString);
    board.setValue(1, 1, 5);
    board.setValue(3, 5, 8);
    board.setValue(1, 2, 9);
    board.setValue(5, 7, 9);
    SudokuBoard original = board.originalBoard();
    assertEquals(original.toString(), boardString);
    board = board.originalBoard();
    assertEquals(board.toString(), boardString);
  }
}
