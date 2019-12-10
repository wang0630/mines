package Level;

import Board.Board;
import Mine.MineFrame;

public abstract class Level {
    private int DIFFICULTY;
    public void runLevel() {
        Board.setDifficulty(0);
        MineFrame.setNoOfMines(20);
        MineFrame.setNoOfRows(15);
        MineFrame.setNoOfCols(15);
        MineFrame.calcDimentions();
        MineFrame.startNewGame();
    }
}
