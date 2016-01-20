import static org.junit.Assert.assertEquals;
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
}
