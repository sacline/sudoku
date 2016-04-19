import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Test class for SudokuSolver.
 * This class tests the public methods within SudokuSolver.
 */
public class SudokuSolverTest {

  @Test
  public void checkRowsTest() {
    String solvedstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedstring);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.checkRows(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.checkRows(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.checkRows(board));
  }

  @Test
  public void checkColsTest() {
    String solvedstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedstring);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.checkCols(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.checkCols(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.checkCols(board));
  }

  @Test
  public void checkRegsTest() {
    String solvedstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedstring);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.checkRegs(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.checkRegs(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.checkRegs(board));
  }

  @Test
  public void isSolvedTest() {
    String solvedstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(solvedstring);
    SudokuSolver solver = new SudokuSolver();
    assertTrue(solver.isSolved(board));
    board.setValue(1,1,4); //changes a value so next call is false
    assertFalse(solver.isSolved(board));
    board.setValue(1,1,3); //changes value back to original value
    assertTrue(solver.isSolved(board));
  }

  @Test
  public void validValueTest() {
    String boardstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardstring);
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
    String boardstring = "391286574487359126652714839875431692213967485964528713149673258538142967726895341";
    SudokuBoard board = new SudokuBoard(boardstring);
    SudokuSolver solver = new SudokuSolver();
    ArrayList<Integer> comparelist = new ArrayList<Integer>();
    comparelist.add(new Integer(3));
    board.setValue(1,1,0);
    solver.generatePencils(board, 1, 1);
    assertTrue(board.getPencils(1, 1).equals(comparelist));
    comparelist.add(new Integer(4));
    assertFalse(board.getPencils(1, 1).equals(comparelist));
  }

  @Test
  public void bruteForceTest() {
    String boardstring = "094000130000000000000076002080010000032000000000200060000050400000008007006304008";
    String solvedstring = "794582136268931745315476982689715324432869571157243869821657493943128657576394218";
    SudokuBoard unsolvedboard = new SudokuBoard(boardstring);
    SudokuBoard solvedboard = new SudokuBoard(solvedstring);
    SudokuSolver solver = new SudokuSolver();
    SudokuBoard compareboard = solver.bruteForceSolve(unsolvedboard, false);
    assertTrue(solvedboard.equals(compareboard));
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
      SudokuBoard bruteforce = solver.bruteForceSolve(board, false);
      SudokuBoard nonbruteforce = solver.solve(board);
      assertTrue(bruteforce.toString().equals(nonbruteforce.toString()));
    }
  }
}
