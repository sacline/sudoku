import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * GUI for Sudoku puzzle game.
 */
public class SudokuGame extends Application {

  private final double WINDOW_WIDTH = 800;
  private final double WINDOW_HEIGHT = 600;
  private final String PUZZLE_FILENAME = "100gamepuzzles.txt";

  private boolean editpencils;
  private Cell selectedcell;

  private Stage stage;
  private Scene gamescene;

  private SudokuBoard currentboard;
  private SudokuBoard currentsolution;
  private SudokuSolver solver;

  private ArrayList<SudokuBoard> easypuzzles = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> easysolutions = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> mediumpuzzles = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> mediumsolutions = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> hardpuzzles = new ArrayList<SudokuBoard>();
  private ArrayList<SudokuBoard> hardsolutions = new ArrayList<SudokuBoard>();

/**
 * Method that launches when the game runs.
 */
  @Override
  public void start(Stage primarystage) throws IOException {
    stage = primarystage;
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
 * @param filename the input file
 */
  private void loadPuzzles(String filename) throws IOException {
    BufferedReader br = new BufferedReader((new FileReader(filename)));
    String line;
    while ((line = br.readLine()) != null) {
      String diff = line.substring(164,165);
      String puzzlestring = line.substring(0,81);
      String solutionstring = line.substring(82,163);
      if (diff.equals("e")) {
        easypuzzles.add(new SudokuBoard(puzzlestring));
        easysolutions.add(new SudokuBoard(solutionstring));
      }
      if (diff.equals("m")) {
        mediumpuzzles.add(new SudokuBoard(puzzlestring));
        mediumsolutions.add(new SudokuBoard(solutionstring));
      }
      if (diff.equals("h")) {
        hardpuzzles.add(new SudokuBoard(puzzlestring));
        hardsolutions.add(new SudokuBoard(solutionstring));
      }
    }
    br.close();
  }

/**
 * Returns the menu scene that is generated upon launching the game.
 */
  private Scene buildMenuScene() {
    BorderPane root = new BorderPane();

    root.setTop(buildGameTitle());
    root.setAlignment(root.getTop(), Pos.CENTER);
    root.setMargin(root.getTop(), new Insets(5));

    root.setBottom(buildMenu());
    root.setAlignment(root.getBottom(), Pos.TOP_CENTER);
    root.setMargin(root.getBottom(), new Insets(0, 0, 400, 0));

    Scene menuscene;
    menuscene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    return menuscene;
  }

/**
 * Returns the game title Text object.
 */
  private Text buildGameTitle() {
    Text menutitle = new Text("Sudoku");
    menutitle.setFill(Color.RED);
    menutitle.setFont(new Font(50));
    return menutitle;
  }

/**
 * Returns the grid pane containing the New Game title and buttons.
 */
  private GridPane buildMenu() {
    GridPane middle = new GridPane();

    Text newgametitle = new Text("New Game");
    newgametitle.setFill(Color.BLUE);
    newgametitle.setFont(new Font(30));
    middle.add(newgametitle, 1, 0);
    middle.setHalignment(newgametitle, HPos.CENTER);
    middle.setPadding(new Insets(50, 0, 0, 0));

    Button neweasy = new Button("Easy");
    neweasy.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildGameScene("easy"));
        stage.show();
      }
    });
    middle.add(neweasy, 0, 1);
    middle.setHalignment(neweasy, HPos.RIGHT);

    Button newmedium = new Button("Medium");
    newmedium.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildGameScene("medium"));
      }
    });
    middle.add(newmedium, 1, 1);
    middle.setHalignment(newmedium, HPos.CENTER);

    Button newhard = new Button("Hard");
    newhard.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildGameScene("hard"));
      }
    });
    middle.add(newhard, 2, 1);
    middle.setHalignment(newhard, HPos.LEFT);

    Button exitgame = new Button("Exit Game");
    exitgame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
          Platform.exit();
      }
    });
    middle.add(exitgame, 1, 3);
    middle.setHalignment(exitgame, HPos.CENTER);
    middle.setVgap(30);

    ColumnConstraints column0 = new ColumnConstraints();
    column0.setPercentWidth(50);
    middle.getColumnConstraints().addAll(column0, column0, column0);

    return middle;
  }

