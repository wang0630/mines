package Mine;

import Board.Board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import CustomGameListener.CustomGameListener;
import Level.*;
import Mine.ItemFactory.ItemFactory;
import Mine.Listeners.Listeners;
import Save.*;

public class MineFrame
{
    //time the game was paused for
    private static double pauseTime = 0.0;
    private static double startPauseTime = 0;

    //Declare GUI objects
    public static JFrame frame;
    public static JPanel gamePanel;
    private static JLabel statusbar;

    //Generic int[] stacks
    public static Stack<int[]> undoStack = new Stack<int[]>();
    public static Stack<int[]> redoStack = new Stack<int[]>();

    //Declare static integers so that they can be accessed from static getters and setters.
    private static int noOfMines = 40;
    private static int noOfRows = 24;
    private static int noOfCols = 24;

    //Static boolean to be accessed across all classes
    public static boolean playingGame;

    //Static long which will contain the time a game has started in milliseconds
    private static long startTime;

    //Init width and height for the gamePanel
    private static int height;
    private static int width;

    //Declare the menu bar and its items (GUI elements)
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu, editMenu, viewMenu, helpMenu;
    public static JMenuItem pauseItem;
    // Array to hold different items for different menus
    private ArrayList<JMenuItem> fileItems = new ArrayList<JMenuItem>();
    private ArrayList<JMenuItem> editItems = new ArrayList<JMenuItem>();
    private ArrayList<JMenuItem> viewItems = new ArrayList<JMenuItem>();
    private ArrayList<JMenuItem> diffItems = new ArrayList<JMenuItem>();
    private ArrayList<JMenuItem> helpItems = new ArrayList<JMenuItem>();

    // The map for mapping e.getActionCommand() to according level
    public static HashMap<String, Level> difficultyMap = new HashMap<String, Level>();

    public MineFrame()
    {
        frame = new JFrame();//Create the frame for the GUI

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Have the application exit when closed
        frame.setTitle("Minesweeper");//Title of the frame
        frame.setResizable(false);//Have the frame re-sizable useful for custom games
        frame.setJMenuBar(buildMenuBar());//Build the menu bar and set it as the JMenuBar

        statusbar = new JLabel("");//Set the passed-in status bar
        gamePanel = new JPanel(new BorderLayout());//New panel that contains the board
        frame.add(gamePanel);//Add gamePanel to the frame
        //frame.setLocationRelativeTo(null);//Centre the frame
        startNewGame();
        frame.setVisible(true);//Show all components on the window

        difficultyMap.put("Beginner", new Beginner());
        difficultyMap.put("Intermediate", new Intermediate());
        difficultyMap.put("Expert", new Expert());
    }

