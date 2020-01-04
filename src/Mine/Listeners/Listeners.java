package Mine.Listeners;

import Board.Board;
import Mine.MineFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Listeners {

    public static class DifficultyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MineFrame.difficultyMap.get(e.getActionCommand()).runLevel();
        }
    }

    public static class NewGameListener implements ActionListener
    {
        //Create a newGame after user agrees
        public void actionPerformed(ActionEvent e)
        {
            int ask = JOptionPane.showConfirmDialog(null, "Are you sure?");
            if (ask == 0)
            {
                MineFrame.startNewGame();
            }
        }
    }

    public static class ResolveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent arg0)
        {
            for (int cCol = 0; cCol < MineFrame.getNoOfCols(); cCol++)
            {
                for (int cRow = 0; cRow < MineFrame.getNoOfRows(); cRow++)
                {
                    //Checks that the square hasn't already been uncovered by the user
                    if (Board.getField()[(cRow * MineFrame.getNoOfCols()) + cCol] >= 10 && Board.getField()[(cRow * MineFrame.getNoOfCols()) + cCol] != 20)
                    {
                        Board.getField()[(cRow * MineFrame.getNoOfCols()) + cCol] -= Board.COVER_FOR_CELL;//Remove the covers for all cells

                        if (Board.getField()[(cRow * MineFrame.getNoOfCols()) + cCol] == 9)//Check if a cell is a mine
                        {
                            Board.getField()[(cRow * MineFrame.getNoOfCols()) + cCol] += 11;//Turn mine cells into a marked mine cell
                        }
                    }
                }
            }
            Board.setSolved(true);
            Board.setInGame(false);
            MineFrame.frame.repaint(); // Repaint the frame to show the resolved board
        }
    }
}
