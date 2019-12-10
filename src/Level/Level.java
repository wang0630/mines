package Level;

import Board.Board;
import Mine.MineFrame;

public abstract class Level {
    private int DIFFICULTY;
    private int NUM_OF_ROWS;
    private int NUM_OF_COLS;
    private int NUM_OF_MINES;
    // super() is called in class Beginner, Intermediate and Expert
    public Level(int d, int r, int c, int m) {
        DIFFICULTY = d;
        NUM_OF_ROWS = r;
        NUM_OF_COLS = c;
        NUM_OF_MINES = m;
    }
    public void runLevel() {
        Board.setDifficulty(DIFFICULTY);
        MineFrame.setNoOfMines(NUM_OF_MINES);
        MineFrame.setNoOfRows(NUM_OF_ROWS);
        MineFrame.setNoOfCols(NUM_OF_COLS);
        MineFrame.calcDimentions();
        MineFrame.startNewGame();
    }
}
