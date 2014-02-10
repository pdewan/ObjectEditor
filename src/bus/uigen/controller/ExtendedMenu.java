package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import bus.uigen.widgets.awt.AWTMenu;import java.awt.MenuItem;
public class ExtendedMenu extends AWTMenu {
  private int maxitems = 30;
  ExtendedMenu next = null;
  
  public ExtendedMenu(String title) {
    super(title);
  }
  
  public void setMaxItems(int max) {
    maxitems = max;
  }

  private ExtendedMenu getLastMenu() {
    // Find the uiExtendedMenu to add to
    ExtendedMenu m = this;
    while (m.getItemCount() >= maxitems) {
      if (m.next == null) {
	m.next = new ExtendedMenu("Others...");
	//m.oldadd(m.next);	m.add(m.next);
	return m.next;
      }
      else
	m = m.next;
    }
    return m;
  }
  /*
  private MenuItem oldadd(MenuItem item) {
    return super.add(item);
  }  */

  public MenuItem add(MenuItem item) {
    ExtendedMenu menu = getLastMenu();
    return menu.add(item);
  }
}