/**
 * Returns a new game scene.
 *
 * @param difficulty the desired game difficulty
 */
  private Scene buildGameScene(String difficulty) {
    solver = new SudokuSolver();
    BorderPane bp = new BorderPane();
    Scene gamescene = new Scene(bp, WINDOW_WIDTH, WINDOW_HEIGHT);

    setCurrentBoard(difficulty);

    bp.setCenter(buildGameBoard());
    bp.setAlignment(bp.getCenter(), Pos.CENTER);
    bp.setBottom(buildGameBottom());
    bp.setAlignment(bp.getBottom(), Pos.TOP_CENTER);
    bp.setRight(buildGameRight());
    bp.setAlignment(bp.getRight(), Pos.CENTER);

    editpencils = false;

    gamescene.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent e) {
          if (selectedcell != null) {
            int value = selectedcell.keyToInt(e);
            if (value != -1) {
              selectedcell.updateCell(value);
              selectedcell = null;
            }
          }
      }
    });
    return gamescene;
  }

/**
 * Selects a random board from the input to use in the game.
 *
 * @param difficulty difficulty of board to select
 */
  private void setCurrentBoard(String difficulty) {
    Random random = new Random();
    if (difficulty == "easy") {
      int index = random.nextInt(easypuzzles.size());
      currentboard = easypuzzles.get(index);
      currentsolution = easysolutions.get(index);
    }
    if (difficulty == "medium") {
      int index = random.nextInt(mediumpuzzles.size());
      currentboard = mediumpuzzles.get(index);
      currentsolution = mediumsolutions.get(index);
    }
    if (difficulty == "hard") {
      int index = random.nextInt(hardpuzzles.size());
      currentboard = hardpuzzles.get(index);
      currentsolution = hardsolutions.get(index);
    }
  }

/**
 * Returns a gridpane representing the sudoku board.
 * Each sudoku board is a grid of Cell objects
 */
  private GridPane buildGameBoard() {
    GridPane gameboard = new GridPane();
    for (int row = 1; row < 10; row++) {
      for (int col = 1; col < 10; col++) {
        gameboard.add(((new Cell(row, col)).getPane()), col - 1, row - 1);
      }
    }
    gameboard.setPadding(new Insets(0, 0, 0, 157));
    return gameboard;
  }

/**
 * Returns the bottom HBox containing the quit button.
 */
  private HBox buildGameBottom() {
    HBox bottombox = new HBox();
    bottombox.setAlignment(Pos.CENTER);

    Button menubutton = new Button("Return to main menu");
    menubutton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.setScene(buildMenuScene());
      }
    });
    bottombox.getChildren().addAll(menubutton);
    bottombox.setPadding(new Insets(0, 0, 50, 0));
    return bottombox;
  }

/**
 * Returns VBox on right side of game scene containing control buttons.
 */
  private VBox buildGameRight() {
    VBox rightbox = new VBox();
    rightbox.setAlignment(Pos.CENTER);
    rightbox.setPadding(new Insets(0, 25, 0, 0));
    rightbox.setSpacing(10);

    Text pencilindicator = new Text("Pencils = On");
    pencilindicator.setFill(Color.BLUE);
    pencilindicator.setVisible(false);

    Button togglepencils = new Button("Toggle Pencils");
    togglepencils.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        editpencils = !editpencils;
        pencilindicator.setVisible(editpencils);
      }
    });

    Text puzzlesolved = new Text();
    puzzlesolved.setFill(Color.GREEN);
    puzzlesolved.setVisible(false);

    Button checkpuzzle = new Button("Check puzzle");
    checkpuzzle.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (solver.isSolved(currentboard)) {
          puzzlesolved.setText("Puzzle is solved!");
          puzzlesolved.setVisible(true);
        }
        else {
          puzzlesolved.setText("Puzzle not solved!");
          puzzlesolved.setVisible(true);
        }
      }
    });

    rightbox.getChildren().addAll(
        pencilindicator, togglepencils, puzzlesolved, checkpuzzle);
    return rightbox;
  }

