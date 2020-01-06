package Mine.ItemFactory;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ItemFactory {
    // ? means wildcard, extends JMenuItem mean matches type JMenuItem or any of its subclasses
    // We have JMenu, JMenuItem, so we want the upper bound is JMenuItem
    public JMenuItem createItem(Class<? extends JMenuItem> cls, String title, char m, ActionListener... l) {
        try {
            Constructor<? extends JMenuItem> c = cls.getConstructor(String.class);
            JMenuItem item = c.newInstance(title);
            item.setMnemonic(m);
            for (ActionListener li : l) {
                item.addActionListener(li);
            }
            return item;
        } catch (NoSuchMethodException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            JOptionPane.showConfirmDialog(null,
                    "Exception", "Exception", JOptionPane.DEFAULT_OPTION);
            System.exit(0);
            e.printStackTrace();
        }
        return null;
    }

    public void addItems(JMenu menu, ArrayList<JMenuItem> arr) {
        for (JMenuItem item: arr) {
            if(item != null) menu.add(item);
            else menu.addSeparator();
        }
    }
}
