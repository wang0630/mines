# Mines
This is a mines weeper game forked from https://github.com/publicarray/mines
## Purpose
Try to use design patterns to re-structure this project, which is for personal practice only
### 12/11: Refactor Difficulty listener
The original listener use if-else statement to separate the level
```java
private class DifficultyListener implements ActionListener
    {
        
        //Beginner Difficulty
        public void actionPerformed(ActionEvent e)
        {
            if (beginnerItem.isSelected())
            {
                Board.setDifficulty(0);
                setNoOfMines(20);
                setNoOfRows(15);
                setNoOfCols(15);
                calcDimentions();
                startNewGame();
            }

            //Intermediate Difficulty
            else if (intermediateItem.isSelected())
            {
                Board.setDifficulty(1);
                setNoOfMines(80);
                setNoOfRows(24);
                setNoOfCols(24);
                calcDimentions();
                startNewGame();
            }

            //Expert Difficulty
            else if (expertItem.isSelected())
            {
                Board.setDifficulty(2);
                setNoOfMines(200);
                setNoOfRows(30);
                setNoOfCols(30);
                calcDimentions();
                startNewGame();
            }
        }
    }
```
Template method is used to move the same logic to an abstract class called `Level`, and there are three classes for different levels
```java
public abstract class Level {
    private int DIFFICULTY;
    private int NUM_OF_ROWS;
    private int NUM_OF_COLS;
    private int NUM_OF_MINES;
    // super() is called in class Beginner, Intermediate and Expert
    Level(int d, int r, int c, int m) {
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
```

### 12/12: Make a factory class for all ui components
Original way tp create JMenuItem
```java
editMenu = new JMenu("Edit");
editMenu.setMnemonic('d');
undoItem = new JMenuItem("Undo");
undoItem.setMnemonic('Z');
undoItem.addActionListener(new UndoListener());
redoItem = new JMenuItem("Redo");
redoItem.setMnemonic('Y');
redoItem.addActionListener(new RedoListener());
```
There are lots of duplicate code
#### Solution:
Make `ItemFactory` in order to reduce duplicate code
Inside `ItemFactory.createItem()`, there are a couple of things worth mentioning:
1. The first parameter is `Class`, which is the class of the UI which we want to create
2. `Class.getConstructor(String.class)` is used to fetch the constructor which accepts a string as argument
3. We use the constructor to create the UI and upcast the type into `JMenuItem`, since we know `JMenu`, `JMenuItem` and `JRadioButtonMenuItem` are all sub classes of `JMenuItem`
4. After setting the title, mnemonic and listener(if any), we return it
