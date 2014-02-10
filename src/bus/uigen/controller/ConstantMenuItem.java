package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import bus.uigen.widgets.MenuItemSelector;import bus.uigen.widgets.VirtualMenuItem;import java.awt.MenuItem;import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.lang.reflect.*;


public class ConstantMenuItem extends MenuItem implements ActionListener{
  private Object constant;  VirtualMenuItem menuItem = MenuItemSelector.createMenuItem();

  public Object getConstant() {
    return constant;
  }

  public ConstantMenuItem(String name, Object o) {
    super(name);
    constant = o;
    addActionListener(this);
  }    public VirtualMenuItem getMenuItem() {	  return menuItem;  }

  public void actionPerformed(ActionEvent e) {
    if (constant != null) {
      // Copy constant to the Clipboard
      // and give feedback
      ObjectClipboard.set(constant);
      if (MethodParameters.FeedbackOnConstant) {
	JOptionPane.showMessageDialog(null, 
				      "Copied constant "+constant+" to clipboard", 
				      "Constant Copied",
				      JOptionPane.PLAIN_MESSAGE);
      }
    }
  }
}
