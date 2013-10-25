package mines;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class SaveListener implements ActionListener
{
    private PrintWriter printer = null;//Create a PrintWriter but set to null for assigning later
    private JFileChooser fileChooser = new JFileChooser();

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // create new Panel
        class FileChooserPanel extends JPanel
        {
            public FileChooserPanel()
            {
            }
        }
        FileChooserPanel fileChooserPanel = new FileChooserPanel();
      //Handle save button action.
      int returnVal = fileChooser.showSaveDialog(fileChooserPanel);
      
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            try
            {
                printer = new PrintWriter(new BufferedWriter(new FileWriter(file)));//Have the FileWriter overwrite a selected file
            }
            catch (FileNotFoundException ex)//Catch the exceptions
            {
                ex.printStackTrace();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            
            //Cycle through the field cells
            for(int i = 0; i < Board.field.length; i++)
            {
              for(int j = 0; j < Board.field.length; j++)
              {
                printer.print(Board.field[j] + " ");//Print cell number to the file with a space to help for reading
              }
            }
            
            printer.close();//Close the PrintWriter
        }
        else
        {
            System.out.println("Open command cancelled by user.");
        }
    }

}
