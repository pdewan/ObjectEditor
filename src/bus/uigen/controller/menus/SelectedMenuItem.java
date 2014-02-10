package bus.uigen.controller.menus;import bus.uigen.uiFrame;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;import bus.uigen.uiGenerator;
import bus.uigen.controller.Selectable;import bus.uigen.controller.SelectionManager;import bus.uigen.widgets.MenuItemSelector;import bus.uigen.widgets.VirtualMenuItem;
public class SelectedMenuItem extends MenuItem implements ActionListener {
  VirtualMenuItem menuItem;
  public SelectedMenuItem(String label) {	  menuItem = MenuItemSelector.createMenuItem(label);
    //super(label);
    addActionListener(this);
  }  public VirtualMenuItem getMenuItem() {	  return menuItem;  }

  public void actionPerformed(ActionEvent evt) {
    Selectable cs = SelectionManager.getCurrentSelection();
    Object obj = null;
    if (cs != null) 
      obj = cs.getObject();
    if (obj != null) {
      uiFrame frame = uiGenerator.generateUIFrame(obj);
      frame.setVisible(true);
    }
  }
}
