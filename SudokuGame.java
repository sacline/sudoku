import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * GUI for Sudoku puzzle game.
 */
public class SudokuGame extends Application {

  private static final double WINDOW_WIDTH = 800;
  private static final double WINDOW_HEIGHT = 600;
  private static final String PUZZLE_FILENAME = "100gamepuzzles.txt";

  private boolean editPencils;
  private Cell selectedCell;

  private Stage stage;

  private SudokuBoard currentBoard;
  private SudokuBoard currentSolution;
  private SudokuSolver solver = new SudokuSolver();

  private ArrayList<SudokuBoard> easyPuzzles = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> easySolutions = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> mediumPuzzles = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> mediumSolutions = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> hardPuzzles = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> hardSolutions = new ArrayList<SudokuBoard>();

  /**
   * Method that launches when the game runs.
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;
    initializeGame();
  }

  /**
   * Handles actions that take place every time the game starts.
   */
  private void initializeGame() throws IOException {
    loadPuzzles(PUZZLE_FILENAME);
    stage.setTitle("Sudoku");
    stage.setScene(buildMenuScene());
    stage.setResizable(false);
    stage.show();
  }

  /**
   * Parses the puzzle input file, converting strings to boards.
   *
   * @param filename the properly-formatted input file
   */
  private void loadPuzzles(String filename) throws IOException {
    BufferedReader br = new BufferedReader((new FileReader(filename)));
    String line;
    while ((line = br.readLine()) != null) {
      String diff = line.substring(164,165);
      String puzzleString = line.substring(0,81);
      String solutionString = line.substring(82,163);
      if (diff.equals("e")) {
        easyPuzzles.add(new SudokuBoard(puzzleString));
        easySolutions.add(new SudokuBoard(solutionString));
      }
      if (diff.equals("m")) {
        mediumPuzzles.add(new SudokuBoard(puzzleString));
        mediumSolutions.add(new SudokuBoard(solutionString));
      }
      if (diff.equals("h")) {
        hardPuzzles.add(new SudokuBoard(puzzleString));
        hardSolutions.add(new SudokuBoard(solutionString));
      }
    }
    br.close();
  }

  /**
   * Returns the menu scene that is generated upon launching the game.
   * The game menu contains a title and a gridpane of buttons
   */
  private Scene buildMenuScene() {
    BorderPane root = new BorderPane();

    root.setTop(buildGameTitle());
    root.setAlignment(root.getTop(), Pos.CENTER);
    root.setMargin(root.getTop(), new Insets(5));

    root.setBottom(buildMenu());
    root.setAlignment(root.getBottom(), Pos.TOP_CENTER);
    root.setMargin(root.getBottom(), new Insets(0, 0, 400, 0));

    Scene menuScene;
    menuScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    return menuScene;
  }

  /**
   * Returns the game title Text object.
   */
  private Text buildGameTitle() {
    Text menuTitle = new Text("Sudoku");
    menuTitle.setFill(Color.RED);
    menuTitle.setFont(new Font(50));
    return menuTitle;
  }

