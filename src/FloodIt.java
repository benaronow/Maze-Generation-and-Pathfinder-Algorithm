import tester.Tester;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a single Cell on the game grid
class Cell {
  int x;
  int y;
  String color;
  Cell left;
  Cell right;
  Cell top;
  Cell bottom;
  boolean flooded;
  ArrayList<String> loColors = new ArrayList<String>();
  ArrayList<String> colorChoices = new ArrayList<String>(
      Arrays.asList("red", "yellow", "green", "orange", "magenta", "cyan", "blue", "pink"));

  // Standard constructor for starting a real game
  Cell(int x, int y, int colors, boolean flooded) {
    for (int i = 0; i < colors; i++) {
      loColors.add(colorChoices.get(i));
    }
    this.x = x;
    this.y = y;
    Random random = new Random();
    int randomColor = random.nextInt(loColors.size());
    this.color = loColors.get(randomColor);
    this.flooded = flooded;
  }
  
  // Testing constructor for a test example
  Cell(int x, int y, String color, boolean flooded,
      Cell left, Cell right, Cell top, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.right = right; 
    this.top = top;
    this.bottom = bottom;
  }

  // Creates the Cell image that is displayed on the game grid for each Cell color
  WorldImage image() {
    if (color.equals("red")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED);
    }
    else if (color.equals("yellow")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW);
    }
    else if (color.equals("green")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN);
    }
    else if (color.equals("orange")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.ORANGE);
    }
    else if (color.equals("magenta")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA);
    }
    else if (color.equals("cyan")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.CYAN);
    }
    else if (color.equals("blue")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE);
    }
    else {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK);
    }
  }

  // EFFECT: Mutates the flooded status of adjacent Cells based on their color
  void adjacentFlooded(String color) {
    if (left != null && left.color.equals(color)) {
      left.flooded = true;
    }
    if (right != null && right.color.equals(color)) {
      right.flooded = true;
    }
    if (top != null && top.color.equals(color)) {
      top.flooded = true;
    }
    if (bottom != null && bottom.color.equals(color)) {
      bottom.flooded = true;
    }
  }
}

//Represents the game grid
class FloodItWorld extends World {
  ArrayList<Cell> grid;
  int size;
  int colors;
  int limit;
  int turns = 0;
  String prev = "";
  String post = "";
  // Raw time for game-end scenario display
  int time = 0;
  // Minute-based time for info display
  int displayTime = 0;
  int minutes = 0;
  // Time when flooding ends for game-end scenario display
  int turnEnd = 0;

  // Standard constructor for starting a real game
  FloodItWorld(int size, int colors) {
    this.size = size;
    this.colors = colors;
    this.limit = (int) (colors * size * 0.3);
    createCells(size);
    setAdjacent();
  }
  
  // Testing constructor for a test example
  FloodItWorld() {
    size = 3;
    colors = 3;
    limit = 3;
  }

