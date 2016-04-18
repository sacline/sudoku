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
    SudokuBoard emptyboard = new SudokuBoard();
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        assertEquals(
            "Values of empty board must be 0", 0, emptyboard.getValue(i, j));
      }
    }
  }

  @Test
  public void fullBoardConstructorTest() {
    final String SOLVED_BOARD = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(SOLVED_BOARD);
    int index = 0;
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 10; j++) {
        assertEquals("Correct value in board",
            String.valueOf(SOLVED_BOARD.charAt(index++)),
            String.valueOf(board.getValue(i, j)));
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void boardLengthShortTest() {
    final String SHORT_BOARD = "12345";
    SudokuBoard board = new SudokuBoard(SHORT_BOARD);
  }

  @Test(expected = IllegalArgumentException.class)
  public void boardLengthLongTest() {
    final String LONG_BOARD = "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789";
    SudokuBoard board = new SudokuBoard(LONG_BOARD);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidCharacterBoardTest() {
    final String BAD_BOARD = "395+53&a959n39bo939129d$h1@h\nji@ILJ24BKL@K$KdjdjdkasBdk2k250sd90v80s3n3b353l%KJK#";
    SudokuBoard board = new SudokuBoard(BAD_BOARD);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidSmallRowTest() {
    int smallrow = -1;
    SudokuBoard board = new SudokuBoard();
    board.getValue(smallrow, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidLargeRowTest() {
    int largerow = 10;
    SudokuBoard board = new SudokuBoard();
    board.getValue(largerow, 5);
  }

  @Test
  public void copyBoardTest() {
    final String BOARD = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard originalboard = new SudokuBoard(BOARD);
    SudokuBoard copy = originalboard.copyBoard();
    assertEquals("Board copy test failed", originalboard, copy);
    //modify original board and make sure the copy does not change
    originalboard.setValue(1,1,2);
    assertFalse(originalboard.equals(copy));
    copy = originalboard.copyBoard();
    assertEquals("Board copy test failed", originalboard, copy);
  }

  @Test
  public void boardEqualityTest() {
    String one = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    String two = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard boardone = new SudokuBoard(one);
    SudokuBoard boardtwo = new SudokuBoard(two);
    assertEquals("Two of the same boards are not equal", boardone, boardtwo);
    assertEquals("Two of the same boards are not equal",
        boardone.hashCode(), boardtwo.hashCode());
    boardone.setValue(1, 1, 2);
    boardtwo.setValue(1, 1, 2);
    assertEquals("Two of the same boards are not equal", boardone, boardtwo);
    assertEquals("Two of the same boards are not equal",
        boardone.hashCode(), boardtwo.hashCode());
    boardone.addPencil(1, 1, 5);
    boardtwo.addPencil(1, 1, 5);
    assertEquals("Two of the same boards are not equal", boardone, boardtwo);
    assertEquals("Two of the same boards are not equal",
        boardone.hashCode(), boardtwo.hashCode());
  }

  @Test
  public void boardUndoMoveTest() {
    String boardstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardstring);
    board.setValue(1, 1, 2);
    board.undoMove();
    assertEquals(board.toString(), boardstring);
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
    String boardstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardstring);
    board.setValue(1, 1, 5);
    board.setValue(3, 5, 8);
    board.setValue(1, 2, 9);
    board.setValue(5, 7, 9);
    SudokuBoard original = board.originalBoard();
    assertEquals(original.toString(), boardstring);
    board = board.originalBoard();
    assertEquals(board.toString(), boardstring);
  }
}
