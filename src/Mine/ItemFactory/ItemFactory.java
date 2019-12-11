package Mine.ItemFactory;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ItemFactory {
    public JMenuItem createItem(Class cls, String title, char m, ActionListener... l) {
        try {
            Constructor c = cls.getConstructor(String.class);
            JMenuItem item = (JMenuItem) c.newInstance(title);
            item.setMnemonic(m);
            if (l.length > 0) {
                item.addActionListener(l[0]);
            }
            return item;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