  // EFFECT: Creates new Cells with i and j coordinates, with only the corner Cell
  // flooded to begin
  public void createCells(int size) {
    grid = new ArrayList<Cell>();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == 0 && j == 0) {
          grid.add(new Cell(0, 0, colors, true));
          post = grid.get(0).color;
        }
        else {
          grid.add(new Cell(i, j, colors, false));
        }
      }
    }
  }

  // EFFECT: Sets the adjacent cell for every cell on the grid
  public void setAdjacent() {
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      if (cell.x > 0) {
        cell.left = grid.get(i - size);
      }
      if (cell.x < size - 1) {
        cell.right = grid.get(i + size);
      }
      if (cell.y > 0) {
        cell.top = grid.get(i - 1);
      }
      if (cell.y < size - 1) {
        cell.bottom = grid.get(i + 1);
      }
    }
  }

  // Creates the display that the user sees on every tick
  public WorldScene makeScene() {
    WorldScene game = new WorldScene(600, 750);

    // Displays the background
    game.placeImageXY(new RectangleImage(600, 750, OutlineMode.SOLID, Color.GRAY), 300, 375);

    // Displays the title backdrop
    game.placeImageXY(new EllipseImage(350, 80, OutlineMode.SOLID, Color.WHITE), 300,
        (330 - 10 * size) / 2);

    // Displays the title
    game.placeImageXY(new TextImage("Flood It", 60, FontStyle.BOLD, Color.BLACK), 300,
        (330 - 10 * size) / 2);

    // Displays the border
    game.placeImageXY(
        new RectangleImage((size + 2) * 20, (size + 2) * 20, OutlineMode.SOLID, Color.WHITE), 300,
        350);
    game.placeImageXY(
        new RectangleImage((size + 1) * 20, (size + 1) * 20, OutlineMode.SOLID, Color.BLACK), 300,
        350);

    // Displays the Cells inside the border
    for (Cell cell : grid) {
      game.placeImageXY(cell.image(), (310 - 10 * size) + 20 * cell.x,
          (360 - 10 * size) + 20 * cell.y);
    }

    // Handles the time when a minute has passed
    if (displayTime == 3000) {
      minutes++;
      displayTime = 0;
    }

    // Accurately converts the time to a string
    TextImage timeDisplay = new TextImage("", 30, Color.BLACK);
    if (minutes == 1 && displayTime / 50 == 1) {
      timeDisplay = new TextImage(
          "Time: " + minutes + " minute, " + displayTime / 50 + " second", 30, Color.BLACK);
    }
    else if (minutes == 1) {
      timeDisplay = new TextImage(
          "Time: " + minutes + " minute, " + displayTime / 50 + " seconds", 30, Color.BLACK);
    }
    else if (displayTime / 50 == 1) {
      timeDisplay = new TextImage(
          "Time: " + minutes + " minutes, " + displayTime / 50 + " second", 30, Color.BLACK);
    }
    else {
      timeDisplay = new TextImage(
          "Time: " + minutes + " minutes, " + displayTime / 50 + " seconds", 30, Color.BLACK);
    }

    // Creates a panel with game information
    WorldImage info = new OverlayOffsetImage(
        new TextImage(
            "Turns: " + Integer.toString(turns) + " / " + Integer.toString(limit), 30, Color.BLACK),
        0, 35,
        new OverlayOffsetImage(timeDisplay, 0, -5,
            new OverlayOffsetImage(
                new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK), 0, -40,
                new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE))));

    // Displays the panel
    game.placeImageXY(info, 300, 750 - ((750 - (370 + 10 * size)) / 2));

    // Creates a new info panel for when the player wins
    WorldImage win = new OverlayOffsetImage(
        new TextImage("You Win!", 60, FontStyle.BOLD, Color.GREEN), 0, 15,
        new OverlayOffsetImage(
            new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK), 0, -40,
            new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));

    // Creates a new info panel for when the player loses
    WorldImage loss = new OverlayOffsetImage(
        new TextImage("Game Over", 60, FontStyle.BOLD, Color.RED), 0, 15,
        new OverlayOffsetImage(
            new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK), 0, -40,
            new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));

    // Displays a win or loss message when the turn limit is reached
    if (turns <= limit && time >= turnEnd && allFlooded()) {
      game.placeImageXY(win, 300, 750 - ((750 - (370 + 10 * size)) / 2));
    }
    if (turns >= limit && time >= turnEnd && !allFlooded()) {
      game.placeImageXY(loss, 300, 750 - ((750 - (370 + 10 * size)) / 2));
    }

    return game;
  }
  
  // Checks if all the Cells in the grid are flooded by testing if they are all the same color,
  // given that not all Cells are flooded even if the win condition is met given the nature of
  // floodGrid and the waterfall effect
  public boolean allFlooded() {
    boolean result = true;
    for (Cell cell : grid) {
      result = result && cell.color == post;
    }
    if (result) {
      for (Cell cell : grid) {
        cell.flooded = true;
      }
      return result;
    }
    return result;
  }

  // EFFECT: Restarts a the game upon pressing the "r" key
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      createCells(size);
      setAdjacent();
      turns = 0;
      time = 0;
      displayTime = 0;
      minutes = 0;
    }
  }

  // EFFECT: Updates the game grid when the mouse is clicked
  public void onMouseClicked(Posn pos) {
    boolean anyFlooded = false;
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      anyFlooded = anyFlooded || cell.flooded;
    }
    // Checks to make sure the click was in bounds, the turns taken are below the turn limit,
    // the clicked color isn't the same as the current corner, and that the waterfall animation
    // is not currently occurring
    if (((pos.x < (300 - 10 * size) || pos.x > (300 - 10 * size) + 20 * size)
        || (pos.y < (350 - 10 * size) || pos.y > (350 - 10 * size) + 20 * size))
        || turns >= limit || (clickedCell(pos)).color == post || anyFlooded) {
      return;
    }
    Cell corner = grid.get(0);
    prev = corner.color;
    post = (clickedCell(pos)).color;
    corner.color = post;
    corner.flooded = true;
    turns++;
    turnEnd = time + (size * 2);
  }

  // Determines which Cell was clicked on by the mouse
  public Cell clickedCell(Posn pos) {
    Cell clicked = null;
    for (Cell cell : grid) {
      if ((cell.x <= (pos.x - (300 - 10 * size)) / 20 && (pos.x - (300 - 10 * size)) / 20 <= cell.x)
          && (cell.y <= (pos.y - (350 - 10 * size)) / 20
              && (pos.y - (350 - 10 * size)) / 20 <= cell.y)) {
        clicked = cell;
      }
    }
    return clicked;
  }
  
  // EFFECT: Updates the game every tick
  public void onTick() {
    // Updates the grid display in order to continue flooding if it is occurring
    floodGrid();
    time++;
    displayTime++;
  }
  
  // EFFECT: Floods the grid starting from the top left corner Cell, achieving the waterfall
  // effect by flooding in so called "waves"
  public void floodGrid() {
    // Floods the Cells adjacent to those that are already flooded
    // while unflooding those that currently are, effectively flooding the next "wave" while
    // unflooding the current one
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      if (cell.flooded && cell.color == post) {
        cell.adjacentFlooded(prev);
        cell.flooded = false;
      }
    }
    // Changes the color of the current flooded Cells, or the new "wave"
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      if (cell.flooded && cell.color == prev) {
        cell.color = post;
      }
    }
    // Updates the display
    makeScene();
  }

  // EFFECT: Initializes the game and restricts unmanageable inputs with exceptions
  public void initGame(int size, int colors) {
    if (size < 2) {
      throw new IllegalArgumentException("Grid dimensions must be at least 2x2");
    }
    else if (size > 22) {
      throw new IllegalArgumentException("Grid dimensions may not exceed 22x22");
    }
    else if (colors < 3) {
      throw new IllegalArgumentException("Grid must contain at least 3 colors");
    }
    else if (colors > 8) {
      throw new IllegalArgumentException("Grid may not exceed 8 colors");
    }
    FloodItWorld game = new FloodItWorld(size, colors);
    game.bigBang(600, 750, 0.02);
  }
}