/**
 * Each cell represents a square of the sudoku board.
 */
  private class Cell {
    Integer row;
    Integer col;
    StackPane pane;
    Color origcolor;
    boolean editable;

  /**
   * Cell constructor.
   */
    private Cell(int row, int col) {
      this.row = new Integer(row);
      this.col = new Integer(col);
      pane = new StackPane();
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
   * Makes the cell editable if the value is not defined in the initial puzzle.
   */
    private void setEditable() {
      if (currentboard.getValue(row, col) != 0) {
        editable = false;
      }
      else {
        editable = true;
      }
    }

  /**
   * Defines behavior for a cell when it is clicked.
   */
    private void setPaneAction() {
      if (editable) {
        Cell thiscell = this;
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (selectedcell == null) {
              ((Rectangle)pane.getChildren().get(0)).setFill(Color.YELLOW);
              selectedcell = thiscell;
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
      int value = -1;
      try {
        value = Integer.parseInt(character);
      }
      catch(java.lang.NumberFormatException e) {
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
      ((Rectangle)pane.getChildren().get(0)).setFill(origcolor);
    }

  /**
   * Returns the Rectangle shape for the cell.
   * Cells in even-numbered regions are different colors than those in
   * odd-numbered regions
   */
    private Rectangle buildRect() {
      Color rectcolor;
      final int DIMENSION = 50;
      if (SudokuBoard.findReg(row, col) % 2 == 0) {
        rectcolor = Color.AQUAMARINE;
      }
      else {
        rectcolor = Color.GREEN;
      }
      origcolor = rectcolor;
      return(new Rectangle(DIMENSION, DIMENSION, rectcolor));
    }

  /**
   * Returns the Text object representing the value in the square.
   */
    private Text buildValue() {
      Text value = new Text(String.valueOf(currentboard.getValue(row, col)));
      if (editable) {
        value.setFill(Color.RED);
      }
      else {
        value.setFill(Color.BLACK);
      }
      return value;
    }

  /**
   * Returns the gridpane of possible pencils.
   * Pencils are displayed by showing and hiding of Text objects.
   */
    private GridPane buildPencils() {
      GridPane pencilgrid = new GridPane();
      for (int row = 1; row < 4; row++) {
        for (int col = 1; col < 4; col++) {
          Text penciltext = new Text(String.valueOf((row - 1) * 3 + col));
          penciltext.setFill(Color.DARKGREY);
          pencilgrid.setRowIndex(penciltext, row);
          pencilgrid.setColumnIndex(penciltext, col);
          pencilgrid.getChildren().add(penciltext);
        }
      }
      return pencilgrid;
    }

  /**
   * Adds a border between cells in the board, with a thicker border between
   * regions.
   */
    private void buildPaneBorder() {
      BorderWidths bw;
      double toppercent = 1;
      double rightpercent = 1;
      double bottompercent = 1;
      double leftpercent = 1;
      double thicksize = 5;

      if (row == 1 || row == 4 || row == 7) {
        toppercent = thicksize;
      }
      if (col == 1 || col == 4 || col == 7) {
        leftpercent = thicksize;
      }
      if (row == 9) {
        bottompercent = thicksize;
      }
      if (col == 9) {
        rightpercent = thicksize;
      }
      bw = new BorderWidths(
          toppercent, rightpercent, bottompercent, leftpercent,
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
      if (editpencils) {
        if (currentboard.getPencils(row, col).contains(input)) {
          currentboard.removePencil(row, col, input);
        }
        else {
          currentboard.addPencil(row, col, input);
        }
      }
      else {
        if (currentboard.getValue(row, col) == input) {
          currentboard.setValue(row, col, 0);
        }
        else {
          currentboard.setValue(row, col, input);
        }
      }
    }

  /**
   * Sets the visibility of value and pencil Text objects based on the board.
   */
    private void updateVisibility() {
      if (currentboard.getValue(row, col) != 0) {
        //set value visible and pencils invisible
        for (Node node : pane.getChildren()) {
          if (node instanceof Text) {
            node.setVisible(true);
          }
          if (node instanceof GridPane) {
            node.setVisible(false);
          }
        }
      }
      else {
        //set value invisible and pencils visible
        for (Node node : pane.getChildren()) {
          if (node instanceof Text) {
            node.setVisible(false);
          }
          if (node instanceof GridPane) {
            //update pencil visibility
            for (Node pencil : ((GridPane)node).getChildren()) {
              if (currentboard.getPencils(row, col).contains(
                  Integer.parseInt(((Text)pencil).getText()))) {
                pencil.setVisible(true);
              }
              else {
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