    //Method to start/restart the game when a game has been lost, restarted or loaded
    public static void startNewGame()
    {
        gamePanel.removeAll();
        undoStack.removeAllElements();
        redoStack.removeAllElements();
        gamePanel.add(statusbar, BorderLayout.SOUTH);
        gamePanel.add(new Board(statusbar, noOfMines, noOfRows, noOfCols), BorderLayout.CENTER);

        playingGame = true;//Set to true so the user may make actions
        startTime = System.currentTimeMillis(); //save the time the game started

        //new Save.SaveToDisk();//Save the generated board to disk
        //Arrays.fill(Board.Board.getField(), 0);//Set all entries in the field to 0 to prove that Save.LoadFromDisk does work
        //new Save.LoadFromDisk();//Load the board from disk
        
        calcDimentions();
        gamePanel.setPreferredSize(new Dimension(width, height));
        gamePanel.validate();
        gamePanel.repaint();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - width) / 2);
        int y = (int) ((dimension.getHeight() - height - 100) / 2);
        frame.setLocation(x, y);
        frame.pack();
    }

    //Method to create the MenuBar, its properties and associate Action Listeners
    private JMenuBar buildMenuBar()
    {
        ItemFactory itemFactory = new ItemFactory();

        // Create the fileMenu and it's items
        fileMenu = (JMenu) itemFactory.createItem(JMenu.class, "File", 'F');
        // Push items to the array
        fileItems.add(itemFactory.createItem(JMenuItem.class, "Save", 'S', new SaveListener()));
        fileItems.add(itemFactory.createItem(JMenuItem.class, "Load", 'L', new Listeners.LoadListener()));
        fileItems.add(null);
        fileItems.add(itemFactory.createItem(JMenuItem.class, "HighScore", 'H', new HighscoreListener()));
        fileItems.add(itemFactory.createItem(JMenuItem.class, "Exit", 'X', new Listeners.ExitListener()));

        // Add file items to the fileMenu
        itemFactory.addItems(fileMenu, fileItems);

        //Create the editMenu and it's items
        editMenu = (JMenu) itemFactory.createItem(JMenu.class, "Edit", 'd');
        editItems.add(itemFactory.createItem(JMenuItem.class, "Undo", 'Z', new Listeners.UndoListener()));
        editItems.add(itemFactory.createItem(JMenuItem.class, "Redo", 'Y', new Listeners.RedoListener()));

        // Add edit items to the editMenu
        itemFactory.addItems(editMenu, editItems);

        // Create the viewMenu and it's items
        viewMenu = (JMenu) itemFactory.createItem(JMenu.class, "Game", 'G');
        pauseItem = itemFactory.createItem(JMenuItem.class, "Pause", 'P', new Listeners.PauseListener());
        viewItems.add(pauseItem);
        viewItems.add(itemFactory.createItem(JMenuItem.class, "New Game", 'N', new Listeners.NewGameListener()));
        // Create difficulty radio buttons
        diffItems.add(itemFactory.createItem(JRadioButtonMenuItem.class, "Beginner", 'B', new Listeners.DifficultyListener()));
        diffItems.add(itemFactory.createItem(JRadioButtonMenuItem.class, "Intermediate", 'I', new Listeners.DifficultyListener()));
        diffItems.add(itemFactory.createItem(JRadioButtonMenuItem.class, "Expert", 'E', new Listeners.DifficultyListener()));
        diffItems.add(itemFactory.createItem(JRadioButtonMenuItem.class, "Custom...", 'C', new CustomGameListener()));

        // Create a button group and add the difficulty items to it
        ButtonGroup difficultyGroup = new ButtonGroup();
        for (JMenuItem difficultyItem: diffItems) {
            difficultyGroup.add(difficultyItem);
        }

        // Add difficulty and view items to viewMenu
        viewItems.add(null);
        itemFactory.addItems(viewMenu, viewItems);
        itemFactory.addItems(viewMenu, diffItems);

        //Create the helpMenu and it's item
        helpMenu = (JMenu) itemFactory.createItem(JMenu.class, "Help", 'H');

        helpItems.add(itemFactory.createItem(JMenuItem.class, "Solve Game", 'c', new Listeners.ResolveListener()));

        //Add help item to helpMenu
        itemFactory.addItems(helpMenu, helpItems);

        //Add File, View and Help Menus to the JMenuBar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        // return the menuBar
        return menuBar;
    }

    //Accessor for the number of mines
    public static int getNoOfMines()
    {
        return noOfMines;
    }

    //Mutator for the number of mines
    public static void setNoOfMines(int noOfMines)
    {
        MineFrame.noOfMines = noOfMines;
    }

    //Accessor for the number of columns
    public static int getNoOfCols()
    {
        return noOfCols;
    }

    //Mutator for the number of columns
    public static void setNoOfCols(int noOfCols)
    {
        MineFrame.noOfCols = noOfCols;
    }

    //Accessor for the number of rows
    public static int getNoOfRows()
    {
        return noOfRows;
    }

    //Mutator for the number of rows
    public static void setNoOfRows(int noOfRows)
    {
        MineFrame.noOfRows = noOfRows;
    }
    
    //Setter for width and height
    public static void setWidth(int width)
    {
        MineFrame.width = width;
    }
    
    public static void setHeight(int height)
    {
        MineFrame.height = height;
    }

    //Method that returns the time elapsed from the time a game was started
    public static double getCurrentTime()
    {
        double endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000.0;
    }

    public static void timePause()
    {
        if (playingGame)
        {
            startPauseTime = System.currentTimeMillis();
        }

        if (!playingGame)
        {
            double endPauseTime = System.currentTimeMillis();
            pauseTime += (endPauseTime - startPauseTime) / 1000.0;
        }
    }

    //Method that returns the score (total time - paused time)
    public static double getScore()
    {
        return getCurrentTime() - pauseTime;
    }
    
    public static void calcDimentions() {
    	width = noOfCols*15;
    	height = noOfRows*15+20;
    }
}