// Represents examples of a Flood It game
class ExamplesFloodIt {

  // Creates examples of Cells
  Cell red;
  Cell yellow;
  Cell green;
  Cell orange;
  Cell magenta;
  Cell cyan;
  Cell blue;
  Cell pink;
  Cell red2;
  
  // Creates examples of game inputs
  int exampleColors;
  ArrayList<String> exampleLoColors;
  ArrayList<String> exampleColorChoices;
  int exampleSize;
  FloodItWorld exampleFloodIt;

  // Initializes an examples Flood It game for testing
  void init() {
    
    // Creates a 3x3 grid of customized Cells and updates them when applicable
    red = new Cell(0, 0, "red", true,
        null, null, null, null);
    yellow = new Cell(1, 0, "yellow", false,
        red, null, null, null);
    red.right = yellow;
    green = new Cell(0, 1, "green", false,
        null, null, red, null);
    red.bottom = green;
    orange = new Cell(1, 1, "orange", false,
        green, null, yellow, null);
    yellow.bottom = orange;
    green.right = orange;
    magenta = new Cell(2, 0, "magenta", false, 
        yellow, null, null, null);
    yellow.right = magenta;
    cyan = new Cell(0, 2, "cyan", false,
        null, null, green, null);
    green.bottom = cyan;
    blue = new Cell(2, 1, "blue", false,
        orange, null, magenta, null);
    orange.right = blue;
    magenta.bottom = blue;
    pink = new Cell(1, 2, "pink", false,
        cyan, null, orange, null);
    orange.bottom = pink;
    cyan.right = pink;
    red2 = new Cell(2, 2, "red", true,
        pink, null, blue, null);
    blue.bottom = red2;
    pink.right = red2;
    
    // Creates an example list of colors with a given colors input
    exampleColors = 3;
    exampleLoColors = new ArrayList<String>();
    exampleColorChoices = new ArrayList<String>(
        Arrays.asList("red", "yellow", "green", "orange", "magenta", "cyan", "blue", "pink"));
    for (int i = 0; i < exampleColors; i++) {
      exampleLoColors.add(exampleColorChoices.get(i));
    }

    // Creates an Example Flood It game with a given size input
    exampleSize = 3;
    exampleFloodIt = new FloodItWorld();
    exampleFloodIt.createCells(exampleSize);
    exampleFloodIt.setAdjacent();
  }
  
