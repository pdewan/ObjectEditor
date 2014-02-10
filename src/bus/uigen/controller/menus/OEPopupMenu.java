package bus.uigen.controller.menus;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import bus.uigen.controller.PopupMenuActionListener;
import javax.swing.*;
import java.util.Vector;
import java.awt.event.ActionListener;

public class OEPopupMenu {
  
  private static JPopupMenu popup = null;
  private static JMenu choosers = null, editors = null;
  private static JMenuItem editMenuItem = null;

  private static void initPopup() {
    if (popup == null) {      	  popup = new JPopupMenu();
      JMenuItem item = new JMenuItem("Edit");
      editMenuItem = item;
      //item.setEnabled(false);
      item.addActionListener(PopupMenuActionListener.getListener());
      popup.add(item);
      item = new JMenuItem("Paste");
      item.addActionListener(PopupMenuActionListener.getListener());
      popup.add(item);
      item = new JMenuItem("Create");
      //item.setEnabled(false);
      item.addActionListener(PopupMenuActionListener.getListener());
      popup.add(item);
      popup.add(new JSeparator());
      //      choosers = new JMenu("Choose");
      //popup.add(choosers);
      //editors = new JMenu("Edit");
      //popup.add(editors);
    }
  }

  public static JPopupMenu getPopupMenu() {
    initPopup();
    return popup;
  }

  /*
  private static void setChoosers(Class type) {
    // Query the uiBusAgent and set up the menu
    // Also add the uiPopupMenuListener as an
    // actionListener to the new components.
    if (type == null) {
      //choosers.removeAll();
      choosers.setVisible(false);
    }
    else {
      choosers.setVisible(true);
    }
  }

  private static void setEditors(Class type) {
    // Query the uiBusAgent and set up the menu
    if (type == null) {
      //editors.removeAll();
      editors.setVisible(false);
    }
    else {
      editors.setVisible(true);
    }
  }*/
  
  public static void configurePopupMenu(boolean editNeeded, 
					Class type) {
    initPopup();
    if (editNeeded)
      editMenuItem.setVisible(true);
    else
      editMenuItem.setVisible(false);
    //setChoosers(type);
    //setEditors(type);
  }
}
