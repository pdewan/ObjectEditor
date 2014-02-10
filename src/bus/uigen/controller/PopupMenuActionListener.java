package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import bus.uigen.controller.menus.OEPopupMenu;
import java.awt.Component;import java.awt.event.*;
import javax.swing.*;

public class PopupMenuActionListener implements ActionListener {
  private static PopupMenuActionListener listener = null;
  
  public PopupMenuActionListener() {
  }

  public static PopupMenuActionListener getListener() {
    if (listener == null) {
      listener = new PopupMenuActionListener();
    }
    return listener;
  }

  // Redirect the event to the component that
  // brought up the popup menu.
  public void actionPerformed(ActionEvent evt) {
    JPopupMenu popup = OEPopupMenu.getPopupMenu();
    Component invoker = popup.getInvoker();
    if (invoker != null &&
	invoker instanceof ComponentWithPopupSupport) {
      ((ComponentWithPopupSupport) invoker).actionPerformed(evt);
    }
  }
}
