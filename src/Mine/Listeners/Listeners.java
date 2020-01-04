package Mine.Listeners;

import Board.Board;
import Mine.MineFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

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

    public static class RedoListener implements ActionListener
    {

        //Method that pushes the current redo to the undoStack and pops the redoStack to the (mine-) field
        public void actionPerformed(ActionEvent e)
        {
            if (!MineFrame.redoStack.empty())//Check if the undoStack is empty
            {
                MineFrame.undoStack.push(MineFrame.redoStack.peek());//Return the item to the undo stack
                Board.setField(MineFrame.redoStack.pop());//Make the field equal to the item and remove it from the stack
                MineFrame.gamePanel.repaint();//Repaint the frame
            }
        }
    }

    public static class UndoListener implements ActionListener
    {

        //Method that pushes the current undo to the redoStack and pops the undoStack to the (mine-) field
        public void actionPerformed(ActionEvent e)
        {
            if (!MineFrame.undoStack.empty())//Check if the undoStack is empty
            {
                MineFrame.redoStack.push(MineFrame.undoStack.pop());//Push the first element of undoStack to redoStack and remove current field
                if (!MineFrame.undoStack.empty())
                {
                    Board.setField(MineFrame.undoStack.pop());//Make the board equal to the next element in undoStack
                    Board.pushFieldToUndoStack();//Push the new current frame into stack
                    MineFrame.gamePanel.repaint();//Repaint the frame
                }
                else if(!MineFrame.redoStack.empty())
                {
                    MineFrame.undoStack.push(MineFrame.redoStack.pop());//Return the item to the undo stack
                }
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

    public static class LoadListener implements ActionListener
    {
        //Initialise fileChooser
        private JFileChooser fileChooser = new JFileChooser();


        //Open a FileChooser, read the selected file into an array, override the (mine-) field with the array and repaint
        public void actionPerformed(ActionEvent e)
        {
            //Initialise new Panel for the File chooser
            class FileChooserPanel extends JPanel
            {
                public FileChooserPanel()
                {
                }
            }
            FileChooserPanel fileChooserPanel = new FileChooserPanel();//Create a new FileChooserPanel
            int returnVal = fileChooser.showOpenDialog(fileChooserPanel);//Handle open button action

            if (returnVal == JFileChooser.APPROVE_OPTION)//Run the following code if the user opens a file
            {
                File file = fileChooser.getSelectedFile();//Set the file to the one selected by the user

                try
                {
                    Scanner scan = new Scanner(file);

                    int n = 0;//Initialise n
                    while (scan.hasNext())
                    {
                        n += 1;//Get the length of the array/file
                        scan.next();
                    }
                    scan.close();//Close scanner

                    scan = new Scanner(file);//Reopen Scanner

                    int[] arr = new int[n];//Initialise array
                    try
                    {
                        //Fill the array with the data from the file
                        for (int i = 0; i < arr.length; i++)
                        {
                            arr[i] = scan.nextInt();
                        }
                    }
                    catch (InputMismatchException ex)//Exception handling
                    {
                        JOptionPane.showMessageDialog(null, "This file is not supported!");//Give user notification of exception
                    }
                    scan.close();//Close Scanner
                    scan = null;//Garbage collection
                    Board.setField(arr);//Set arr to field
                    MineFrame.frame.repaint();//Repaint
                }
                catch (FileNotFoundException ex)//Handle file not found exception
                {
                    ex.printStackTrace();
                }

            }
        }
    }

    //Method to handle pausing the game
    public static class PauseListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            MineFrame.timePause();
            MineFrame.playingGame = !MineFrame.pauseItem.isSelected();
        }
    }

    public static class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Quit the program
            System.exit(0);
        }
    }
}
