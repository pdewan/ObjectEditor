package bus.uigen.controller;import bus.uigen.uiFrame;import bus.uigen.uiGenerator;import bus.uigen.widgets.MenuItemSelector;import bus.uigen.widgets.VirtualMenuItem;import bus.uigen.widgets.events.VirtualActionEvent;import bus.uigen.widgets.events.VirtualActionListener;
public class GenMenuItem implements VirtualActionListener {
  Object obj;
  VirtualMenuItem menuItem = MenuItemSelector.createMenuItem();
  public GenMenuItem(String label, Object o) {
    //super(label);	  menuItem.setLabel(label);
    obj = o;
    menuItem.addActionListener(this);
  }
  public VirtualMenuItem getMenuItem() {	  return menuItem;  }
  public void actionPerformed(VirtualActionEvent evt) {
    uiFrame frame = uiGenerator.generateUIFrame(obj);
    frame.setVisible(true);
  }
}