  /**
   * Returns the grid pane containing the New Game title and buttons.
   */
  private GridPane buildMenu() {
    GridPane middle = new GridPane();

    Text newGameTitle = new Text("New Game");
    newGameTitle.setFill(Color.BLUE);
    newGameTitle.setFont(new Font(30));
    middle.add(newGameTitle, 1, 0);
    middle.setHalignment(newGameTitle, HPos.CENTER);
    middle.setPadding(new Insets(50, 0, 0, 0));

    Button newEasy = new Button("Easy");
    newEasy.setFocusTraversable(false);
    newEasy.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildGameScene("easy"));
        stage.show();
      }
    });
    middle.add(newEasy, 0, 1);
    middle.setHalignment(newEasy, HPos.RIGHT);

    Button newMedium = new Button("Medium");
    newMedium.setFocusTraversable(false);
    newMedium.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildGameScene("medium"));
      }
    });
    middle.add(newMedium, 1, 1);
    middle.setHalignment(newMedium, HPos.CENTER);

    Button newHard = new Button("Hard");
    newHard.setFocusTraversable(false);
    newHard.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildGameScene("hard"));
      }
    });
    middle.add(newHard, 2, 1);
    middle.setHalignment(newHard, HPos.LEFT);

    Button exitGame = new Button("Exit Game");
    exitGame.setFocusTraversable(false);
    exitGame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
          Platform.exit();
      }
    });
    middle.add(exitGame, 1, 3);
    middle.setHalignment(exitGame, HPos.CENTER);
    middle.setVgap(30);

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPercentWidth(50);
    middle.getColumnConstraints().addAll(cc, cc, cc);

    return middle;
  }

  /**
   * Returns a new game scene.
   * The scene contains an event handler for typed keys. This allows keyboard
   * input to the game. The keyboard input is allowed only if a cell has first
   * been clicked.
   *
   * @param difficulty the desired game difficulty
   */
  private Scene buildGameScene(String difficulty) {
    BorderPane bp = new BorderPane();
    Scene gameScene = new Scene(bp, WINDOW_WIDTH, WINDOW_HEIGHT);

    setCurrentBoard(difficulty);

    bp.setCenter(buildGameBoard());
    bp.setAlignment(bp.getCenter(), Pos.CENTER);
    bp.setBottom(buildGameBottom());
    bp.setAlignment(bp.getBottom(), Pos.TOP_CENTER);
    bp.setRight(buildGameRight());
    bp.setAlignment(bp.getRight(), Pos.CENTER);

    editPencils = false;

    gameScene.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
          if (selectedCell != null) {
            int value = selectedCell.keyToInt(event);
            if (value != -1) {
              selectedCell.updateCell(value);
              selectedCell = null;
            }
          }
      }
    });
    return gameScene;
  }

  /**
   * Selects a random board from the input to use in the game.
   *
   * @param difficulty difficulty of board to select
   */
  private void setCurrentBoard(String difficulty) {
    Random random = new Random();
    if (difficulty == "easy") {
      int index = random.nextInt(easyPuzzles.size());
      currentBoard = easyPuzzles.get(index);
      currentSolution = easySolutions.get(index);
    }
    if (difficulty == "medium") {
      int index = random.nextInt(mediumPuzzles.size());
      currentBoard = mediumPuzzles.get(index);
      currentSolution = mediumSolutions.get(index);
    }
    if (difficulty == "hard") {
      int index = random.nextInt(hardPuzzles.size());
      currentBoard = hardPuzzles.get(index);
      currentSolution = hardSolutions.get(index);
    }
  }

  /**
   * Returns a gridpane representing the sudoku board.
   * Each sudoku board is a grid of Cell objects
   */
  private GridPane buildGameBoard() {
    GridPane gameBoard = new GridPane();
    for (int row = 1; row < 10; row++) {
      for (int col = 1; col < 10; col++) {
        gameBoard.add(((new Cell(row, col)).getPane()), col - 1, row - 1);
      }
    }
    gameBoard.setPadding(new Insets(0, 0, 0, 157));
    return gameBoard;
  }

  /**
   * Returns the bottom HBox containing the quit button.
   */
  private HBox buildGameBottom() {
    HBox bottomBox = new HBox();
    bottomBox.setAlignment(Pos.CENTER);

    Button menuButton = new Button("Return to main menu");
    menuButton.setFocusTraversable(false);
    menuButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildMenuScene());
      }
    });
    bottomBox.getChildren().addAll(menuButton);
    bottomBox.setPadding(new Insets(0, 0, 50, 0));
    return bottomBox;
  }

  /**
   * Returns VBox on right side of game scene containing control buttons.
   */
  private VBox buildGameRight() {
    VBox rightBox = new VBox();
    rightBox.setAlignment(Pos.CENTER);
    rightBox.setPadding(new Insets(0, 25, 0, 0));
    rightBox.setSpacing(10);

    Text pencilIndicator = new Text("Pencils = On");
    pencilIndicator.setFill(Color.BLUE);
    pencilIndicator.setVisible(false);

    Button togglePencils = new Button("Toggle Pencils");
    togglePencils.setFocusTraversable(false);
    togglePencils.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        editPencils = !editPencils;
        pencilIndicator.setVisible(editPencils);
      }
    });

    Text puzzleSolved = new Text();
    puzzleSolved.setFill(Color.GREEN);
    puzzleSolved.setVisible(false);

    Button checkPuzzle = new Button("Check puzzle");
    checkPuzzle.setFocusTraversable(false);
    checkPuzzle.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (solver.isSolved(currentBoard)) {
          puzzleSolved.setText("Puzzle is solved!");
          puzzleSolved.setVisible(true);
        } else {
          puzzleSolved.setText("Puzzle not solved!");
          puzzleSolved.setVisible(true);
        }
      }
    });

    rightBox.getChildren().addAll(
        pencilIndicator, togglePencils, puzzleSolved, checkPuzzle);
    return rightBox;
  }

  /**
   * Each cell represents a square of the sudoku board.
   */
  private class Cell {
    Integer row;
    Integer col;
    StackPane pane;
    Color origColor;
    boolean editable;

    /**
     * Cell constructor.
     */
    private Cell(int row, int col) {
      this.row = new Integer(row);
      this.col = new Integer(col);
      this.pane = new StackPane();
      setEditable();
      pane.getChildren().addAll(buildRect(), buildValue(), buildPencils());
      buildPaneBorder();
      setPaneAction();
      updateVisibility();
    }

    /**
     * Returns the Cell's StackPane.
     */
    private StackPane getPane() {
      return pane;
    }

    /**
     * Makes the cell editable if value is not defined in the initial puzzle.
     */
    private void setEditable() {
      if (currentBoard.getValue(row, col) != 0) {
        editable = false;
      } else {
        editable = true;
      }
    }

    /**
     * Defines behavior for a cell when it is clicked.
     */
    private void setPaneAction() {
      if (editable) {
        Cell thisCell = this;
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (selectedCell == null) {
              ((Rectangle)pane.getChildren().get(0)).setFill(Color.YELLOW);
              selectedCell = thisCell;
            }
          }
        });
      }
    }

    /**
     * Converts a KeyEvent to an int that can modify the board.
     *
     * @param event the event generated by a typed key
     */
    private int keyToInt(KeyEvent event) {
      String character = event.getCharacter();
      //if -1 is returned an error message will appear
      int value = -1;
      try {
        value = Integer.parseInt(character);
      } catch (java.lang.NumberFormatException e) {
        System.out.println("Invalid character entered. Please try again.");
      }
      return value;
    }

    /**
     * Handles updates to a cell's pencil or value.
     *
     * @param value value being added to or removed from the cell
     */
    private void updateCell(int value) {
      modifyBoard(value);
      pane.getChildren().set(1, buildValue());
      updateVisibility();
      ((Rectangle)pane.getChildren().get(0)).setFill(origColor);
    }

    /**
     * Returns the Rectangle shape for the cell.
     * Cells in even-numbered regions are different colors than those in
     * odd-numbered regions
     */
    private Rectangle buildRect() {
      Color rectColor;
      final int dimension = 50;
      if (SudokuBoard.findReg(row, col) % 2 == 0) {
        rectColor = Color.AQUAMARINE;
      } else {
        rectColor = Color.GREEN;
      }
      origColor = rectColor;
      return (new Rectangle(dimension, dimension, rectColor));
    }

    /**
     * Returns the Text object representing the value in the square.
     * This converts the board's int to a string for use in the Text.
     */
    private Text buildValue() {
      Text value = new Text(String.valueOf(currentBoard.getValue(row, col)));
      if (editable) {
        value.setFill(Color.RED);
      } else {
        value.setFill(Color.BLACK);
      }
      return value;
    }

    /**
     * Returns the gridpane of possible pencils.
     * Pencils are displayed by showing and hiding of Text objects.
     */
    private GridPane buildPencils() {
      GridPane pencilGrid = new GridPane();
      for (int row = 1; row < 4; row++) {
        for (int col = 1; col < 4; col++) {
          Text pencilText = new Text(String.valueOf((row - 1) * 3 + col));
          pencilText.setFill(Color.DARKGREY);
          pencilGrid.setRowIndex(pencilText, row);
          pencilGrid.setColumnIndex(pencilText, col);
          pencilGrid.getChildren().add(pencilText);
        }
      }
      return pencilGrid;
    }

    /**
     * Adds a border between cells in the board. 
     * The border between regions is thicker than the border between cells
     * of the same region.
     */
    private void buildPaneBorder() {
      double topPercent = 1;
      double rightPercent = 1;
      double bottomPercent = 1;
      double leftPercent = 1;
      double thickSize = 5;

      if (row == 1 || row == 4 || row == 7) {
        topPercent = thickSize;
      }
      if (col == 1 || col == 4 || col == 7) {
        leftPercent = thickSize;
      }
      if (row == 9) {
        bottomPercent = thickSize;
      }
      if (col == 9) {
        rightPercent = thickSize;
      }
      BorderWidths bw = new BorderWidths(
          topPercent, rightPercent, bottomPercent, leftPercent,
          false, false, false, false);
      BorderStroke bs = new BorderStroke(
          Color.BLACK, BorderStrokeStyle.SOLID,
          CornerRadii.EMPTY, bw);
      pane.setBorder(new Border(bs));
    }

    /**
     * Updates the current board with the integer input.
     * Removes the number if it already exists, otherwise adds it.
     *
     * @param input number to update the board with
     */
    private void modifyBoard(int input) {
      if (editPencils) {
        if (currentBoard.getPencils(row, col).contains(input)) {
          currentBoard.removePencil(row, col, input);
        } else {
          currentBoard.addPencil(row, col, input);
        }
      } else {
        if (currentBoard.getValue(row, col) == input) {
          currentBoard.setValue(row, col, 0);
        } else {
          currentBoard.setValue(row, col, input);
        }
      }
    }

    /**
     * Sets the visibility of value and pencil Text objects based on the board.
     * If the cell has a value, the value is shown. If the cell has no value,
     * but has pencils, the pencils are shown. If it has neither, the cell is
     * empty.
     */
    private void updateVisibility() {
      if (currentBoard.getValue(row, col) != 0) {
        //set value visible and pencils invisible
        for (Node node : pane.getChildren()) {
          if (node instanceof Text) {
            node.setVisible(true);
          }
          if (node instanceof GridPane) {
            node.setVisible(false);
          }
        }
      } else {
        //set value invisible and pencils visible
        for (Node node : pane.getChildren()) {
          if (node instanceof Text) {
            node.setVisible(false);
          }
          if (node instanceof GridPane) {
            //update pencil visibility
            for (Node pencil : ((GridPane)node).getChildren()) {
              if (currentBoard.getPencils(row, col).contains(
                  Integer.parseInt(((Text)pencil).getText()))) {
                pencil.setVisible(true);
              } else {
                pencil.setVisible(false);
              }
            }
            node.setVisible(true);
          }
        }
      }
    }
  }
}
