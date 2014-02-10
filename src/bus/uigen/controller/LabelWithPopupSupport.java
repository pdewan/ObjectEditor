package bus.uigen.controller;import bus.uigen.uiFrame;
import javax.swing.JLabel;
import java.awt.event.*;

public abstract class LabelWithPopupSupport extends JLabel implements ComponentWithPopupSupport {
  
  public LabelWithPopupSupport() {
    super();
  }
  
  public abstract void actionPerformed(ActionEvent e);
   
}