  // EFFECT: Tests the image method
  void testImage(Tester t) {
    init();
    // Checks that each Cell is converted to the correct display image
    t.checkExpect(red.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED));
    t.checkExpect(yellow.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW));
    t.checkExpect(green.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN));
    t.checkExpect(orange.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.ORANGE));
    t.checkExpect(magenta.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA));
    t.checkExpect(cyan.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.CYAN));
    t.checkExpect(blue.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE));
    t.checkExpect(pink.image(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK));
  }

  // EFFECT: Tests the adjacentFlooded method
  void testAdjacentFlooded(Tester t) {
    init();
    // Checks to see that the correct Cells are flooded after adjacentFlooded is run
    t.checkExpect(red.right.flooded, false);
    t.checkExpect(red.bottom.flooded, false);
    red.adjacentFlooded("yellow");
    t.checkExpect(magenta.left.flooded, true);
    t.checkExpect(magenta.bottom.flooded, false);
    magenta.adjacentFlooded("blue");
    t.checkExpect(red2.left.flooded, false);
    t.checkExpect(red2.top.flooded, true);
    red2.adjacentFlooded("pink");
    t.checkExpect(cyan.right.flooded, true);
    t.checkExpect(cyan.top.flooded, false);
    cyan.adjacentFlooded("green");
    // At this point, all cells surrounding the center should be flooded
    t.checkExpect(orange.left.flooded, true);
    t.checkExpect(orange.right.flooded, true);
    t.checkExpect(orange.top.flooded, true);
    t.checkExpect(orange.bottom.flooded, true);
  }

  // EFFECT: Tests createCells method
  void testCreateCells(Tester t) {
    init();
    // Checks that the correct Cells are flooded and that each is in an applicable spot
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, true);
    for (int i = 0; i < exampleFloodIt.grid.size(); i++) {
      Cell cell = exampleFloodIt.grid.get(i);
      t.checkRange(cell.x, 0, exampleSize);
      t.checkRange(cell.y, 0, exampleSize);
      t.checkExpect(exampleLoColors.contains(cell.color), true);
    }
    for (int i = 1; i < exampleSize; i++) {
      t.checkExpect(exampleFloodIt.grid.get(i).flooded, false);
    }
  }
  
  // EFFECT: Tests the setAdjacent method
  void testSetAdjacent(Tester t) {
    init();
    // Checks that each Cell has its adjacent Cells set correctly
    for (int i = 0; i < exampleFloodIt.grid.size(); i++) {
      Cell cell = exampleFloodIt.grid.get(i);
      if (cell.x > 0) {
        t.checkExpect(cell.left, exampleFloodIt.grid.get(i - exampleSize));
      }
      if (cell.x < exampleSize - 1) {
        t.checkExpect(cell.right, exampleFloodIt.grid.get(i + exampleSize));
      }
      if (cell.y > 0) {
        t.checkExpect(cell.top, exampleFloodIt.grid.get(i - 1));
      }
      if (cell.y < exampleSize - 1) {
        t.checkExpect(cell.bottom, exampleFloodIt.grid.get(i + 1));
      }
    }
  }
  
  // EFFECT: Tests the makeScene Method
  void testMakeScene(Tester t) {
    init();
    // Manually recreates each aspect of makeScene with the given inputs
    // and confirms that it mirrors the automated makeScene method
    WorldScene game = new WorldScene(600, 750);
    game.placeImageXY(new RectangleImage(600, 750, OutlineMode.SOLID, Color.GRAY), 300, 375);
    game.placeImageXY(new EllipseImage(350, 80, OutlineMode.SOLID, Color.WHITE), 300,
        (330 - 10 * exampleSize) / 2);
    game.placeImageXY(new TextImage("Flood It", 60, FontStyle.BOLD, Color.BLACK), 300,
        (330 - 10 * exampleSize) / 2);
    game.placeImageXY(
        new RectangleImage((exampleSize + 2) * 20, (exampleSize + 2) * 20, OutlineMode.SOLID,
            Color.WHITE), 300, 350);
    game.placeImageXY(
        new RectangleImage((exampleSize + 1) * 20, (exampleSize + 1) * 20, OutlineMode.SOLID,
            Color.BLACK), 300, 350);
    for (Cell cell : exampleFloodIt.grid) {
      game.placeImageXY(cell.image(), (310 - 10 * exampleSize) + 20 * cell.x,
          (360 - 10 * exampleSize) + 20 * cell.y);
    }
    if (exampleFloodIt.displayTime == 3000) {
      exampleFloodIt.minutes++;
      exampleFloodIt.displayTime = 0;
    }
    TextImage timeDisplay = new TextImage("", 30, Color.BLACK);
    if (exampleFloodIt.minutes == 1 && exampleFloodIt.displayTime / 50 == 1) {
      timeDisplay = new TextImage(
          "Time: " + exampleFloodIt.minutes + " minute, " + exampleFloodIt.displayTime / 50
          + " second", 30, Color.BLACK);
    }
    else if (exampleFloodIt.minutes == 1) {
      timeDisplay = new TextImage(
          "Time: " + exampleFloodIt.minutes + " minute, " + exampleFloodIt.displayTime / 50
          + " seconds", 30, Color.BLACK);
    }
    else if (exampleFloodIt.displayTime / 50 == 1) {
      timeDisplay = new TextImage(
          "Time: " + exampleFloodIt.minutes + " minutes, " + exampleFloodIt.displayTime / 50
          + " second", 30, Color.BLACK);
    }
    else {
      timeDisplay = new TextImage(
          "Time: " + exampleFloodIt.minutes + " minutes, " + exampleFloodIt.displayTime / 50
          + " seconds", 30, Color.BLACK);
    }
    WorldImage info = new OverlayOffsetImage(
        new TextImage(
            "Turns: " + Integer.toString(exampleFloodIt.turns) + " / "
        + Integer.toString(exampleFloodIt.limit), 30, Color.BLACK), 0, 35,
        new OverlayOffsetImage(timeDisplay, 0, -5,
            new OverlayOffsetImage(
                new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK),
                0, -40, new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE))));
    game.placeImageXY(info, 300, 750 - ((750 - (370 + 10 * exampleSize)) / 2));
    WorldImage win = new OverlayOffsetImage(
        new TextImage("You Win!", 60, FontStyle.BOLD, Color.GREEN), 0, 15,
        new OverlayOffsetImage(
            new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK),
            0, -40, new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));
    WorldImage loss = new OverlayOffsetImage(
        new TextImage("Game Over", 60, FontStyle.BOLD, Color.RED), 0, 15,
        new OverlayOffsetImage(
            new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK),
            0, -40, new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));
    if (exampleFloodIt.turns <= exampleFloodIt.limit &&
        exampleFloodIt.time >= exampleFloodIt.turnEnd && exampleFloodIt.allFlooded()) {
      game.placeImageXY(win, 300, 750 - ((750 - (370 + 10 * exampleSize)) / 2));
    }
    if (exampleFloodIt.turns >= exampleFloodIt.limit &&
        exampleFloodIt.time >= exampleFloodIt.turnEnd && !exampleFloodIt.allFlooded()) {
      game.placeImageXY(loss, 300, 750 - ((750 - (370 + 10 * exampleSize)) / 2));
    }
    t.checkExpect(exampleFloodIt.makeScene(), game);
  }
  
  // EFFECT: Tests the allFlooded method
  void testAllFlooded(Tester t) {
    init();
    // Checks whether or not all Cells are the same color
    // Does not check binary flooded status because when floodGrid is inactive,
    // all Cells except the top left corner are registered as not flooded
    t.checkExpect(exampleFloodIt.allFlooded(), false);
    for (Cell cell : exampleFloodIt.grid) {
      cell.color = exampleFloodIt.post;
    }
    t.checkExpect(exampleFloodIt.allFlooded(), true);
  }
  
  // EFFECT: Tests the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    init();
    // Creates a new alternate Flood It game with its own grid
    FloodItWorld testResetFloodIt = new FloodItWorld();
    ArrayList<Cell> testResetGrid = new ArrayList<Cell>();
    testResetFloodIt.grid = testResetGrid;
    // Checks that the intended reset key correctly creates a new game with the default
    // Flood It game previously initialized
    t.checkExpect(testResetFloodIt.size == 3, true);
    t.checkExpect(testResetFloodIt.colors == 3, true);
    t.checkExpect(testResetFloodIt.grid == testResetGrid, true);
    testResetFloodIt.onKeyEvent("a");
    t.checkExpect(testResetFloodIt.size == 3, true);
    t.checkExpect(testResetFloodIt.colors == 3, true);
    t.checkExpect(testResetFloodIt.grid == testResetGrid, true);
    testResetFloodIt.onKeyEvent("r");
    t.checkExpect(testResetFloodIt.size == 3, true);
    t.checkExpect(testResetFloodIt.colors == 3, true);
    t.checkExpect(testResetFloodIt.grid == testResetGrid, false);
  }
  
  // EFFECT: Tests the onMouseClicked method
  void testOnMouseClicked(Tester t) {
    init();
    // Manually alters conditions in a favorable manner for testing and then
    // tests these conditions
    exampleFloodIt.grid.get(0).color = "red";
    exampleFloodIt.post = "red";
    exampleFloodIt.grid.get(1).color = "yellow";
    exampleFloodIt.prev = "yellow";
    exampleFloodIt.grid.get(4).color = "green";
    exampleFloodIt.grid.get(0).flooded = false;
    exampleFloodIt.turns = 0;
    exampleFloodIt.time = 0;
    exampleFloodIt.turnEnd = 0;
    Posn grid4Posn = new Posn(300, 350);
    t.checkExpect(exampleFloodIt.post, "red");
    t.checkExpect(exampleFloodIt.prev, "yellow");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.turns, 0);
    t.checkExpect(exampleFloodIt.turnEnd, 0);
    exampleFloodIt.grid.get(2).flooded = true;
    // Checks that the method doesn't work when a non-top-left-corner Cell is flooded
    // to replicate a null click when the waterfall animation is active
    exampleFloodIt.onMouseClicked(grid4Posn);
    t.checkExpect(exampleFloodIt.post, "red");
    t.checkExpect(exampleFloodIt.prev, "yellow");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.turns, 0);
    t.checkExpect(exampleFloodIt.turnEnd, 0);
    exampleFloodIt.grid.get(2).flooded = false;
    exampleFloodIt.grid.get(4).color = "red";
    // Checks that the method doesn't work when the clicked upon Cell is the same color
    // as the Cell currently in the top left corner
    exampleFloodIt.onMouseClicked(grid4Posn);
    t.checkExpect(exampleFloodIt.post, "red");
    t.checkExpect(exampleFloodIt.prev, "yellow");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.turns, 0);
    t.checkExpect(exampleFloodIt.turnEnd, 0);
    exampleFloodIt.grid.get(4).color = "green";
    // Checks that the method does work when the required conditions are met
    exampleFloodIt.onMouseClicked(grid4Posn);
    t.checkExpect(exampleFloodIt.post, exampleFloodIt.clickedCell(grid4Posn).color);
    t.checkExpect(exampleFloodIt.prev, "red");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, true);
    t.checkExpect(exampleFloodIt.turns, 1);
    t.checkExpect(exampleFloodIt.turnEnd, exampleSize * 2);
  }
  
  // EFFECT: Tests the clickedCell method
  void testClickedCell(Tester t) {
    init();
    // Checks that the clicked position corresponds to the correct Cell or lack thereof
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(300, 350)), exampleFloodIt.grid.get(4));
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(320, 350)), exampleFloodIt.grid.get(7));
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(300, 370)), exampleFloodIt.grid.get(5));
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(300, 390)), null);
  }

  // EFFECT: Tests the floodGrid method
  void testfloodGrid(Tester t) {
    init();
    // Manually alters conditions in a favorable manner for testing and then
    // tests these conditions
    exampleFloodIt.grid.get(0).color = "red";
    exampleFloodIt.post = "red";
    exampleFloodIt.grid.get(1).color = "yellow";
    exampleFloodIt.prev = "yellow";
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, true);
    t.checkExpect(exampleFloodIt.grid.get(1).flooded, false);
    t.checkExpect(exampleFloodIt.grid.get(0).color, "red");
    t.checkExpect(exampleFloodIt.grid.get(1).color, "yellow");
    // Checks that the correct flooded Cell changes color upon running the method
    exampleFloodIt.floodGrid();
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.grid.get(1).flooded, true);
    t.checkExpect(exampleFloodIt.grid.get(0).color, "red");
    t.checkExpect(exampleFloodIt.grid.get(1).color, "red");
  }
  
  // EFFECT: Tests the initGame method
  void testInitGame(Tester t) {
    FloodItWorld game = new FloodItWorld(22, 6);
    // Runs a game using the given inputs [size, colors] within fixed bounds
    game.initGame(22, 6);
    // Checks to make sure illegal arguments don't pass through
    t.checkException(
        new IllegalArgumentException("Grid dimensions must be at least 2x2"),
        game, "initGame", 1, 6);
    t.checkException(
        new IllegalArgumentException("Grid dimensions may not exceed 22x22"),
        game, "initGame", 23, 6);
    t.checkException(
        new IllegalArgumentException("Grid must contain at least 3 colors"),
        game, "initGame", 18, 2);
    t.checkException(
        new IllegalArgumentException("Grid may not exceed 8 colors"),
        game, "initGame", 18, 9);
    // Tests that the initial inputs are correctly interpreted
    t.checkExpect(game.size, 22);
    t.checkExpect(game.colors, 6);
    t.checkExpect(game.limit, (int) (22 * 6 * 0.3));
    t.checkExpect(game.turns, 0);
    t.checkExpect(game.prev, "");
    t.checkExpect(game.post, game.grid.get(0).color);
    t.checkExpect(game.time, 0);
    t.checkExpect(game.displayTime, 0);
    t.checkExpect(game.minutes, 0);
    t.checkExpect(game.turnEnd, 0);
  }
}