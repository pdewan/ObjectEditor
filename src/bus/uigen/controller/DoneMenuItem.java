package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.widgets.MenuItemSelector;import bus.uigen.widgets.VirtualMenuItem;
import bus.uigen.widgets.awt.AWTMenuItem;import java.awt.MenuItem;

public class DoneMenuItem /*extends AnAWTMenuItem*/ {	VirtualMenuItem menuItem = MenuItemSelector.createMenuItem();
  private transient uiFrame frame;
  public void setUIFrame(uiFrame parent) {
    frame = parent;
  }  public VirtualMenuItem getMenuItem() {	  return menuItem;  }

  public uiFrame getUIFrame() {
    return frame;
  }

  public DoneMenuItem(String arg) {	  menuItem.setLabel(arg);
    //super(arg);
  }
}
